import api from './api'

export default {
    getAll(){
        return api.get('/deliveryOrders/getAll')
    },

    getPaged({page = 0, size = 10, sortBy = 'orderDate', direction = 'desc', search = ''}){
        return api.get('deliveryOrders/search',{
            params: { page, size, sortBy, direction, search: search || undefined }
        })
    }
}

