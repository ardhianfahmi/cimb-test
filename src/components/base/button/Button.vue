<template>
    <component
        :is="tag"
        :type="tag === 'button' ? type : undefined"
        :disabled="tag === 'button' ? disabled || loading : undefined"
        :aria-disabled="disabled || loading ? 'true' : undefined"
        :class="classes"
    >
        <span
            v-if="loading"
            class="loading loading-spinner"
            :class="spinnerSize"
        ></span>
        <slot />
    </component>
</template>

<script setup>
    import { computed } from 'vue';

    const props = defineProps({
        tag: {
            type: String,
            default: 'button', // 'button' | 'a' | 'label' | 'div' | etc
        },
        type: {
            type: String,
            default: 'button', // 'button' | 'submit' | 'reset'
        },

        variant: {
            type: String,
            default: '', // '' | 'neutral' | 'primary' | 'secondary' | 'accent' | 'info' | 'success' | 'warning' | 'error'
        },

        outline: { type: Boolean, default: false },
        dash: { type: Boolean, default: false },
        soft: { type: Boolean, default: false },
        ghost: { type: Boolean, default: false },
        link: { type: Boolean, default: false },

        // SECTION SIZE
        size: {
            type: String,
            default: 'md', // 'xs' | 'sm' | 'md' | 'lg' | 'xl'
        },

        wide: { type: Boolean, default: false },
        block: { type: Boolean, default: false },
        square: { type: Boolean, default: false },
        circle: { type: Boolean, default: false },

        active: { type: Boolean, default: false },
        disabled: { type: Boolean, default: false },
        loading: { type: Boolean, default: false },
    });

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
        props.dash && 'btn-dash',
        props.soft && 'btn-soft',
        props.ghost && 'btn-ghost',
        props.link && 'btn-link',
        props.size !== 'md' && `btn-${props.size}`,
        props.wide && 'btn-wide',
        props.block && 'btn-block',
        props.square && 'btn-square',
        props.circle && 'btn-circle',
        props.active && 'btn-active',
        props.disabled && 'btn-disabled',
    ]);
</script>
