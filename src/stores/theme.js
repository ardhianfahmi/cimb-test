import { defineStore } from 'pinia';

const DEFAULT_THEME = 'light';

export const useThemeStore = defineStore('theme', {
    state: () => ({
        theme: localStorage.getItem('theme') || DEFAULT_THEME,
    }),

    getters: {
        isDark: (state) => state.theme === 'dark',
    },

    actions: {
        setTheme(theme) {
            this.theme = theme;
            localStorage.setItem('theme', theme);
            document.documentElement.setAttribute('data-theme', theme);
            document.body.setAttribute('data-theme', theme);
            document.documentElement.classList.toggle('dark', theme === 'dark');

            document.querySelectorAll('dialog').forEach((dialog) => {
                dialog.setAttribute('data-theme', theme);
            });
        },

        initTheme() {
            this.setTheme(this.theme);
        },

        toggleTheme() {
            const next = this.theme === 'dark' ? 'light' : 'dark';
            this.setTheme(next);
        },
    },
});
