<template>
    <CardBaseContent
        title="Monitoring List"
        subtitle="Monitor customer call sentiment"
        :icon="PhoneIcon"
    >
        <template #actions>
            <div class="join hidden lg:flex gap-2 p-2 bg-base-300 rounded-xl">
                <IconButton
                    class="join-item rounded-lg"
                    aria-label="Table view"
                    size="sm"
                    :circle="false"
                    square
                    :ghost="store.view.isViewing !== 'table'"
                    :variant="store.view.isViewing === 'table' ? 'primary' : ''"
                    @click="store.view.isViewing = 'table'"
                >
                    <TableCellsIcon class="w-4 h-4" />
                </IconButton>
                <IconButton
                    class="join-item rounded-lg"
                    aria-label="List view"
                    size="sm"
                    :circle="false"
                    square
                    :ghost="store.view.isViewing !== 'list'"
                    :variant="store.view.isViewing === 'list' ? 'primary' : ''"
                    @click="store.view.isViewing = 'list'"
                >
                    <ListBulletIcon class="w-4 h-4" />
                </IconButton>
                <IconButton
                    class="join-item rounded-lg"
                    aria-label="Grid view"
                    size="sm"
                    :circle="false"
                    square
                    :ghost="store.view.isViewing !== 'grid'"
                    :variant="store.view.isViewing === 'grid' ? 'primary' : ''"
                    @click="store.view.isViewing = 'grid'"
                >
                    <Squares2X2Icon class="w-4 h-4" />
                </IconButton>
            </div>
        </template>

        <div class="flex flex-row items-center gap-2 mb-2">
            <FormFieldset
                v-model="searchInput"
                class="flex-1 min-w-0"
                type="text"
                placeholder="Search ..."
            >
                <template #prefix>
                    <MagnifyingGlassIcon class="w-4 h-4 text-base-content/40 shrink-0" />
                </template>
                <template
                    v-if="searchInput"
                    #suffix
                >
                    <button
                        type="button"
                        class="text-base-content/40 hover:text-base-content/70 transition-colors"
                        @click="searchInput = ''"
                    >
                        <XMarkIcon class="w-4 h-4" />
                    </button>
                </template>
            </FormFieldset>

            <div class="relative shrink-0 lg:hidden">
                <IconButton
                    aria-label="Filter records"
                    size="sm"
                    :circle="false"
                    square
                    :ghost="false"
                    variant="primary"
                    class="rounded-xl"
                    @click="isFilterOpen = true"
                >
                    <FunnelIcon class="w-4 h-4" />
                </IconButton>
                <span
                    v-if="store.activeFilterCount > 0"
                    class="badge badge-error badge-xs text-white absolute -top-1 -right-1"
                >
                    {{ store.activeFilterCount }}
                </span>
            </div>

            <Button
                variant="primary"
                class="relative hidden lg:inline-flex shrink-0 rounded-xl"
                @click="isFilterOpen = true"
            >
                <FunnelIcon class="w-4 h-4" />
                Filter
                <span
                    v-if="store.activeFilterCount > 0"
                    class="badge badge-error badge-sm text-white"
                >
                    {{ store.activeFilterCount }}
                </span>
            </Button>

            <div class="join flex lg:hidden gap-1 p-1 bg-base-300 rounded-xl shrink-0">
                <IconButton
                    class="join-item rounded-lg"
                    aria-label="List view"
                    size="sm"
                    :circle="false"
                    square
                    :ghost="store.view.isViewing !== 'list'"
                    :variant="store.view.isViewing === 'list' ? 'primary' : ''"
                    @click="store.view.isViewing = 'list'"
                >
                    <ListBulletIcon class="w-4 h-4" />
                </IconButton>
                <IconButton
                    class="join-item rounded-lg"
                    aria-label="Grid view"
                    size="sm"
                    :circle="false"
                    square
                    :ghost="store.view.isViewing !== 'grid'"
                    :variant="store.view.isViewing === 'grid' ? 'primary' : ''"
                    @click="store.view.isViewing = 'grid'"
                >
                    <Squares2X2Icon class="w-4 h-4" />
                </IconButton>
            </div>
        </div>

        <div
            v-if="store.table.isLoading"
            class="overflow-x-auto rounded-xl border border-base-content/5 bg-base-100"
        >
            <TableSkeleton
                :columns="5"
                :rows="store.payloads.size"
            />
        </div>

        <ErrorState
            v-else-if="store.table.hasError && store.table.data.length === 0"
            description="We couldn't load the call records. Check your connection and try again."
            @retry="() => fetchData()"
        />

        <EmptyState
            v-else-if="!store.table.isLoading && store.table.data.length === 0"
            :icon="PhoneIcon"
            title="No call records found"
            description="Try adjusting your search or filter."
        />

        <template v-else-if="store.table.data.length > 0">
            <CardTableView
                v-if="!isMobile && store.view.isViewing === 'table'"
                :records="store.table.data"
                :start-index="rowStartIndex"
            />
            <CardListView
                v-else-if="store.view.isViewing === 'list' || (isMobile && store.view.isViewing === 'table')"
                :records="store.table.data"
                :start-index="isMobile ? 0 : rowStartIndex"
            />
            <CardGridView
                v-else
                :records="store.table.data"
            />

            <div
                v-if="isMobile"
                ref="loadMoreSentinel"
                class="flex items-center justify-center py-3"
            >
                <span
                    v-if="store.table.isLoadingMore"
                    class="loading loading-spinner loading-sm text-primary"
                ></span>
                <Typography
                    v-else-if="!store.hasMore"
                    tag="p"
                    variant="text-xs"
                    color="text-base-content/40"
                >
                    End of results
                </Typography>
            </div>
        </template>

        <Pagination
            v-if="!isMobile && !store.table.isLoading && !store.table.hasError && store.table.data.length > 0"
            :page="store.payloads.page"
            :total-pages="store.table.totalPages"
            :total-items="store.table.totalItems"
            :page-size="store.payloads.size"
            @change="handlePageChange"
            @update:page-size="handlePageSizeChange"
        />
    </CardBaseContent>

    <Modal
        v-model="isFilterOpen"
        title="Filter records"
    >
        <div class="flex flex-col gap-4">
            <div class="form-control flex flex-col gap-1">
                <label class="label-text text-sm font-medium">Sentiment</label>
                <select
                    v-model="draftFilters.sentiment"
                    class="select select-bordered w-full"
                >
                    <option value="">All sentiments</option>
                    <option value="above70">Above 70 (Positive)</option>
                    <option value="below70">Below 70 (Negative)</option>
                </select>
            </div>

            <div class="form-control flex flex-col gap-1">
                <label class="label-text text-sm font-medium">Sort by</label>
                <select
                    v-model="draftFilters.sortBy"
                    class="select select-bordered w-full"
                >
                    <option value="createdAt">Call time</option>
                    <option value="sentiment_score">Sentiment score</option>
                    <option value="cs_name">CS name</option>
                </select>
            </div>

            <div class="form-control flex flex-col gap-1">
                <label class="label-text text-sm font-medium">Order</label>
                <select
                    v-model="draftFilters.sortOrder"
                    class="select select-bordered w-full"
                >
                    <option value="desc">Newest / Highest first</option>
                    <option value="asc">Oldest / Lowest first</option>
                </select>
            </div>
        </div>

        <template #footer>
            <Button
                variant="neutral"
                ghost
                @click="handleResetFilters"
            >
                Reset
            </Button>
            <Button
                variant="primary"
                @click="applyFilters"
            >
                Apply filter
            </Button>
        </template>
    </Modal>
</template>

<script setup>
    import { ref, reactive, computed, onMounted, onBeforeUnmount, watch, nextTick } from 'vue';
    import {
        PhoneIcon,
        MagnifyingGlassIcon,
        XMarkIcon,
        FunnelIcon,
        ListBulletIcon,
        Squares2X2Icon,
        TableCellsIcon,
    } from '@heroicons/vue/24/outline';

    import CardBaseContent from '@/components/base/card/CardBaseContent.vue';
    import Button from '@/components/base/button/Button.vue';
    import IconButton from '@/components/base/button/IconButton.vue';
    import FormFieldset from '@/components/base/form/FormFieldset.vue';
    import Modal from '@/components/base/modal/BaseModal.vue';
    import Pagination from '@/components/base/pagination/BasePagination.vue';
    import TableSkeleton from '@/components/base/skeleton/TableSkeleton.vue';
    import EmptyState from '@/components/base/state-handler/TableEmptyState.vue';
    import ErrorState from '@/components/base/state-handler/TableErrorState.vue';
    import Typography from '@/components/base/typography/Typography.vue';

    import CardTableView from './_components/CardTableView.vue';
    import CardListView from './_components/CardListView.vue';
    import CardGridView from './_components/CardGridView.vue';

    import { useCallMonitoringStore } from '@/stores/calll-monitoring';
    import { getListCallRecords } from '@/api/call-monitoring/getListCallRecords.js';

    const store = useCallMonitoringStore();

    const searchInput = ref(store.payloads.search);
    const isFilterOpen = ref(false);
    const isMobile = ref(false);
    const loadMoreSentinel = ref(null);
    const draftFilters = reactive({
        sentiment: store.payloads.sentiment,
        sortBy: store.payloads.sortBy,
        sortOrder: store.payloads.sortOrder,
    });

    const rowStartIndex = computed(() => store.payloads.page * store.payloads.size);

    let mediaQuery;
    let observer;

    function syncViewportMode() {
        const nextIsMobile = mediaQuery?.matches ?? false;
        const wasMobile = isMobile.value;
        isMobile.value = nextIsMobile;

        if (nextIsMobile && store.view.isViewing === 'table') {
            store.view.isViewing = 'list';
        }

        if (wasMobile && !nextIsMobile) {
            store.payloads.page = 0;
            fetchData();
        }

        if (!wasMobile && nextIsMobile) {
            store.payloads.page = 0;
            fetchData();
            nextTick(setupInfiniteScroll);
        }
    }

    async function fetchData({ append = false } = {}) {
        if (append) {
            if (store.table.isLoadingMore) return;
            store.table.isLoadingMore = true;
        } else {
            store.table.isLoading = true;
            store.table.hasError = false;
        }

        try {
            const response = await getListCallRecords(store.payloads);
            const items = response.data.items ?? [];

            store.table.data = append ? [...store.table.data, ...items] : items;
            store.table.totalItems = response.data.total_items;
            store.table.totalPages = response.data.total_pages;
        } catch (error) {
            console.error(error);
            if (!append) {
                store.table.hasError = true;
                store.table.data = [];
            }
        } finally {
            store.table.isLoading = false;
            store.table.isLoadingMore = false;
        }
    }

    async function loadMore() {
        if (!isMobile.value || store.table.isLoading || store.table.isLoadingMore || !store.hasMore) {
            return;
        }

        store.payloads.page += 1;
        await fetchData({ append: true });
    }

    function setupInfiniteScroll() {
        observer?.disconnect();

        if (!isMobile.value || !loadMoreSentinel.value) return;

        observer = new IntersectionObserver(
            (entries) => {
                if (entries.some((entry) => entry.isIntersecting)) {
                    loadMore();
                }
            },
            { rootMargin: '160px' }
        );

        observer.observe(loadMoreSentinel.value);
    }

    let searchTimeout;
    watch(searchInput, (value) => {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            store.payloads.search = value;
            store.payloads.page = 0;
            fetchData().then(() => nextTick(setupInfiniteScroll));
        }, 400);
    });

    watch(
        () => store.table.data.length,
        () => {
            if (isMobile.value) nextTick(setupInfiniteScroll);
        }
    );

    function applyFilters() {
        store.payloads.sentiment = draftFilters.sentiment;
        store.payloads.sortBy = draftFilters.sortBy;
        store.payloads.sortOrder = draftFilters.sortOrder;
        store.payloads.page = 0;
        isFilterOpen.value = false;
        fetchData().then(() => nextTick(setupInfiniteScroll));
    }

    function handleResetFilters() {
        store.resetFilters();
        draftFilters.sentiment = '';
        draftFilters.sortBy = 'createdAt';
        draftFilters.sortOrder = 'desc';
        isFilterOpen.value = false;
        fetchData().then(() => nextTick(setupInfiniteScroll));
    }

    function handlePageChange(page) {
        store.payloads.page = page;
        fetchData();
    }

    function handlePageSizeChange(size) {
        store.payloads.size = size;
        store.payloads.page = 0;
        fetchData();
    }

    onMounted(() => {
        mediaQuery = window.matchMedia('(max-width: 1023px)');
        syncViewportMode();
        mediaQuery.addEventListener('change', syncViewportMode);

        if (!isMobile.value) {
            fetchData();
        } else {
            nextTick(setupInfiniteScroll);
        }
    });

    onBeforeUnmount(() => {
        mediaQuery?.removeEventListener('change', syncViewportMode);
        observer?.disconnect();
        clearTimeout(searchTimeout);
    });
</script>
