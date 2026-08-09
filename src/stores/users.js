import { defineStore } from 'pinia';

export const useUsersStore = defineStore('users', {
    state: () => ({
        table: {
            data: [],
            totalItems: 0,
            totalPages: 0,
            isLoading: false,
            isLoadingMore: false,
            hasError: false,
        },
        payloads: {
            search: '',
            page: 0,
            size: 5,
            sortBy: 'full_name',
            sortOrder: 'asc', // asc | desc
        },
        view: {
            isViewing: 'table', // 'table' | 'list' | 'grid'
        },
    }),

    getters: {
        hasMore: (state) => {
            if (!state.table.totalPages) return false;
            return state.payloads.page < state.table.totalPages - 1;
        },
    },

    actions: {
        goToPage(page) {
            this.payloads.page = page;
        },

        setPageSize(size) {
            this.payloads.size = size;
            this.payloads.page = 0;
        },

        setView(view) {
            this.view.isViewing = view;
        },
    },
});
