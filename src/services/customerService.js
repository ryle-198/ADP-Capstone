import api from './api'

export default {

    getAll() {
        return api.get('/customer/getAll')
    },

    create(customer) {
        return api.post('/customer/create', customer)
    },

    read(id) {
        return api.get(`/customer/read/${id}`)
    },

    update(customer) {
        return api.put('/customer/update', customer)
    },

    delete(id) {
        return api.delete(`/customer/delete/${id}`)
    }

}