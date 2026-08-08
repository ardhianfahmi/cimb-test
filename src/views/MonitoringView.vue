<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getCallRecords } from '@/api/call-monitoring/list'
import {
  formatTimestamp,
  getMinPeriodDateInput,
  getTodayDateInput,
  toEndDateTime,
  toStartDateTime,
} from '../utils/date'

const router = useRouter()
const auth = useAuthStore()

// filters
const search = ref('')
const startDate = ref('')
const endDate = ref('')
const sentimentFilter = ref('') // '', 'below70', 'above70'

// sorting + pagination
const sortBy = ref('callTimestamp')
const sortDir = ref('desc') // asc | desc
const page = ref(0) // backend page is 0-based
const size = 5

// table state
const records = ref([])
const totalElements = ref(0)
const totalPages = ref(0)
const loading = ref(false)
const errorMessage = ref('')

const minDate = getMinPeriodDateInput()
const maxDate = getTodayDateInput()

const columns = [
  { key: 'callId', label: 'Call ID' },
  { key: 'callTimestamp', label: 'Call Timestamp' },
  { key: 'csName', label: 'CS Name' },
  { key: 'customerName', label: 'Nama Nasabah' },
  { key: 'sentimentScore', label: 'Sentiment Score Nasabah' },
]

const pageLabel = computed(() => {
  if (totalPages.value === 0) return 'Page 0 of 0'
  return `Page ${page.value + 1} of ${totalPages.value}`
})

const rowNumberStart = computed(() => page.value * size)

async function fetchRecords() {
  loading.value = true
  errorMessage.value = ''

  try {
    const params = {
      page: page.value,
      size,
      sort: `${sortBy.value},${sortDir.value}`,
    }

    if (search.value.trim() !== '') {
      params.search = search.value.trim()
    }

    if (startDate.value) {
      params.startDate = toStartDateTime(startDate.value)
    }

    if (endDate.value) {
      params.endDate = toEndDateTime(endDate.value)
    }

    if (sentimentFilter.value) {
      params.sentimentFilter = sentimentFilter.value
    }

    const result = await getCallRecords(params)

    if (result.status !== 200) {
      throw new Error(result.message || 'Failed to load data')
    }

    const data = result.data
    records.value = data.content || []
    totalElements.value = data.totalElements || 0
    totalPages.value = data.totalPages || 0
  } catch (error) {
    const apiMessage =
      error?.details?.response?.data?.message ||
      error?.response?.data?.message ||
      error?.message
    errorMessage.value = apiMessage || 'Failed to load data'
    records.value = []
    totalElements.value = 0
    totalPages.value = 0

    // if token expired / unauthorized
    const status = error?.details?.response?.status || error?.response?.status
    if (status === 401) {
      auth.logout()
      router.push('/auth/login')
    }
  } finally {
    loading.value = false
  }
}

function onSort(columnKey) {
  if (sortBy.value === columnKey) {
    // click same column = toggle asc/desc
    sortDir.value = sortDir.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortBy.value = columnKey
    sortDir.value = 'asc'
  }
  // keep page, or reset? story: sorting while filters active is fine;
  // usually reset page when sort changes
  page.value = 0
  fetchRecords()
}

function sortIcon(columnKey) {
  if (sortBy.value !== columnKey) return '↕'
  return sortDir.value === 'asc' ? '↑' : '↓'
}

function previousPage() {
  if (page.value > 0) {
    page.value -= 1
    fetchRecords()
  }
}

function nextPage() {
  if (page.value + 1 < totalPages.value) {
    page.value += 1
    fetchRecords()
  }
}

function logout() {
  auth.logout()
  router.push('/auth/login')
}

function sentimentBadgeClass(score) {
  if (score < 70) return 'badge badge-error badge-outline'
  return 'badge badge-success badge-outline'
}

// when filter change -> reset to first page + reload
watch([search, startDate, endDate, sentimentFilter], () => {
  page.value = 0
  fetchRecords()
})

onMounted(() => {
  fetchRecords()
})
</script>

<template>
  <div class="min-h-screen bg-base-200">
    <!-- top navbar -->
    <div class="navbar bg-base-100 shadow-sm px-4">
      <div class="flex-1">
        <span class="text-xl font-bold">Call Monitoring</span>
        <span class="ml-3 badge badge-primary badge-outline">Monitoring</span>
      </div>
      <div class="flex-none gap-3 items-center">
        <span class="text-sm hidden sm:inline">
          {{ auth.userData.fullName || auth.userData.userName }}
        </span>
        <button class="btn btn-ghost btn-sm" @click="logout">Logout</button>
      </div>
    </div>

    <main class="p-4 md:p-6 max-w-7xl mx-auto space-y-4">
      <div>
        <h1 class="text-2xl font-bold">Monitoring Call Sentiment</h1>
        <p class="text-sm opacity-70">
          Cari dan filter data call untuk menemukan nasabah yang butuh perhatian.
        </p>
      </div>

      <!-- filters -->
      <div class="card bg-base-100 shadow">
        <div class="card-body gap-4">
          <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-3">
            <fieldset class="fieldset">
              <legend class="fieldset-legend">Search</legend>
              <input
                v-model="search"
                type="text"
                class="input input-bordered w-full"
                placeholder="Call ID, CS, Nasabah, Score..."
              />
            </fieldset>

            <fieldset class="fieldset">
              <legend class="fieldset-legend">Start Period</legend>
              <input
                v-model="startDate"
                type="date"
                class="input input-bordered w-full"
                :min="minDate"
                :max="maxDate"
              />
            </fieldset>

            <fieldset class="fieldset">
              <legend class="fieldset-legend">End Period</legend>
              <input
                v-model="endDate"
                type="date"
                class="input input-bordered w-full"
                :min="minDate"
                :max="maxDate"
              />
            </fieldset>

            <fieldset class="fieldset">
              <legend class="fieldset-legend">Sentiment Filter</legend>
              <select v-model="sentimentFilter" class="select select-bordered w-full">
                <option value="">Semua</option>
                <option value="below70">Di bawah 70%</option>
                <option value="above70">70% atau lebih</option>
              </select>
            </fieldset>
          </div>

          <p class="text-xs opacity-60">
            Periode yang bisa dipilih maksimal 3 bulan terakhir ({{ minDate }} s/d {{ maxDate }}).
          </p>
        </div>
      </div>

      <div v-if="errorMessage" role="alert" class="alert alert-error">
        <span>{{ errorMessage }}</span>
      </div>

      <!-- table -->
      <div class="card bg-base-100 shadow">
        <div class="card-body p-0">
          <div class="overflow-x-auto">
            <table class="table table-zebra">
              <thead>
                <tr>
                  <th>No.</th>
                  <th
                    v-for="col in columns"
                    :key="col.key"
                    class="cursor-pointer select-none"
                    @click="onSort(col.key)"
                  >
                    <span class="inline-flex items-center gap-1">
                      {{ col.label }}
                      <span class="opacity-60">{{ sortIcon(col.key) }}</span>
                    </span>
                  </th>
                </tr>
              </thead>

              <tbody>
                <tr v-if="loading">
                  <td colspan="6" class="text-center py-10">
                    <span class="loading loading-spinner loading-md"></span>
                    <p class="mt-2 text-sm opacity-70">Loading data...</p>
                  </td>
                </tr>

                <tr v-else-if="records.length === 0">
                  <td colspan="6" class="text-center py-10">
                    <div class="flex flex-col items-center gap-2">
                      <p class="font-semibold">Tidak ada data</p>
                      <p class="text-sm opacity-70">
                        Tidak ada call record yang cocok dengan filter saat ini.
                        Coba ubah search / periode / sentiment filter.
                      </p>
                    </div>
                  </td>
                </tr>

                <tr v-for="(row, index) in records" v-else :key="row.callId">
                  <td>{{ rowNumberStart + index + 1 }}</td>
                  <td class="font-mono text-sm">{{ row.callId }}</td>
                  <td>{{ formatTimestamp(row.callTimestamp) }}</td>
                  <td>{{ row.csName }}</td>
                  <td>{{ row.customerName }}</td>
                  <td>
                    <span :class="sentimentBadgeClass(row.sentimentScore)">
                      {{ row.sentimentScore }}%
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- pagination -->
          <div class="flex flex-col sm:flex-row items-center justify-between gap-3 p-4 border-t border-base-200">
            <p class="text-sm opacity-70">
              Total {{ totalElements }} records · {{ pageLabel }} · 5 per page
            </p>

            <div class="join">
              <button
                class="btn btn-sm join-item"
                :disabled="loading || page === 0"
                @click="previousPage"
              >
                Previous
              </button>
              <button
                class="btn btn-sm join-item"
                :disabled="loading || page + 1 >= totalPages"
                @click="nextPage"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
