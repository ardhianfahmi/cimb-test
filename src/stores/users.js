import { defineStore } from 'pinia';

export const useUsersStore = defineStore('users', {
    state: () => ({
        table: {
            data: [],
            isLoading: false,
            hasError: false,
        },
        view: {
            isViewing: 'table', // 'table' | 'list' | 'grid'
        },
    }),
});
