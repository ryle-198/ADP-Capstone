import api from './api'

export default {
  getAll() {
    return api.get('/shipment/getAll')
  },
  getById(id) {
    return api.get(`/shipment/read/${id}`)
  },
  create(shipment) {
    return api.post('/shipment/create', shipment)
  },
  update(shipment) {
    return api.put('/shipment/update', shipment)
  },
  delete(id) {
    return api.delete(`/shipment/delete/${id}`)
  },
}