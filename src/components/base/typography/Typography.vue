<template>
    <component
        :is="tag"
        v-bind="attrs"
        :class="computedClasses"
        :style="computedStyle"
    >
        <slot />
    </component>
</template>

<script setup>
    import { computed, useAttrs } from 'vue';

    const props = defineProps({
        tag: { type: String, default: 'p' },

        variant: { type: String, default: '' }, // text-sm, text-lg, prose, etc
        weight: { type: [String, Number], default: '' }, // font-bold | 500
        color: { type: String, default: '' }, // text-primary OR raw color
        align: { type: String, default: '' }, // left | center | right | justify
        transform: { type: String, default: '' }, // uppercase | lowercase | capitalize
        italic: { type: Boolean, default: false },

        truncate: { type: Boolean, default: false },
        lineClamp: { type: [String, Number], default: 0 },

        fontSize: { type: [String, Number], default: '' }, // text-sm OR 14(px)
        letterSpacing: { type: String, default: '' },

        cssVars: { type: Object, default: () => ({}) },
        customClass: { type: [String, Array, Object], default: '' },
    });

    const attrs = useAttrs();

    const computedClasses = computed(() => {
        const cls = [];

        if (props.variant) cls.push(props.variant);

        if (typeof props.fontSize === 'string' && props.fontSize) {
            cls.push(props.fontSize);
        }

        if (props.weight) {
            if (typeof props.weight === 'number') cls.push(`font-[${props.weight}]`);
            else cls.push(props.weight);
        }

        if (props.color) {
            const c = props.color.trim();
            if (c.startsWith('text-')) cls.push(c);
        }

        if (props.align) {
            const map = {
                left: 'text-left',
                center: 'text-center',
                right: 'text-right',
                justify: 'text-justify',
            };
            if (map[props.align]) cls.push(map[props.align]);
        }

        if (props.transform) cls.push(props.transform);

        if (props.italic) cls.push('italic');

        if (props.truncate) cls.push('truncate');

        if (props.lineClamp && Number(props.lineClamp) > 0) {
            cls.push(`line-clamp-${props.lineClamp}`);
        }

        if (props.customClass) cls.push(props.customClass);

        return cls;
    });

    const computedStyle = computed(() => {
        const style = {};

        if (props.color) {
            const c = props.color.trim();
            if (c.startsWith('#') || c.startsWith('rgb') || c.startsWith('hsl') || c.startsWith('var(')) {
                style.color = c;
            }
        }

        if (typeof props.fontSize === 'number' && props.fontSize > 0) {
            style.fontSize = `${props.fontSize}px`;
        }

        if (props.letterSpacing) {
            style.letterSpacing = props.letterSpacing;
        }

        Object.assign(style, props.cssVars);

        return style;
    });
</script>
