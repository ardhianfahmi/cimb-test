import api from '@/lib/axios'

export function register(username, password, fullName, email, phoneNumber) {
    return new Promise((resolve, reject) => {
        api
            .post('/api/auth/register', {
                username: username,
                password: password,
                full_name: fullName,
                email: email,
                phone_number: phoneNumber,
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
