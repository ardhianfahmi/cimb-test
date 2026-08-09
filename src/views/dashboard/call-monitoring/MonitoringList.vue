<template>
    <CardBaseContent
        title="Monitoring List"
        subtitle="Monitor customer call sentiment"
        :icon="PhoneIcon"
    >
        <div class="overflow-x-auto rounded-box rounded-xl border border-base-content/5 bg-base-100">
            <table class="table">
                <thead class="bg-primary text-white">
                    <tr>
                        <th>No</th>
                        <th>Name</th>
                        <th>Job</th>
                        <th>Favorite Color</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>1</th>
                        <td>Cy Ganderton</td>
                        <td>Quality Control Specialist</td>
                        <td>Blue</td>
                    </tr>
                    <tr>
                        <th>2</th>
                        <td>Hart Hagerty</td>
                        <td>Desktop Support Technician</td>
                        <td>Purple</td>
                    </tr>
                    <tr>
                        <th>3</th>
                        <td>Brice Swyre</td>
                        <td>Tax Accountant</td>
                        <td>Red</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </CardBaseContent>
</template>

<script setup>
    import { PhoneIcon } from '@heroicons/vue/24/outline';
    import CardBaseContent from '../../../components/base/card/CardBaseContent.vue';
    import { useCallMonitoringStore } from '../../../stores/calll-monitoring.js';
    import { onMounted } from 'vue';
    import { getListCallRecords } from '../../../api/call-monitoring/getListCallRecords.js';

    const store = useCallMonitoringStore();

    const getData = async () => {
        store.table.isLoading = true;
        try {
            const response = await getListCallRecords(store.payloads);
        } catch (error) {
            console.error(error);
        } finally {
            store.table.isLoading = false;
        }
    };

    onMounted(async () => {
        await getData();
    });
</script>
