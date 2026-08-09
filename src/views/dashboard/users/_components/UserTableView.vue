<template>
    <div class="overflow-x-auto rounded-xl border border-base-content/5 bg-base-100">
        <table class="table">
            <thead class="bg-primary text-primary-content">
                <tr>
                    <th>No</th>
                    <th>Full Name</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody>
                <tr
                    v-for="(user, index) in users"
                    :key="user.username"
                    class="hover:bg-base-200/50 transition-colors"
                >
                    <th class="text-base-content/60">{{ startIndex + index + 1 }}</th>
                    <td class="font-medium">{{ user.full_name }}</td>
                    <td>{{ user.username }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.phone_number }}</td>
                    <td>
                        <span
                            class="badge badge-soft"
                            :class="roleBadgeClass(user.role)"
                        >
                            {{ user.role }}
                        </span>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
    import { roleBadgeClass } from './userDisplay.js';

    defineProps({
        users: {
            type: Array,
            required: true,
        },
        startIndex: {
            type: Number,
            default: 0,
        },
    });
</script>
