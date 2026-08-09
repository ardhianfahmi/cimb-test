<template>
    <CardBaseContent
        title="Monitoring List"
        subtitle="Monitor customer call sentiment"
        :icon="PhoneIcon"
    ></CardBaseContent>
</template>

<script setup>
    import { PhoneIcon } from '@heroicons/vue/24/outline';
    import CardBaseContent from '../../../components/base/card/CardBaseContent.vue';
</script>
