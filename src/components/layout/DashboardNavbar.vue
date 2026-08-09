<template>
    <header
        class="navbar bg-base-100 border border-base-200 h-16 px-4 lg:px-6 shrink-0 sticky top-0 z-30 rounded-2xl shadow-sm"
    >
        <div class="flex-1 flex items-center gap-3">
            <label
                for="dashboard-drawer"
                aria-label="Open sidebar"
                class="btn btn-square btn-ghost lg:hidden"
            >
                <Bars3Icon class="w-5 h-5" />
            </label>

            <Typography
                v-if="title"
                tag="h1"
                variant="text-lg"
                weight="font-semibold"
                class="truncate"
            >
                {{ title }}
            </Typography>
        </div>

        <div class="flex-none flex items-center gap-2">
            <slot name="actions" />

            <IconButton
                :aria-label="themeToggleLabel"
                @click="themeStore.toggleTheme()"
            >
                <SunIcon
                    v-if="themeStore.isDark"
                    class="w-5 h-5"
                />
                <MoonIcon
                    v-else
                    class="w-5 h-5"
                />
            </IconButton>

            <div class="dropdown dropdown-end">
                <div
                    tabindex="0"
                    role="button"
                    class="btn btn-ghost btn-circle avatar"
                >
                    <div class="w-9 rounded-full bg-primary text-primary-content flex items-center justify-center">
                        <span class="text-sm font-semibold">{{ userInitial }}</span>
                    </div>
                </div>
                <ul
                    tabindex="0"
                    class="dropdown-content menu bg-base-100 rounded-box z-50 mt-3 w-52 p-2 shadow-lg border border-base-300"
                >
                    <li class="menu-title">
                        <Typography
                            tag="span"
                            variant="text-xs"
                            color="text-base-content/60"
                        >
                            {{ auth.userData.fullName || auth.userData.userName }}
                        </Typography>
                    </li>
                    <div class="divider my-1"></div>
                    <li>
                        <a
                            class="text-error"
                            @click="handleLogout"
                        >
                            Logout
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </header>
</template>

<script setup>
    import { computed } from 'vue';
    import { useRouter } from 'vue-router';
    import { useAuthStore } from '@/stores/auth';
    import { useThemeStore } from '@/stores/theme';
    import Typography from '@/components/base/typography/Typography.vue';
    import IconButton from '@/components/base/button/IconButton.vue';
    import { Bars3Icon, MoonIcon, SunIcon } from '@heroicons/vue/24/outline';

    defineProps({
        title: {
            type: String,
            default: '',
        },
    });

    const router = useRouter();
    const auth = useAuthStore();
    const themeStore = useThemeStore();

    const userInitial = computed(() => {
        const name = auth.userData.fullName || auth.userData.userName || '?';
        return name.charAt(0).toUpperCase();
    });

    const themeToggleLabel = computed(() => (themeStore.isDark ? 'Switch to light mode' : 'Switch to dark mode'));

    function handleLogout() {
        auth.logout();
        router.push({ name: 'login' });
    }
</script>
