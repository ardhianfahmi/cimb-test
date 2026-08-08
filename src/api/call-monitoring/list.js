import api from '@/lib/axios'

export function getCallRecords(params) {
    return new Promise((resolve, reject) => {
        api
            .get('/api/call-monitoring', { params })
            .then((response) => {
                if (response.status === 200) {
                    resolve(response.data)
                } else {
                    reject({ transport: true, details: response })
                }
            })
            .catch((err) => {
                console.error('getCallRecords error:', err)
                reject({ transport: true, details: err })
            })
    })
}
