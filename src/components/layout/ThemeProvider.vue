<template>
    <div
        :data-theme="themeStore.theme"
        class="min-h-screen"
    >
        <slot />
    </div>
</template>

<script setup>
    import { onMounted } from 'vue';
    import { useThemeStore } from '@/stores/theme';

    const themeStore = useThemeStore();

    onMounted(() => {
        themeStore.initTheme();
    });
</script>
