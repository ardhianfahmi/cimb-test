<template>
    <div class="flex flex-col items-center justify-center gap-3 py-16 px-4 text-center">
        <div class="w-14 h-14 rounded-2xl bg-error/10 text-error flex items-center justify-center">
            <ExclamationTriangleIcon class="w-7 h-7" />
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
                class="max-w-sm"
            >
                {{ description }}
            </Typography>
        </div>
        <Button
            variant="primary"
            size="sm"
            outline
            @click="$emit('retry')"
        >
            Try again
        </Button>
    </div>
</template>

<script setup>
    import Typography from '@/components/base/typography/Typography.vue';
    import Button from '@/components/base/button/Button.vue';
    import { ExclamationTriangleIcon } from '@heroicons/vue/24/outline';

    defineProps({
        title: {
            type: String,
            default: 'Something went wrong',
        },
        description: {
            type: String,
            default: '',
        },
    });

    defineEmits(['retry']);
</script>
