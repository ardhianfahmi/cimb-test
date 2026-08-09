<template>
    <div class="space-y-2 mb-6">
        <Typography
            tag="h2"
            variant="text-3xl"
            weight="font-bold"
        >
            Sign in
        </Typography>
        <Typography
            tag="p"
            variant="text-sm"
            color="text-base-content/60"
        >
            Please enter your account details to sign in.
        </Typography>
    </div>

    <div class="flex items-center gap-3 mb-6">
        <div class="flex-1 h-px bg-base-300"></div>
        <Typography
            tag="span"
            variant="text-xs"
            weight="font-medium"
            color="text-base-content/50"
        >
            Continue with Username
        </Typography>
        <div class="flex-1 h-px bg-base-300"></div>
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
        @submit.prevent="onSubmit"
    >
        <FormFieldset
            v-model="auth.loginForm.username"
            label="Username"
            type="text"
            placeholder="Input your username"
            autocomplete="username"
            required
        />

        <FormFieldset
            v-model="auth.loginForm.password"
            label="Password"
            type="password"
            placeholder="••••••••"
            autocomplete="current-password"
            required
            :reveal="showPassword"
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

        <Button
            type="submit"
            variant="primary"
            block
            class="rounded-xl mt-2"
            :loading="auth.loginForm.isLoading"
        >
            {{ auth.loginForm.isLoading ? 'Signing in...' : 'Sign in to your account' }}
        </Button>

        <Typography
            tag="p"
            variant="text-sm"
            color="text-base-content/60"
            class="text-center"
        >
            Don't have an account?
            <RouterLink
                :to="{ name: 'register' }"
                class="link link-primary font-medium"
            >
                Register here
            </RouterLink>
        </Typography>
    </form>
</template>

<script setup>
    import { ref } from 'vue';
    import { useRouter } from 'vue-router';
    import { useAuthStore } from '@/stores/auth';
    import FormFieldset from '@/components/base/form/FormFieldset.vue';
    import Typography from '@/components/base/typography/Typography.vue';
    import { EyeIcon, EyeSlashIcon, ExclamationTriangleIcon } from '@heroicons/vue/24/outline';
    import Button from '@/components/base/button/Button.vue';

    const router = useRouter();
    const auth = useAuthStore();

    const errorMessage = ref('');
    const showPassword = ref(false);

    async function onSubmit() {
        errorMessage.value = '';
        auth.loginForm.isLoading = true;

        try {
            await auth.login();
            router.push('/monitoring');
        } catch (error) {
            errorMessage.value = error.message || 'Login failed';
        } finally {
            auth.loginForm.isLoading = false;
        }
    }
</script>
