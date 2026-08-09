import { createRouter, createWebHistory } from 'vue-router';
import Auth from '../views/auth/Auth.vue';
import Login from '../views/auth/Login.vue';
import Register from '../views/auth/Register.vue';
import Dashboard from '../views/dashboard/Dashboard.vue';
import MonitoringList from '../views/dashboard/call-monitoring/MonitoringList.vue';
import UserList from '../views/dashboard/users/UserList.vue';
import { useAuthStore } from '../stores/auth';

const routes = [
    {
        path: '/',
        component: Auth,
        meta: { guestOnly: true },
        children: [
            { path: '', name: 'login', component: Login },
            { path: 'register', name: 'register', component: Register },
        ],
    },
    {
        path: '/dashboard',
        component: Dashboard,
        meta: { requiresAuth: true },
        children: [
            { path: '', redirect: { name: 'call-monitoring' } },
            {
                path: 'call-monitoring',
                name: 'call-monitoring',
                component: MonitoringList,
                meta: { title: 'Call Monitoring' },
            },
            {
                path: 'users',
                name: 'users',
                component: UserList,
                meta: { title: 'Users' },
            },
        ],
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to) => {
    const auth = useAuthStore();

    if (to.meta.requiresAuth && !auth.isLoggedIn) {
        return { name: 'login' };
    }

    if (to.meta.guestOnly && auth.isLoggedIn) {
        return { name: 'call-monitoring' };
    }

    return true;
});

export default router;
