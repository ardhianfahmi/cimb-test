<template>
    <div class="drawer lg:drawer-open">
        <input
            id="dashboard-drawer"
            v-model="isDrawerOpen"
            type="checkbox"
            class="drawer-toggle"
        />

        <div class="drawer-content flex flex-col min-h-screen bg-base-200">
            <Navbar :title="title">
                <template #actions>
                    <slot name="navbar-actions" />
                </template>
            </Navbar>

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

            <Sidebar
                :nav-items="navItems"
                :brand="brand"
                @navigate="isDrawerOpen = false"
            />
        </div>
    </div>
</template>

<script setup>
    import { ref } from 'vue';
    import Navbar from './Navbar.vue';
    import Sidebar from './Sidebar.vue';

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
