import api from './api'

export default {
    getAll(){
        return api.get('/deliveryOrders/getAll')
    },

    getById(id) {
    return api.get(`/deliveryOrders/read/${id}`)
  },
  create(order) {
    return api.post('/deliveryOrders/create', order)
  },
  update(order) {
    return api.put('/deliveryOrders/update', order)
  },
  delete(id) {
    return api.delete(`/deliveryOrders/delete/${id}`)
  },



    getPaged({page = 0, size = 10, sortBy = 'orderDate', direction = 'desc', search = ''}){
        return api.get('deliveryOrders/search',{
            params: { page, size, sortBy, direction, search: search || undefined }
        })
    }
}

