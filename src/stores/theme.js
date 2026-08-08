import { defineStore } from 'pinia';
import { useUtilsStore } from './UtilsStore';

const DEFAULT_THEME = 'light';
const DARK_THEMES = ['dark', 'sunset', 'nord'];

export const useThemeStore = defineStore('theme', {
    state: () => ({
        theme: localStorage.getItem('theme') || DEFAULT_THEME,
    }),

    actions: {
        setTheme(theme) {
            this.theme = theme;
            localStorage.setItem('theme', theme);
            document.documentElement.setAttribute('data-theme', theme);
            document.body.setAttribute('data-theme', theme);

            document.querySelectorAll('dialog').forEach((dialog) => {
                dialog.setAttribute('data-theme', theme);
            });
        },

        initTheme() {
            document.documentElement.setAttribute('data-theme', this.theme);
            document.body.setAttribute('data-theme', this.theme);
        },

        toggleTheme() {
            const next = this.theme === 'light' ? 'dark' : 'light';
            this.setTheme(next);
        },
    },
});
