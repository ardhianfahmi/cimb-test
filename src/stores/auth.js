import { defineStore } from 'pinia'
import { login as loginRequest } from '@/api/auth/login'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        form: {
            username: '',
            password: '',
            isLoading: false,
        },
        userData: {
            token: localStorage.getItem('token') || '',
            userName: localStorage.getItem('userName') || '',
            fullName: localStorage.getItem('fullName') || '',
            role: localStorage.getItem('role') || '',
        },
    }),

    getters: {
        isLoggedIn: (state) => state.userData.token !== '',
    },

    actions: {
        async login() {
            try {
                const response = await loginRequest(this.form.username, this.form.password)
                const result = response.data

                if (result.status !== 200 || !result.data) {
                    throw new Error(result.message || 'Login failed')
                }

                this.userData.token = result.data.token
                this.userData.userName = result.data.username
                this.userData.fullName = result.data.fullName
                this.userData.role = result.data.role

                localStorage.setItem('token', this.userData.token)
                localStorage.setItem('userName', this.userData.userName)
                localStorage.setItem('fullName', this.userData.fullName)
                localStorage.setItem('role', this.userData.role)

                this.form.password = ''
            } catch (error) {
                const responseMessage =
                    error?.details?.response?.data?.message ||
                    error?.response?.data?.message ||
                    error?.message

                throw new Error(responseMessage || 'Login failed')
            }
        },

        logout() {
            this.form.username = ''
            this.form.password = ''

            this.userData.token = ''
            this.userData.userName = ''
            this.userData.fullName = ''
            this.userData.role = ''

            localStorage.removeItem('token')
            localStorage.removeItem('userName')
            localStorage.removeItem('fullName')
            localStorage.removeItem('role')
            
            localStorage.removeItem('username')
        },
    },
})
