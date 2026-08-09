<template>
    <table class="table">
        <thead class="bg-primary text-primary-content">
            <tr>
                <th
                    v-for="n in columns"
                    :key="n"
                >
                    <div class="skeleton h-3 w-16 bg-primary-content/25"></div>
                </th>
            </tr>
        </thead>
        <tbody>
            <tr
                v-for="r in rows"
                :key="r"
            >
                <td
                    v-for="c in columns"
                    :key="c"
                >
                    <div
                        class="skeleton h-4 rounded-md"
                        :style="{ width: cellWidth(c) }"
                    ></div>
                </td>
            </tr>
        </tbody>
    </table>
</template>

<script setup>
    const props = defineProps({
        columns: {
            type: Number,
            default: 4,
        },
        rows: {
            type: Number,
            default: 5,
        },
    });

    function cellWidth(col) {
        const widths = ['60%', '85%', '70%', '50%', '90%'];
        return widths[col % widths.length];
    }
</script>
