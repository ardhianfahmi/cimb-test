import api from '@/lib/axios'

export function login(username, password) {
    return new Promise((resolve, reject) => {
        api
            .post('/api/auth/login', {
                username: username,
                password: password,
            })
            .then((response) => {
                if (response.status === 200) {
                    resolve(response)
                } else {
                    reject({ transport: true, details: response })
                }
            })
            .catch((err) => {
                console.error('login error:', err)
                reject({ transport: true, details: err })
            })
    })
}
