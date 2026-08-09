<template>
    <div
        class="card border-0 bg-transparent shadow-none lg:overflow-hidden lg:rounded-2xl lg:border lg:border-base-200/80 lg:bg-base-100 lg:shadow-md lg:shadow-base-300"
    >
        <div class="card-body flex flex-col gap-4 p-0 lg:gap-6 lg:p-6">
            <div class="flex flex-col gap-3 px-1 lg:flex-row lg:items-start lg:justify-between lg:px-0">
                <div class="flex min-w-0 items-center gap-3 lg:items-start lg:gap-4">
                    <div
                        v-if="icon || $slots.icon"
                        class="w-10 h-10 rounded-xl bg-primary/10 text-primary flex items-center justify-center shrink-0 shadow-sm shadow-primary/10"
                    >
                        <slot name="icon">
                            <component
                                :is="icon"
                                class="w-5 h-5"
                            />
                        </slot>
                    </div>

                    <div class="flex min-w-0 flex-col gap-0.5">
                        <Typography
                            tag="h2"
                            variant="text-lg lg:text-xl"
                            weight="font-semibold"
                            color="text-base-content"
                        >
                            {{ title }}
                        </Typography>
                        <Typography
                            v-if="subtitle"
                            tag="p"
                            variant="text-xs sm:text-sm"
                            color="text-base-content/55"
                            class="line-clamp-2 max-w-2xl"
                        >
                            {{ subtitle }}
                        </Typography>
                    </div>
                </div>

                <div
                    v-if="$slots.actions"
                    class="flex flex-wrap gap-2 lg:justify-end"
                >
                    <slot name="actions" />
                </div>
            </div>

            <template v-if="$slots.default">
                <slot />
            </template>
        </div>
    </div>
</template>

<script setup>
    import Typography from '@/components/base/typography/Typography.vue';

    defineProps({
        title: {
            type: String,
            required: true,
        },
        subtitle: {
            type: String,
            default: '',
        },
        icon: {
            type: [Object, Function],
            default: null,
        },
    });
</script>
