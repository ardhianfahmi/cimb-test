<template>
    <div class="overflow-x-auto rounded-xl border border-base-content/5 bg-base-100">
        <table class="table">
            <thead class="bg-primary text-primary-content">
                <tr>
                    <th>No</th>
                    <th>Call ID</th>
                    <th>Timestamp</th>
                    <th>CS Name</th>
                    <th>Customer</th>
                    <th>Sentiment</th>
                </tr>
            </thead>
            <tbody>
                <tr
                    v-for="(record, index) in records"
                    :key="record.call_id"
                    class="hover:bg-base-200/50 transition-colors"
                >
                    <th class="text-base-content/60">{{ startIndex + index + 1 }}</th>
                    <td class="font-mono text-xs">{{ record.call_id }}</td>
                    <td>{{ formatCallDate(record.call_timestamp) }}</td>
                    <td>{{ record.cs_name }}</td>
                    <td>{{ record.customer_name }}</td>
                    <td>
                        <span
                            class="badge badge-soft"
                            :class="sentimentBadgeClass(record.sentiment_score)"
                        >
                            {{ record.sentiment_score }}
                        </span>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
    import { formatCallDate, sentimentBadgeClass } from './callRecordDisplay.js';

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
