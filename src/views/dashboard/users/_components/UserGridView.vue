<template>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
        <article
            v-for="user in users"
            :key="user.username"
            class="rounded-xl border border-base-content/5 bg-base-100 p-4 flex flex-col gap-3 hover:border-primary/20 hover:shadow-sm transition-all"
        >
            <div class="flex items-start gap-3">
                <div
                    class="w-12 h-12 rounded-2xl bg-primary text-primary-content flex items-center justify-center shrink-0 text-sm font-bold shadow-sm shadow-primary/20"
                >
                    {{ getUserInitials(user.full_name) }}
                </div>

                <div class="min-w-0 flex-1">
                    <div class="flex items-start justify-between gap-2">
                        <Typography
                            tag="p"
                            variant="text-sm"
                            weight="font-semibold"
                            class="truncate"
                        >
                            {{ user.full_name }}
                        </Typography>
                        <span
                            class="badge badge-soft shrink-0"
                            :class="roleBadgeClass(user.role)"
                        >
                            {{ user.role }}
                        </span>
                    </div>
                    <Typography
                        tag="p"
                        variant="text-xs"
                        color="text-base-content/50"
                        class="mt-0.5"
                    >
                        @{{ user.username }}
                    </Typography>
                </div>
            </div>

            <div class="divider my-0"></div>

            <div class="flex flex-col gap-1.5 text-xs text-base-content/45">
                <div class="flex items-center justify-between gap-2">
                    <span>Email</span>
                    <span class="text-base-content/70 truncate max-w-[60%] text-right">
                        {{ user.email }}
                    </span>
                </div>
                <div class="flex items-center justify-between gap-2">
                    <span>Phone</span>
                    <span class="text-base-content/70">{{ user.phone_number }}</span>
                </div>
            </div>
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
