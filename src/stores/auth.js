import { defineStore } from 'pinia';
import { login as loginRequest } from '@/api/auth/login';
import { register as registerRequest } from '../api/auth/register';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        loginForm: {
            username: '',
            password: '',
            isLoading: false,
        },
        registerForm: {
            username: '',
            password: '',
            fullName: '',
            email: '',
            phoneNumber: '',
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
            const { username, password } = this.loginForm;
            try {
                const response = await loginRequest(username, password);
                const result = response.data;

                if (result.status !== 200 || !result.data) {
                    throw new Error(result.message || 'Login failed');
                }

                this.userData.token = result.data.token;
                this.userData.userName = result.data.username;
                this.userData.fullName = result.data.fullName;
                this.userData.role = result.data.role;

                localStorage.setItem('token', this.userData.token);
                localStorage.setItem('userName', this.userData.userName);
                localStorage.setItem('fullName', this.userData.fullName);
                localStorage.setItem('role', this.userData.role);

                this.loginForm.password = '';
            } catch (error) {
                const responseMessage =
                    error?.details?.response?.data?.message || error?.response?.data?.message || error?.message;

                throw new Error(responseMessage || 'Login failed');
            }
        },

        async register() {
            const { username, password, fullName, email, phoneNumber } = this.registerForm;
            try {
                const response = await registerRequest(username, password, fullName, email, phoneNumber);
                const result = response.data;

                if (result.status !== 200 || !result.data) {
                    throw new Error(result.message || 'Register failed');
                }

                this.registerForm.username = '';
                this.registerForm.password = '';
                this.registerForm.fullName = '';
                this.registerForm.email = '';
                this.registerForm.phoneNumber = '';
            } catch (error) {
                const responseMessage =
                    error?.details?.response?.data?.message || error?.response?.data?.message || error?.message;

                throw new Error(responseMessage || 'Register failed');
            }
        },

        logout() {
            this.loginForm.username = '';
            this.loginForm.password = '';

            this.userData.token = '';
            this.userData.userName = '';
            this.userData.fullName = '';
            this.userData.role = '';

            localStorage.removeItem('token');
            localStorage.removeItem('userName');
            localStorage.removeItem('fullName');
            localStorage.removeItem('role');

            localStorage.removeItem('username');
        },
    },
});
