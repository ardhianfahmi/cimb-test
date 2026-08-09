<template>
    <div class="flex flex-col gap-3">
        <article
            v-for="(record, index) in records"
            :key="record.call_id"
            class="rounded-xl border border-base-content/5 bg-base-100 p-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between hover:border-primary/20 hover:shadow-sm transition-all"
        >
            <div class="flex items-start gap-3 min-w-0">
                <div
                    class="w-9 h-9 rounded-xl bg-primary/10 text-primary flex items-center justify-center shrink-0 text-xs font-semibold"
                >
                    {{ startIndex + index + 1 }}
                </div>

                <div class="min-w-0 flex flex-col gap-1">
                    <div class="flex flex-wrap items-center gap-2">
                        <Typography
                            tag="p"
                            variant="text-sm"
                            weight="font-semibold"
                            class="truncate"
                        >
                            {{ record.customer_name }}
                        </Typography>
                        <span class="font-mono text-[11px] text-base-content/40">
                            {{ record.call_id }}
                        </span>
                    </div>

                    <Typography
                        tag="p"
                        variant="text-xs"
                        color="text-base-content/50"
                    >
                        Handled by {{ record.cs_name }}
                    </Typography>

                    <Typography
                        tag="p"
                        variant="text-xs"
                        color="text-base-content/40"
                    >
                        {{ formatCallDate(record.call_timestamp) }}
                    </Typography>
                </div>
            </div>

            <div class="flex items-center gap-2 sm:flex-col sm:items-end sm:gap-1 shrink-0">
                <span
                    class="badge badge-soft"
                    :class="sentimentBadgeClass(record.sentiment_score)"
                >
                    {{ record.sentiment_score }}
                </span>
                <Typography
                    tag="span"
                    variant="text-[11px]"
                    color="text-base-content/40"
                >
                    {{ sentimentLabel(record.sentiment_score) }}
                </Typography>
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
        startIndex: {
            type: Number,
            default: 0,
        },
    });
</script>
