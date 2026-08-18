import api from './api'
export default {
    getAll() {
        return api.get('/driver/getAll')
    },
    create(driver) {
        return api.post('/driver/create', driver)
    },
    read(id) {
        return api.get(`/driver/read/${id}`)
    },
    update(driver) {
        return api.put('/driver/update', driver)
    },
    delete(id) {
        return api.delete(`/driver/delete/${id}`)
    }
}