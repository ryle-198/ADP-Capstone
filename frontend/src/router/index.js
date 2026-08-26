import {createRouter, createWebHistory} from "vue-router"
import {useAuth} from '../stores/authStore'
import HomeView from '../views/HomeView.vue'
import CustomerView from "../views/CustomerView.vue"
import DeliveryOrdersView from "../views/DeliveryOrdersView.vue"
import DriversView from "../views/DriversView.vue"
import RoutesView from "../views/RoutesView.vue"
import ShipmentView from "../views/ShipmentView.vue"
import VehicleView from "../views/VehicleView.vue"
import AdminLogin from "../views/admin/AdminLogin.vue"
import DeliveryOrderForm from "../views/admin/DeliveryOrderForm.vue"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),

    

    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView,
        },

        {
            path: "/orders",
            name: "orders",
            component: DeliveryOrdersView,
            meta: { 
                requiresAdmin: true ,
                showSideBar: true,
            }
        },
         {
            path: "/customers",
            name: "customers",
            component: CustomerView,
            meta: { 
                requiresAdmin: true ,
                showSideBar: true,
            }
        },
         {
            path: "/drivers",
            name: "drivers",
            component: DriversView,
            meta: { 
                requiresAdmin: true ,
                showSideBar: true,
            }
        },
         {
            path: "/routes",
            name: "routes",
            component: RoutesView,
            meta: { 
                requiresAdmin: true ,
                showSideBar: true,
            }
        },
         {
            path: "/shipment",
            name: "shipment",
            component: ShipmentView,
            meta: { 
                requiresAdmin: true ,
                showSideBar: true,
            }
        },
         {
            path: "/vehicle",
            name: "vehicle",
            component: VehicleView,
            meta: { 
                requiresAdmin: true ,
                showSideBar: true,
            }
        },

        {
            path: "/orders/new",
            component: DeliveryOrderForm,
            meta:{ requiresAdmin: true,
                showSideBar: true,
            }
        },

        {
            path: "/orders/:id/edit",
            component: DeliveryOrderForm,
            meta: { requiresAdmin: true, showSideBar: true }
            },


        // {
        //     path: "/",
        //     redirect: "/portal-x7q2/login",
        // },

        { 
            path: "/portal-x7q2/login", 
            component: AdminLogin },

        // {
        //     path: "/orders",
        //     component: DeliveryOrdersView,
        //     meta: { requiresAdmin: true }

        // }


    ]
})

router.beforeEach(async (to)=>{
    if(!to.meta.requiresAdmin) 
        return true

    const {isAdmin, checked, checkAuth} = useAuth()
    if(!checked.value) await checkAuth()

    if (!isAdmin.value) {
        return '/portal-x7q2/login'
    }
    return true

})

export default router