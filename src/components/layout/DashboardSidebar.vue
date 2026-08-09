<template>
    <aside class="min-h-full w-72 bg-base-100 border-r border-base-300 flex flex-col">
        <div class="h-16 flex items-center gap-3 px-5 border-b border-base-300 shrink-0">
            <img
                v-if="brand.logo"
                :src="brand.logo"
                :alt="brand.name"
                class="w-8 h-8 rounded-lg object-cover"
            />
            <div
                v-else
                class="w-8 h-8 rounded-lg bg-primary text-primary-content flex items-center justify-center font-bold text-sm shrink-0"
            >
                {{ brand.name?.charAt(0) }}
            </div>
            <Typography
                tag="span"
                variant="text-base"
                weight="font-bold"
                class="truncate"
            >
                {{ brand.name }}
            </Typography>
        </div>

        <nav class="flex-1 overflow-y-auto py-3 px-3">
            <ul class="menu w-full gap-1 p-0">
                <li
                    v-for="item in navItems"
                    :key="item.label"
                >
                    <details
                        v-if="item.children?.length"
                        :open="isParentActive(item)"
                    >
                        <summary class="rounded-lg">
                            <component
                                :is="item.icon"
                                v-if="item.icon"
                                class="w-5 h-5 shrink-0"
                            />
                            <span class="truncate">{{ item.label }}</span>
                        </summary>
                        <ul>
                            <li
                                v-for="child in item.children"
                                :key="child.label"
                            >
                                <RouterLink
                                    :to="child.to"
                                    class="rounded-lg"
                                    active-class="menu-active"
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
                        class="rounded-lg"
                        active-class="menu-active"
                        @click="$emit('navigate')"
                    >
                        <component
                            :is="item.icon"
                            v-if="item.icon"
                            class="w-5 h-5 shrink-0"
                        />
                        <span class="truncate">{{ item.label }}</span>
                    </RouterLink>
                </li>
            </ul>
        </nav>
    </aside>
</template>

<script setup>
    import { useRoute } from 'vue-router';
    import Typography from '@/components/base/typography/Typography.vue';

    const props = defineProps({
        brand: {
            type: Object,
            default: () => ({ name: 'Dashboard', logo: null }),
        },
        navItems: {
            type: Array,
            required: true,
        },
    });

    defineEmits(['navigate']);

    const route = useRoute();

    function isParentActive(item) {
        return item.children?.some((child) => route.path.startsWith(child.to));
    }
</script>
