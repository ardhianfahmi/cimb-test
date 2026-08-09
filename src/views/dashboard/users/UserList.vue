<template>
    <CardBaseContent
        title="Users"
        subtitle="Manage system users and supervisor accounts"
        :icon="UsersIcon"
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
            description="We couldn't load the users. Check your connection and try again."
            @retry="() => fetchData()"
        />

        <EmptyState
            v-else-if="!store.table.isLoading && store.table.data.length === 0"
            :icon="UsersIcon"
            title="No users found"
            description="Try adjusting your search."
        />

        <template v-else-if="store.table.data.length > 0">
            <UserTableView
                v-if="!isMobile && store.view.isViewing === 'table'"
                :users="store.table.data"
                :start-index="rowStartIndex"
            />
            <UserListView
                v-else-if="store.view.isViewing === 'list' || (isMobile && store.view.isViewing === 'table')"
                :users="store.table.data"
            />
            <UserGridView
                v-else
                :users="store.table.data"
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
    </CardBaseContent>
</template>

<script setup>
    import { ref, computed, onMounted, onBeforeUnmount, watch, nextTick } from 'vue';
    import {
        UsersIcon,
        MagnifyingGlassIcon,
        XMarkIcon,
        ListBulletIcon,
        Squares2X2Icon,
        TableCellsIcon,
    } from '@heroicons/vue/24/outline';

    import CardBaseContent from '@/components/base/card/CardBaseContent.vue';
    import IconButton from '@/components/base/button/IconButton.vue';
    import FormFieldset from '@/components/base/form/FormFieldset.vue';
    import Pagination from '@/components/base/pagination/BasePagination.vue';
    import TableSkeleton from '@/components/base/skeleton/TableSkeleton.vue';
    import EmptyState from '@/components/base/state-handler/TableEmptyState.vue';
    import ErrorState from '@/components/base/state-handler/TableErrorState.vue';
    import Typography from '@/components/base/typography/Typography.vue';

    import UserTableView from './_components/UserTableView.vue';
    import UserListView from './_components/UserListView.vue';
    import UserGridView from './_components/UserGridView.vue';

    import { useUsersStore } from '@/stores/users';
    import { getListUsers } from '@/api/users/getListUsers.js';

    const store = useUsersStore();

    const searchInput = ref(store.payloads.search);
    const isMobile = ref(false);
    const loadMoreSentinel = ref(null);

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
            const response = await getListUsers(store.payloads);
            const items = response.data?.items ?? [];

            store.table.data = append ? [...store.table.data, ...items] : items;
            store.table.totalItems = response.data?.total_items ?? items.length;
            store.table.totalPages =
                response.data?.total_pages ??
                Math.max(1, Math.ceil((store.table.totalItems || items.length) / store.payloads.size));
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
