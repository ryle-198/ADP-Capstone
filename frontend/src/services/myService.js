import api from './api'

// Endpoints for the logged-in customer / driver.
export default {

    //customer
    getMyOrders() {
        return api.get('/my/customer/orders')
    },

    getMyOrder(orderId) {
        return api.get(`/my/customer/orders/${orderId}`)
    },

    placeOrder(order) {
        return api.post('/my/customer/orders', order)
    },

    payOrder(orderId) {
        return api.post(`/my/customer/orders/${orderId}/pay`)
    },

    getMyOrderShipments(orderId) {
        return api.get(`/my/customer/orders/${orderId}/shipments`)
    },

    trackShipment(shipmentId) {
        return api.get(`/my/customer/shipments/${shipmentId}`)
    },

    getShipmentHistory(shipmentId) {
        return api.get(`/my/customer/shipments/${shipmentId}/history`)
    },

    cancelOrder(orderId) {
        return api.post(`/my/customer/orders/${orderId}/cancel`)
    },

    getProfile() {
        return api.get('/my/customer/profile')
    },

    updateProfile(profile) {

        return api.put('/my/customer/profile', profile)
    },

    changePassword(currentPassword, newPassword) {
        return api.post('/my/customer/password', { currentPassword, newPassword })
    },

    // driver
    getMyRoutes() {
        return api.get('/my/driver/routes')
    },

    getMyRouteShipments(routeId) {
        return api.get(`/my/driver/routes/${routeId}/shipments`)
    },

    updateShipmentStatus(shipmentId, status, location, note) {
        return api.put(`/my/driver/shipments/${shipmentId}/status`, { status, location, note })
    }
}