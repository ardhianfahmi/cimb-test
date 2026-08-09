<template>
    <dialog
        ref="dialogRef"
        class="modal modal-bottom sm:modal-middle"
        @close="handleClose"
    >
        <div class="modal-box">
            <form method="dialog">
                <button
                    class="btn btn-sm btn-circle btn-ghost absolute right-3 top-3"
                    aria-label="Close"
                >
                    ✕
                </button>
            </form>

            <Typography
                v-if="title"
                tag="h3"
                variant="text-lg"
                weight="font-bold"
                class="mb-4 pr-8"
            >
                {{ title }}
            </Typography>

            <slot />

            <div
                v-if="$slots.footer"
                class="modal-action"
            >
                <slot name="footer" />
            </div>
        </div>

        <form
            method="dialog"
            class="modal-backdrop"
        >
            <button>close</button>
        </form>
    </dialog>
</template>

<script setup>
    import { ref, watch } from 'vue';
    import Typography from '@/components/base/typography/Typography.vue';

    const props = defineProps({
        modelValue: {
            type: Boolean,
            default: false,
        },
        title: {
            type: String,
            default: '',
        },
    });

    const emit = defineEmits(['update:modelValue']);

    const dialogRef = ref(null);

    watch(
        () => props.modelValue,
        (isOpen) => {
            if (isOpen) dialogRef.value?.showModal();
            else dialogRef.value?.close();
        }
    );

    function handleClose() {
        emit('update:modelValue', false);
    }
</script>
