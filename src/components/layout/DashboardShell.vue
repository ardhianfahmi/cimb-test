<template>
    <div class="drawer lg:drawer-open">
        <input
            id="dashboard-drawer"
            v-model="isDrawerOpen"
            type="checkbox"
            class="drawer-toggle"
        />

        <div class="drawer-content flex flex-col min-h-screen">
            <div class="p-2 w-full">
                <DashboardNavbar :title="title">
                    <template #actions>
                        <slot name="navbar-actions" />
                    </template>
                </DashboardNavbar>
            </div>

            <main class="flex-1 p-4 lg:p-6 overflow-x-hidden">
                <slot />
            </main>
        </div>

        <div class="drawer-side z-40">
            <label
                for="dashboard-drawer"
                aria-label="Close sidebar"
                class="drawer-overlay"
            ></label>

            <div class="p-2 h-full">
                <DashboardSidebar
                    :nav-items="navItems"
                    :brand="brand"
                    @navigate="isDrawerOpen = false"
                />
            </div>
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
