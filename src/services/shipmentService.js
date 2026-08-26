import api from './api'

export default {
  getAll() {
    return api.get('/shipments/getAll')
  },
  getById(id) {
    return api.get(`/shipments/read/${id}`)
  },
  create(shipment) {
    return api.post('/shipments/create', shipment)
  },
  update(shipment) {
    return api.put('/shipments/update', shipment)
  },
  delete(id) {
    return api.delete(`/shipments/delete/${id}`)
  },
}