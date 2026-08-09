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
                :rows="5"
            />
        </div>

        <ErrorState
            v-else-if="store.table.hasError && store.table.data.length === 0"
            description="We couldn't load the users. Check your connection and try again."
            @retry="fetchData"
        />

        <EmptyState
            v-else-if="!store.table.isLoading && store.table.data.length === 0"
            :icon="UsersIcon"
            title="No users found"
            description="There are no users to display."
        />

        <template v-else-if="store.table.data.length > 0">
            <UserTableView
                v-if="!isMobile && store.view.isViewing === 'table'"
                :users="store.table.data"
                :start-index="0"
            />
            <UserListView
                v-else-if="store.view.isViewing === 'list' || (isMobile && store.view.isViewing === 'table')"
                :users="store.table.data"
            />
            <UserGridView
                v-else
                :users="store.table.data"
            />
        </template>
    </CardBaseContent>
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount } from 'vue';
    import {
        UsersIcon,
        ListBulletIcon,
        Squares2X2Icon,
        TableCellsIcon,
    } from '@heroicons/vue/24/outline';

    import CardBaseContent from '@/components/base/card/CardBaseContent.vue';
    import IconButton from '@/components/base/button/IconButton.vue';
    import TableSkeleton from '@/components/base/skeleton/TableSkeleton.vue';
    import EmptyState from '@/components/base/state-handler/TableEmptyState.vue';
    import ErrorState from '@/components/base/state-handler/TableErrorState.vue';

    import UserTableView from './_components/UserTableView.vue';
    import UserListView from './_components/UserListView.vue';
    import UserGridView from './_components/UserGridView.vue';

    import { useUsersStore } from '@/stores/users';
    import { getListUsers } from '@/api/users/getListUsers.js';

    const store = useUsersStore();

    const isMobile = ref(false);

    let mediaQuery;

    function syncViewportMode() {
        isMobile.value = mediaQuery?.matches ?? false;

        if (isMobile.value && store.view.isViewing === 'table') {
            store.view.isViewing = 'list';
        }
    }

    async function fetchData() {
        store.table.isLoading = true;
        store.table.hasError = false;

        try {
            const response = await getListUsers();
            store.table.data = response.data?.items ?? response.data ?? [];
        } catch (error) {
            console.error(error);
            store.table.hasError = true;
            store.table.data = [];
        } finally {
            store.table.isLoading = false;
        }
    }

    onMounted(() => {
        mediaQuery = window.matchMedia('(max-width: 1023px)');
        syncViewportMode();
        mediaQuery.addEventListener('change', syncViewportMode);
        fetchData();
    });

    onBeforeUnmount(() => {
        mediaQuery?.removeEventListener('change', syncViewportMode);
    });
</script>
