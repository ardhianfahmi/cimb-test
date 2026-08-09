<template>
    <DashboardShell
        :nav-items="navItems"
        :brand="{ name: 'Call Monitoring' }"
        title="Call Monitoring System"
    >
        <router-view />
    </DashboardShell>
</template>

<script setup>
    import DashboardShell from '@/components/layout/DashboardShell.vue';
    import { HomeIcon, MapIcon, Cog6ToothIcon } from '@heroicons/vue/24/outline';

    const navItems = [{ label: 'Overview', to: '/monitoring', icon: HomeIcon }];
</script>
