<template>
    <aside class="min-h-full w-72 bg-base-100 flex flex-col border border-r border-base-300 shadow-md p-3">
        <div class="h-16 flex items-center gap-3 px-5 shrink-0 bg-primary rounded-xl">
            <img
                v-if="brand.logo"
                :src="brand.logo"
                :alt="brand.name"
                class="w-9 h-9 rounded-xl object-cover shadow-sm"
            />
            <div
                v-else
                class="w-9 h-9 rounded-xl bg-base-100 text-primary flex items-center justify-center font-bold text-sm shrink-0 shadow-sm shadow-primary/30"
            >
                {{ brand.name?.charAt(0) }}
            </div>
            <div class="flex flex-col leading-tight truncate">
                <Typography
                    tag="span"
                    variant="text-base text-white"
                    weight="font-bold"
                    class="truncate"
                >
                    {{ brand.name }}
                </Typography>
                <Typography
                    v-if="brand.subtitle"
                    tag="span"
                    variant="text-xs"
                    color="text-base-content/50"
                    class="truncate"
                >
                    {{ brand.subtitle }}
                </Typography>
            </div>
        </div>

        <nav class="sidebar-scroll flex-1 overflow-y-auto py-4 px-3">
            <ul class="menu w-full gap-2 p-0">
                <li
                    v-for="item in navItems"
                    :key="item.label"
                >
                    <details
                        v-if="item.children?.length"
                        :open="isParentActive(item)"
                    >
                        <summary
                            class="rounded-xl font-medium text-base-content/70 hover:bg-base-200 transition-colors"
                        >
                            <component
                                :is="item.icon"
                                v-if="item.icon"
                                class="w-5 h-5 shrink-0"
                            />
                            <span class="truncate">{{ item.label }}</span>
                        </summary>
                        <ul class="ml-3 border-l border-base-300 pl-2">
                            <li
                                v-for="child in item.children"
                                :key="child.label"
                            >
                                <RouterLink
                                    :to="child.to"
                                    class="rounded-lg text-sm relative"
                                    :class="
                                        isActive(child.to)
                                            ? 'bg-primary/10 text-primary font-semibold'
                                            : 'text-base-content/60 hover:bg-base-200'
                                    "
                                    @click="$emit('navigate')"
                                >
                                    {{ child.label }}
                                </RouterLink>
                            </li>
                        </ul>
                    </details>

                    <RouterLink
                        v-else
                        :to="item.to"
                        class="rounded-xl relative font-medium transition-colors p-3"
                        :class="
                            isActive(item.to) ? 'bg-primary/10 text-primary' : 'text-base-content/70 hover:bg-base-200'
                        "
                        @click="$emit('navigate')"
                    >
                        <span
                            v-if="isActive(item.to)"
                            class="absolute left-0 top-1/2 -translate-y-1/2 h-5 w-1 rounded-r-full bg-primary"
                        ></span>
                        <component
                            :is="item.icon"
                            v-if="item.icon"
                            class="w-5 h-5 shrink-0"
                            :class="isActive(item.to) ? 'text-primary' : ''"
                        />
                        <span class="truncate">{{ item.label }}</span>
                    </RouterLink>
                </li>
            </ul>
        </nav>

        <div
            v-if="$slots.footer"
            class="px-3 py-3 border-t border-base-300 shrink-0"
        >
            <slot name="footer" />
        </div>
    </aside>
</template>

<script setup>
    import { useRoute } from 'vue-router';
    import Typography from '@/components/base/typography/Typography.vue';

    const props = defineProps({
        brand: {
            type: Object,
            default: () => ({ name: 'Dashboard', logo: null, subtitle: '' }),
        },
        navItems: {
            type: Array,
            required: true,
        },
    });

    defineEmits(['navigate']);

    const route = useRoute();

    function resolveName(to) {
        if (typeof to === 'string') return null;
        return to?.name ?? null;
    }

    function resolvePath(to) {
        if (typeof to === 'string') return to;
        return null;
    }

    function isActive(to) {
        const name = resolveName(to);
        if (name) return route.name === name;

        const path = resolvePath(to);
        if (path) return route.path === path || route.path.startsWith(`${path}/`);

        return false;
    }

    function isParentActive(item) {
        return item.children?.some((child) => isActive(child.to));
    }
</script>

<style scoped>
    .sidebar-scroll::-webkit-scrollbar {
        width: 6px;
    }

    .sidebar-scroll::-webkit-scrollbar-track {
        background: transparent;
    }

    .sidebar-scroll::-webkit-scrollbar-thumb {
        background-color: color-mix(in oklch, var(--color-base-content) 15%, transparent);
        border-radius: 999px;
    }

    .sidebar-scroll::-webkit-scrollbar-thumb:hover {
        background-color: color-mix(in oklch, var(--color-base-content) 25%, transparent);
    }
</style>
