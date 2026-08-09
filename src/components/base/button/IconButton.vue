<template>
    <component
        :is="tag"
        :type="tag === 'button' ? type : undefined"
        :disabled="tag === 'button' ? disabled || loading : undefined"
        :aria-label="ariaLabel"
        :aria-disabled="disabled || loading ? 'true' : undefined"
        :title="title || ariaLabel"
        :class="classes"
        @click="onClick"
    >
        <span
            v-if="loading"
            class="loading loading-spinner"
            :class="spinnerSize"
        ></span>
        <slot v-else />
    </component>
</template>

<script setup>
    import { computed } from 'vue';

    const props = defineProps({
        tag: {
            type: String,
            default: 'button',
        },
        type: {
            type: String,
            default: 'button',
        },
        ariaLabel: {
            type: String,
            required: true,
        },
        title: {
            type: String,
            default: '',
        },

        variant: {
            type: String,
            default: '',
        },

        outline: { type: Boolean, default: false },
        soft: { type: Boolean, default: false },
        ghost: { type: Boolean, default: true },

        size: {
            type: String,
            default: 'md', // 'xs' | 'sm' | 'md' | 'lg' | 'xl'
        },

        square: { type: Boolean, default: false },
        circle: { type: Boolean, default: true },

        active: { type: Boolean, default: false },
        disabled: { type: Boolean, default: false },
        loading: { type: Boolean, default: false },
    });

    const emit = defineEmits(['click']);

    const spinnerSize = computed(() => {
        const map = {
            xs: 'loading-xs',
            sm: 'loading-sm',
            md: 'loading-sm',
            lg: 'loading-md',
            xl: 'loading-lg',
        };
        return map[props.size] || 'loading-sm';
    });

    const classes = computed(() => [
        'btn',
        props.variant && `btn-${props.variant}`,
        props.outline && 'btn-outline',
        props.soft && 'btn-soft',
        props.ghost && 'btn-ghost',
        props.size !== 'md' && `btn-${props.size}`,
        props.square && 'btn-square',
        props.circle && 'btn-circle',
        props.active && 'btn-active',
        props.disabled && 'btn-disabled',
    ]);

    function onClick(event) {
        if (props.disabled || props.loading) return;
        emit('click', event);
    }
</script>
