<template>
  <div :data-theme="themeStore.theme" :class="themeStore.theme">
    <slot />
  </div>
</template>

<script setup>
import { watch } from "vue";
import { useThemeStore } from "../../stores/theme";

const themeStore = useThemeStore();

watch(
  () => themeStore.theme,
  (val) => {
    document.documentElement.setAttribute("data-theme", val);
    document.documentElement.classList.toggle("dark", val === "dark");
  },
  { immediate: true },
);
</script>
