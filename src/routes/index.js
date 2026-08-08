import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/auth/Login.vue'
import MonitoringView from '../views/MonitoringView.vue'
import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/',
    name: 'login',
    component: Login,
    meta: { guestOnly: true },
  },
  {
    path: '/monitoring',
    name: 'monitoring',
    component: MonitoringView,
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    return { name: 'login' }
  }

  if (to.meta.guestOnly && auth.isLoggedIn) {
    return { name: 'monitoring' }
  }

  return true
})

export default router
