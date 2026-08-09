import { defineStore } from 'pinia';

export const useCallMonitoringStore = defineStore('callMonitoring', {
    state: () => ({
        table: {
            data: [],
            page: 0,
            size: 5,
            totalItems: 0,
            totalPages: 0,
            isLoading: false,
        },
        payloads: {
            search: '',
            sentiment: '', //below70 | above70
            page: 0,
            size: 5,
            sortBy: 'createdAt',
            sortOrder: 'desc', //asc | desc
        },
        filter: {
            search: '',
            sentiment: '',
            sortBy: 'createdAt',
            sortOrder: 'desc',
        },
        view: {
            isViewing: 'grid' | 'list',
        },
    }),
});
