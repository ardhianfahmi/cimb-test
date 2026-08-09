<template>
    <div class="space-y-2 mb-6">
        <Typography
            tag="h2"
            variant="text-3xl"
            weight="font-bold"
        >
            Create account
        </Typography>
        <Typography
            tag="p"
            variant="text-sm"
            color="text-base-content/60"
        >
            Fill in your details to get started.
        </Typography>
    </div>

    <div
        v-if="errorMessage"
        role="alert"
        class="alert alert-error alert-soft text-sm py-2 px-3 mb-4"
    >
        <ExclamationTriangleIcon class="w-5 h-5 shrink-0" />
        <Typography
            tag="span"
            variant="text-sm"
        >
            {{ errorMessage }}
        </Typography>
    </div>

    <form
        class="flex flex-col gap-4"
        novalidate
        @submit.prevent="onSubmit"
    >
        <FormFieldset
            v-model="auth.form.fullName"
            label="Full Name"
            type="text"
            placeholder="Your full name"
            autocomplete="name"
            required
            :error="fieldErrors.fullName"
            @blur="validateField('fullName')"
        />

        <FormFieldset
            v-model="auth.form.username"
            label="Username"
            type="text"
            placeholder="Choose a username"
            autocomplete="username"
            required
            :error="fieldErrors.username"
            @blur="validateField('username')"
        />

        <FormFieldset
            v-model="auth.form.email"
            label="Email"
            type="email"
            placeholder="you@example.com"
            autocomplete="email"
            required
            :error="fieldErrors.email"
            @blur="validateField('email')"
        />

        <FormFieldset
            v-model="auth.form.phoneNumber"
            label="Phone Number"
            type="tel"
            placeholder="08xxxxxxxxxx"
            autocomplete="tel"
            required
            :error="fieldErrors.phoneNumber"
            @blur="validateField('phoneNumber')"
        />

        <FormFieldset
            v-model="auth.form.password"
            label="Password"
            type="password"
            placeholder="••••••••"
            autocomplete="new-password"
            required
            :reveal="showPassword"
            :error="fieldErrors.password"
            @blur="validateField('password')"
        >
            <template #suffix>
                <button
                    type="button"
                    class="text-base-content/40 hover:text-base-content/70 transition-colors"
                    @click="showPassword = !showPassword"
                >
                    <EyeIcon
                        v-if="!showPassword"
                        class="w-4 h-4"
                    />
                    <EyeSlashIcon
                        v-else
                        class="w-4 h-4"
                    />
                </button>
            </template>
        </FormFieldset>

        <FormFieldset
            v-model="auth.form.confirmPassword"
            label="Confirm Password"
            type="password"
            placeholder="••••••••"
            autocomplete="new-password"
            required
            :reveal="showPassword"
            :error="fieldErrors.confirmPassword"
            @blur="validateField('confirmPassword')"
        />

        <ul
            v-if="passwordHints.length"
            class="text-xs text-base-content/50 -mt-2 space-y-0.5"
        >
            <li
                v-for="hint in passwordHints"
                :key="hint.label"
                :class="hint.valid ? 'text-success' : 'text-base-content/50'"
            >
                {{ hint.valid ? '✓' : '•' }} {{ hint.label }}
            </li>
        </ul>

        <button
            type="submit"
            class="btn btn-primary w-full rounded-xl mt-2"
            :disabled="auth.form.isLoading"
        >
            <span
                v-if="auth.form.isLoading"
                class="loading loading-spinner loading-sm"
            ></span>
            {{ auth.form.isLoading ? 'Creating account...' : 'Create account' }}
        </button>

        <Typography
            tag="p"
            variant="text-sm"
            color="text-base-content/60"
            class="text-center"
        >
            Already have an account?
            <RouterLink
                :to="{ name: 'login' }"
                class="link link-primary font-medium"
            >
                Sign in
            </RouterLink>
        </Typography>
    </form>
</template>

<script setup>
    import { ref, reactive, computed } from 'vue';
    import { useRouter } from 'vue-router';
    import { useAuthStore } from '@/stores/auth';
    import FormFieldset from '@/components/base/form/FormFieldset.vue';
    import Typography from '@/components/base/typography/Typography.vue';
    import { EyeIcon, EyeSlashIcon, ExclamationTriangleIcon } from '@heroicons/vue/24/outline';

    const router = useRouter();
    const auth = useAuthStore();

    const errorMessage = ref('');
    const showPassword = ref(false);

    const fieldErrors = reactive({
        fullName: '',
        username: '',
        email: '',
        phoneNumber: '',
        password: '',
        confirmPassword: '',
    });

    const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const PHONE_REGEX = /^(?:\+62|62|0)8[1-9][0-9]{7,11}$/;
    const USERNAME_REGEX = /^[a-zA-Z0-9_]{4,20}$/;

    const passwordHints = computed(() => {
        const pwd = auth.form.password || '';
        return [
            { label: 'At least 8 characters', valid: pwd.length >= 8 },
            { label: 'One uppercase letter', valid: /[A-Z]/.test(pwd) },
            { label: 'One number', valid: /[0-9]/.test(pwd) },
            { label: 'One special character', valid: /[^A-Za-z0-9]/.test(pwd) },
        ];
    });

    function validateField(field) {
        const form = auth.form;

        switch (field) {
            case 'fullName':
                fieldErrors.fullName = form.fullName?.trim() ? '' : 'Full name is required';
                break;

            case 'username':
                if (!form.username?.trim()) {
                    fieldErrors.username = 'Username is required';
                } else if (!USERNAME_REGEX.test(form.username)) {
                    fieldErrors.username = '4-20 chars, letters/numbers/underscore only';
                } else {
                    fieldErrors.username = '';
                }
                break;

            case 'email':
                if (!form.email?.trim()) {
                    fieldErrors.email = 'Email is required';
                } else if (!EMAIL_REGEX.test(form.email)) {
                    fieldErrors.email = 'Enter a valid email address';
                } else {
                    fieldErrors.email = '';
                }
                break;

            case 'phoneNumber':
                if (!form.phoneNumber?.trim()) {
                    fieldErrors.phoneNumber = 'Phone number is required';
                } else if (!PHONE_REGEX.test(form.phoneNumber.replace(/[\s-]/g, ''))) {
                    fieldErrors.phoneNumber = 'Enter a valid Indonesian phone number';
                } else {
                    fieldErrors.phoneNumber = '';
                }
                break;

            case 'password':
                fieldErrors.password = passwordHints.value.every((h) => h.valid)
                    ? ''
                    : 'Password does not meet requirements';
                if (form.confirmPassword) validateField('confirmPassword');
                break;

            case 'confirmPassword':
                fieldErrors.confirmPassword = form.confirmPassword === form.password ? '' : 'Passwords do not match';
                break;
        }
    }

    function validateAll() {
        ['fullName', 'username', 'email', 'phoneNumber', 'password', 'confirmPassword'].forEach(validateField);
        return Object.values(fieldErrors).every((err) => !err);
    }

    async function onSubmit() {
        errorMessage.value = '';

        if (!validateAll()) {
            errorMessage.value = 'Please fix the errors below';
            return;
        }

        auth.form.isLoading = true;

        try {
            await auth.register();
            router.push({ name: 'login' });
        } catch (error) {
            errorMessage.value = error.message || 'Registration failed';
        } finally {
            auth.form.isLoading = false;
        }
    }
</script>
