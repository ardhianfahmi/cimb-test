<template>
    <DashboardShell
        :nav-items="navItems"
        :brand="{ name: 'Dashboard' }"
        :title="pageTitle"
    >
        <router-view />
    </DashboardShell>
</template>

<script setup>
    import { computed } from 'vue';
    import { useRoute } from 'vue-router';
    import DashboardShell from '@/components/layout/DashboardShell.vue';
    import { PhoneIcon, UsersIcon } from '@heroicons/vue/24/outline';

    const route = useRoute();

    const navItems = [
        { label: 'Call Monitoring', to: { name: 'call-monitoring' }, icon: PhoneIcon },
        { label: 'Users', to: { name: 'users' }, icon: UsersIcon },
    ];

    const pageTitle = computed(() => route.meta.title || '');
</script>
