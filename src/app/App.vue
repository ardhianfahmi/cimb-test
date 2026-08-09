<template>
    <ThemeProvider>
        <RouterView />
    </ThemeProvider>
</template>

<script setup>
    import ThemeProvider from '@/components/layout/ThemeProvider.vue';
</script>
