import { defineStore } from 'pinia';

export const useCallMonitoringStore = defineStore('callMonitoring', {
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
            sentiment: '', // '' | 'below70' | 'above70'
            page: 0,
            size: 5,
            sortBy: 'createdAt',
            sortOrder: 'desc', // asc | desc
        },
        view: {
            isViewing: 'table', // 'table' | 'list' | 'grid'
        },
    }),

    getters: {
        activeFilterCount: (state) => {
            let count = 0;
            if (state.payloads.sentiment) count++;
            if (state.payloads.sortBy !== 'createdAt') count++;
            if (state.payloads.sortOrder !== 'desc') count++;
            return count;
        },

        hasMore: (state) => {
            if (!state.table.totalPages) return false;
            return state.payloads.page < state.table.totalPages - 1;
        },
    },

    actions: {
        resetFilters() {
            this.payloads.sentiment = '';
            this.payloads.sortBy = 'createdAt';
            this.payloads.sortOrder = 'desc';
            this.payloads.page = 0;
        },

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
