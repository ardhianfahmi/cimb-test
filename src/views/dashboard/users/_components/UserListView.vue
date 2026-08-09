<template>
    <div class="flex flex-col gap-3">
        <article
            v-for="user in users"
            :key="user.username"
            class="rounded-xl border border-base-content/5 bg-base-100 p-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between hover:border-primary/20 hover:shadow-sm transition-all"
        >
            <div class="flex items-start gap-3 min-w-0">
                <div
                    class="w-10 h-10 rounded-xl bg-primary/10 text-primary flex items-center justify-center shrink-0 text-xs font-semibold"
                >
                    {{ getUserInitials(user.full_name) }}
                </div>

                <div class="min-w-0 flex flex-col gap-1">
                    <Typography
                        tag="p"
                        variant="text-sm"
                        weight="font-semibold"
                        class="truncate"
                    >
                        {{ user.full_name }}
                    </Typography>
                    <Typography
                        tag="p"
                        variant="text-xs"
                        color="text-base-content/50"
                    >
                        @{{ user.username }}
                    </Typography>
                    <Typography
                        tag="p"
                        variant="text-xs"
                        color="text-base-content/40"
                        class="truncate"
                    >
                        {{ user.email }} · {{ user.phone_number }}
                    </Typography>
                </div>
            </div>

            <span
                class="badge badge-soft shrink-0 self-start sm:self-center"
                :class="roleBadgeClass(user.role)"
            >
                {{ user.role }}
            </span>
        </article>
    </div>
</template>

<script setup>
    import Typography from '@/components/base/typography/Typography.vue';
    import { getUserInitials, roleBadgeClass } from './userDisplay.js';

    defineProps({
        users: {
            type: Array,
            required: true,
        },
    });
</script>
