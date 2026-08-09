<template>
    <div class="flex flex-col sm:flex-row items-center justify-between gap-3 pt-2">
        <Typography
            tag="p"
            variant="text-xs"
            color="text-base-content/50"
        >
            Showing page {{ page + 1 }} of {{ totalPages || 1 }} · {{ totalItems }} total items
        </Typography>

        <div class="flex flex-col sm:flex-row items-center gap-3">
            <label class="flex items-center gap-2">
                <Typography
                    tag="span"
                    variant="text-xs"
                    color="text-base-content/50"
                >
                    Show
                </Typography>
                <select
                    class="select select-bordered select-sm rounded-lg"
                    :value="pageSize"
                    @change="onPageSizeChange"
                >
                    <option
                        v-for="option in pageSizeOptions"
                        :key="option"
                        :value="option"
                    >
                        {{ option }}
                    </option>
                </select>
            </label>

            <div class="join flex gap-1">
                <Button
                    class="join-item rounded-lg"
                    size="sm"
                    square
                    aria-label="Previous page"
                    :disabled="page <= 0"
                    @click="$emit('change', page - 1)"
                >
                    <ChevronLeftIcon class="w-4 h-4" />
                </Button>

                <template
                    v-for="(item, index) in pageItems"
                    :key="`${item.type}-${item.value ?? index}`"
                >
                    <Button
                        v-if="item.type === 'page'"
                        class="join-item rounded-lg"
                        size="sm"
                        :variant="item.value === page ? 'primary' : ''"
                        @click="$emit('change', item.value)"
                    >
                        {{ item.value + 1 }}
                    </Button>
                    <Button
                        v-else
                        class="join-item pointer-events-none rounded-lg"
                        size="sm"
                        disabled
                        tabindex="-1"
                        aria-hidden="true"
                        soft
                        variant="primary"
                    >
                        …
                    </Button>
                </template>

                <Button
                    class="join-item rounded-lg"
                    size="sm"
                    square
                    aria-label="Next page"
                    :disabled="page >= totalPages - 1"
                    @click="$emit('change', page + 1)"
                >
                    <ChevronRightIcon class="w-4 h-4" />
                </Button>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { computed } from 'vue';
    import Typography from '@/components/base/typography/Typography.vue';
    import Button from '@/components/base/button/Button.vue';
    import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline';

    const props = defineProps({
        page: {
            type: Number,
            required: true,
        },
        totalPages: {
            type: Number,
            required: true,
        },
        totalItems: {
            type: Number,
            default: 0,
        },
        pageSize: {
            type: Number,
            default: 10,
        },
        pageSizeOptions: {
            type: Array,
            default: () => [5, 10, 20, 50],
        },
    });

    const emit = defineEmits(['change', 'update:pageSize']);

    /**
     * Pattern: < 1 … current, current+1, current+2 … last >
     * Example: < 1 … 5 6 7 … 20 >
     */
    const pageItems = computed(() => {
        const total = Math.max(1, props.totalPages || 1);
        const current = Math.min(Math.max(0, props.page), total - 1);
        const last = total - 1;

        const middle = [current, current + 1, current + 2].filter((p) => p >= 0 && p <= last);

        const pages = new Set([0, ...middle, last]);
        const sorted = [...pages].sort((a, b) => a - b);

        const items = [];
        sorted.forEach((p, index) => {
            if (index > 0 && p - sorted[index - 1] > 1) {
                items.push({ type: 'ellipsis' });
            }
            items.push({ type: 'page', value: p });
        });

        return items;
    });

    function onPageSizeChange(event) {
        emit('update:pageSize', Number(event.target.value));
    }
</script>
