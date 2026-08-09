<template>
    <div class="drawer lg:drawer-open">
        <input
            id="dashboard-drawer"
            v-model="isDrawerOpen"
            type="checkbox"
            class="drawer-toggle"
        />

        <div class="drawer-content flex flex-col min-h-screen bg-base-200">
            <div
                class="mx-auto w-full max-w-[85rem] px-4 sm:px-6 lg:px-8 flex-1 flex flex-col"
            >
                <div class="pt-2 w-full">
                    <DashboardNavbar :title="title">
                        <template #actions>
                            <slot name="navbar-actions" />
                        </template>
                    </DashboardNavbar>
                </div>

                <main class="flex-1 py-4 lg:py-6 overflow-x-hidden">
                    <slot />
                </main>
            </div>
        </div>

        <div class="drawer-side z-40">
            <label
                for="dashboard-drawer"
                aria-label="Close sidebar"
                class="drawer-overlay"
            ></label>

            <DashboardSidebar
                :nav-items="navItems"
                :brand="brand"
                @navigate="isDrawerOpen = false"
            />
        </div>
    </div>
</template>

<script setup>
    import { ref } from 'vue';
    import DashboardNavbar from './DashboardNavbar.vue';
    import DashboardSidebar from './DashboardSidebar.vue';

    defineProps({
        title: {
            type: String,
            default: '',
        },
        brand: {
            type: Object,
            default: () => ({ name: 'Dashboard', logo: null }),
        },
        navItems: {
            type: Array,
            required: true,
            // [{ label, icon, to, children: [{ label, to }] }]
        },
    });

    const isDrawerOpen = ref(false);
</script>
