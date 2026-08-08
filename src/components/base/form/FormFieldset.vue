<template>
    <fieldset class="fieldset w-full">
        <legend
            v-if="label"
            class="fieldset-legend text-sm font-semibold px-0"
        >
            {{ label }}<span
                v-if="required"
                class="text-error"
            >*</span>
        </legend>

        <label class="input input-bordered w-full flex items-center gap-2 rounded-xl">
            <slot name="prefix" />

            <input
                class="grow"
                :type="inputType"
                :value="modelValue"
                :placeholder="placeholder"
                :autocomplete="autocomplete"
                :required="required"
                :disabled="disabled"
                @input="$emit('update:modelValue', $event.target.value)"
            />

            <slot name="suffix" />
        </label>

        <Typography
            v-if="hint"
            tag="p"
            variant="text-xs"
            color="text-base-content/50"
            custom-class="mt-1"
        >
            {{ hint }}
        </Typography>
    </fieldset>
</template>

<script setup>
    import { computed } from 'vue';
    import Typography from '@/components/base/typography/Typography.vue';

    const props = defineProps({
        modelValue: {
            type: [String, Number],
            default: '',
        },
        label: {
            type: String,
            default: '',
        },
        type: {
            type: String,
            default: 'text',
        },
        placeholder: {
            type: String,
            default: '',
        },
        autocomplete: {
            type: String,
            default: '',
        },
        required: {
            type: Boolean,
            default: false,
        },
        disabled: {
            type: Boolean,
            default: false,
        },
        hint: {
            type: String,
            default: '',
        },
        reveal: {
            type: Boolean,
            default: false,
        },
    });

    defineEmits(['update:modelValue']);

    const inputType = computed(() => {
        if (props.type === 'password' && props.reveal) {
            return 'text';
        }
        return props.type;
    });
</script>
