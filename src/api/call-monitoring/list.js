import api from '@/lib/axios';

export async function getCallRecords(params = {}) {
    const { search, sentiment, page = 0, size = 10, sortBy, sortOrder } = params;
    const query = Object.fromEntries(
        Object.entries({
            search,
            sentiment,
            page,
            size,
            sort_by: sortBy,
            sort_order: sortOrder,
        }).filter(([, value]) => value !== undefined && value !== null && value !== '')
    );

    try {
        const response = await api.get('/api/call-monitoring', { params: query });
        return response.data;
    } catch (error) {
        const message = error?.response?.data?.message || error?.message || 'Failed to fetch call records';

        console.error('getCallRecords error:', error);
        throw new Error(message);
    }
}
