import api from '@/lib/axios';

export async function getListUsers() {
    try {
        const response = await api.get('/api/users');
        return response.data;
    } catch (error) {
        const message = error?.response?.data?.message || error?.message || 'Failed to fetch users';

        console.error('getListUsers error:', error);
        throw new Error(message);
    }
}
