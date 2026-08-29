import api from './api'
export default {
    getAll() {
        return api.get('/route/getAllRoute')
    },
    create(route) {
        return api.post('/route/create', route)
    },
    read(id) {
        return api.get(`/route/read/${id}`)
    },
    update(route) {
      return api.put('/route/update', route)
    },
    delete(id) {
        return api.delete(`/route/delete/${id}`)
    }
}