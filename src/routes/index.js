import { createRouter, createWebHistory } from 'vue-router';
import Auth from '../views/auth/Auth.vue';
import Login from '../views/auth/Login.vue';
import Register from '../views/auth/Register.vue';
import MonitoringView from '../views/MonitoringView.vue';
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
        path: '/monitoring',
        name: 'monitoring',
        component: MonitoringView,
        meta: { requiresAuth: true },
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
        return { name: 'monitoring' };
    }

    return true;
});

export default router;
