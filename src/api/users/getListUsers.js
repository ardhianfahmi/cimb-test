import api from '@/lib/axios';

export async function getListUsers(params = {}) {
    const { search, page = 0, size = 10, sortBy, sortOrder } = params;
    const query = Object.fromEntries(
        Object.entries({
            search,
            page,
            size,
            sort_by: sortBy,
            sort_order: sortOrder,
        }).filter(([, value]) => value !== undefined && value !== null && value !== ''),
    );

    try {
        const response = await api.get('/api/users', { params: query });
        return response.data;
    } catch (error) {
        const message = error?.response?.data?.message || error?.message || 'Failed to fetch users';

        console.error('getListUsers error:', error);
        throw new Error(message);
    }
}
