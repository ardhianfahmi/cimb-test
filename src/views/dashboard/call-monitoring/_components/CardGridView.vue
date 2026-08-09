<template>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
        <article
            v-for="record in records"
            :key="record.call_id"
            class="rounded-xl border border-base-content/5 bg-base-100 p-4 flex flex-col gap-3 hover:border-primary/20 hover:shadow-sm transition-all"
        >
            <div class="flex items-start justify-between gap-2">
                <div class="min-w-0">
                    <Typography
                        tag="p"
                        variant="text-sm"
                        weight="font-semibold"
                        class="truncate"
                    >
                        {{ record.customer_name }}
                    </Typography>
                    <Typography
                        tag="p"
                        variant="text-xs"
                        color="text-base-content/50"
                        class="mt-0.5"
                    >
                        Handled by {{ record.cs_name }}
                    </Typography>
                </div>
                <span
                    class="badge badge-soft shrink-0"
                    :class="sentimentBadgeClass(record.sentiment_score)"
                >
                    {{ record.sentiment_score }}
                </span>
            </div>

            <div class="divider my-0"></div>

            <div class="flex flex-col gap-1.5 text-xs text-base-content/45">
                <div class="flex items-center justify-between gap-2">
                    <span>Call ID</span>
                    <span class="font-mono text-base-content/70">{{ record.call_id }}</span>
                </div>
                <div class="flex items-center justify-between gap-2">
                    <span>Timestamp</span>
                    <span class="text-base-content/70">{{ formatCallDate(record.call_timestamp) }}</span>
                </div>
                <div class="flex items-center justify-between gap-2">
                    <span>Sentiment</span>
                    <span class="text-base-content/70">
                        {{ sentimentLabel(record.sentiment_score) }}
                    </span>
                </div>
            </div>
        </article>
    </div>
</template>

<script setup>
    import Typography from '@/components/base/typography/Typography.vue';
    import {
        formatCallDate,
        sentimentBadgeClass,
        sentimentLabel,
    } from './callRecordDisplay.js';

    defineProps({
        records: {
            type: Array,
            required: true,
        },
    });
</script>
