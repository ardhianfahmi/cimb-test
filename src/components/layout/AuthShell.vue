<template>
    <div class="min-h-screen bg-base-100 p-3 sm:p-4 lg:p-6">
        <div class="min-h-[calc(100vh-1.5rem)] sm:min-h-[calc(100vh-2rem)] lg:min-h-[calc(100vh-3rem)] grid lg:grid-cols-2 gap-4 lg:gap-6">
            <aside
                class="relative overflow-hidden rounded-3xl bg-primary text-primary-content p-6 sm:p-8 lg:p-10 hidden lg:flex flex-col justify-between lg:min-h-full"
            >
                <div
                    class="pointer-events-none absolute -bottom-24 -left-24 w-72 h-72 rounded-full border border-white/20"
                ></div>
                <div
                    class="pointer-events-none absolute -bottom-10 -left-10 w-48 h-48 rounded-full border border-white/15"
                ></div>
                <div
                    class="pointer-events-none absolute bottom-16 left-16 w-24 h-24 rounded-full border border-white/10"
                ></div>

                <div class="relative z-10 space-y-2 max-w-md">
                    <Typography
                        tag="h1"
                        variant="text-2xl sm:text-3xl"
                        weight="font-bold"
                        color="text-primary-content"
                    >
                        {{ title }}
                    </Typography>
                    <Typography
                        tag="p"
                        variant="text-sm sm:text-base"
                        color="text-primary-content/80"
                    >
                        {{ subtitle }}
                    </Typography>
                </div>

                <div class="relative z-10 mt-8 lg:mt-0">
                    <div class="bg-base-100 text-base-content rounded-2xl p-5 sm:p-6 shadow-lg">
                        <div class="flex items-start justify-between gap-4">
                            <div class="space-y-2 max-w-sm">
                                <Typography
                                    tag="h2"
                                    variant="text-lg sm:text-xl"
                                    weight="font-bold"
                                >
                                    {{ welcomeTitle }}
                                </Typography>
                                <Typography
                                    tag="p"
                                    variant="text-sm"
                                    color="text-base-content/60"
                                >
                                    {{ welcomeDescription }}
                                </Typography>
                            </div>

                            <div
                                class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center shrink-0"
                            >
                                <PhoneIcon class="w-5 h-5 text-primary" />
                            </div>
                        </div>

                        <div class="mt-5 flex items-center justify-end">
                            <div class="flex -space-x-2">
                                <div
                                    v-for="item in avatars"
                                    :key="item"
                                    class="w-8 h-8 rounded-full bg-primary text-primary-content text-xs font-semibold border-2 border-base-100 flex items-center justify-center"
                                >
                                    {{ item }}
                                </div>
                                <div
                                    class="w-8 h-8 rounded-full bg-neutral text-neutral-content text-[10px] font-semibold border-2 border-base-100 flex items-center justify-center"
                                >
                                    {{ avatarExtra }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </aside>

            <!-- FORM AREA -->
            <section class="flex items-center justify-center px-2 sm:px-6 lg:px-10 py-6">
                <div class="w-full max-w-md">
                    <div class="flex items-center gap-2 mb-8">
                        <div class="w-9 h-9 rounded-full bg-primary/10 flex items-center justify-center">
                            <PhoneIcon class="w-5 h-5 text-primary" />
                        </div>
                        <Typography
                            tag="span"
                            variant="text-lg"
                            weight="font-bold"
                        >
                            {{ brandName }}
                        </Typography>
                    </div>

                    <slot />
                </div>
            </section>
        </div>
    </div>
</template>

<script setup>
    import { PhoneIcon } from '@heroicons/vue/24/outline';
    import Typography from '@/components/base/typography/Typography.vue';

    defineProps({
        brandName: {
            type: String,
            default: 'Call Monitoring',
        },
        title: {
            type: String,
            default: 'Call Monitoring System',
        },
        subtitle: {
            type: String,
            default: 'Monitor customer call sentiment and identify calls that need attention',
        },
        welcomeTitle: {
            type: String,
            default: 'Welcome to your monitoring space',
        },
        welcomeDescription: {
            type: String,
            default: 'Access call records, filter by sentiment, and track supervisor insights.',
        },
        avatars: {
            type: Array,
            default: () => ['AP', 'SR', 'BH', 'RM'],
        },
        avatarExtra: {
            type: String,
            default: '+20',
        },
    });
</script>
