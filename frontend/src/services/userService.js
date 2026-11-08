import api from './api'


export default {
    getAll() {
        return api.get('/user/getAll')
    },

    create(user) {
        return api.post('/user/create', user)
    },

    enable(id) {
        return api.put(`/user/enable/${id}`)
    },

    disable(id) {
        return api.put(`/user/disable/${id}`)
    },

    resetPassword(id, newPassword) {
        return api.put(`/user/resetPassword/${id}`, { newPassword })
    },

    delete(id) {
        return api.delete(`/user/delete/${id}`)
    }
}