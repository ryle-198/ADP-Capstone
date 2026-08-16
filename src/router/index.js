import {createRouter, createWebHistory} from "vue-router"
import CustomerView from "../views/CustomerView.vue"
import DeliveryOrdersView from "../views/DeliveryOrdersView.vue"
import DriversView from "../views/DriversView.vue"
import RoutesView from "../views/RoutesView.vue"
import ShipmentView from "../views/ShipmentView.vue"
import VehicleView from "../views/VehicleView.vue"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),

    

    routes: [
        {
            path: "/orders",
            name: "orders",
            component: DeliveryOrdersView,
        },
         {
            path: "/customers",
            name: "customers",
            component: CustomerView,
        },
         {
            path: "/drivers",
            name: "drivers",
            component: DriversView,
        },
         {
            path: "/routes",
            name: "routes",
            component: RoutesView,
        },
         {
            path: "/shipment",
            name: "shipment",
            component: ShipmentView,
        },
         {
            path: "/vehicle",
            name: "vehicle",
            component: VehicleView,
        },

        {
            path: "/",
            redirect: "/orders",
        },


    ]
})

export default router