<template>
    <div class="flex flex-col items-center justify-center gap-3 py-16 px-4 text-center">
        <div class="w-14 h-14 rounded-2xl bg-base-200 text-base-content/40 flex items-center justify-center">
            <component
                :is="icon"
                class="w-7 h-7"
            />
        </div>
        <div class="flex flex-col gap-1">
            <Typography
                tag="p"
                variant="text-sm"
                weight="font-semibold"
                color="text-base-content/70"
            >
                {{ title }}
            </Typography>
            <Typography
                v-if="description"
                tag="p"
                variant="text-xs"
                color="text-base-content/45"
            >
                {{ description }}
            </Typography>
        </div>
        <slot name="action" />
    </div>
</template>

<script setup>
    import Typography from '@/components/base/typography/Typography.vue';
    import { InboxIcon } from '@heroicons/vue/24/outline';

    defineProps({
        icon: {
            type: [Object, Function],
            default: () => InboxIcon,
        },
        title: {
            type: String,
            default: 'No data found',
        },
        description: {
            type: String,
            default: '',
        },
    });
</script>
