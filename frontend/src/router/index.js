import {createRouter, createWebHistory} from "vue-router"
import {useAuth} from '../stores/authStore'
import HomeView from '../views/HomeView.vue'
import CustomerView from "../views/admin/CustomerView.vue"
import DeliveryOrdersView from "../views/admin/DeliveryOrdersView.vue"
import DriversView from "../views/admin/DriversView.vue"
import RoutesView from "../views/admin/RoutesView.vue"
import ShipmentView from "../views/admin/ShipmentView.vue"
import VehicleView from "../views/admin/VehicleView.vue"
import AdminLogin from "../views/admin/AdminLogin.vue"
import DeliveryOrderForm from "../views/admin/DeliveryOrderForm.vue"
import MyOrders from "../views/customer/MyOrders.vue"
import MyRoutes from "../views/driver/MyRoutes.vue"
import UsersView from "../views/admin/UsersView.vue"
import RegisterView from "../views/customer/RegisterView.vue"
import DashboardView from "../views/admin/DashboardView.vue";
import CustomerLogin from "../views/customer/CustomerLogin.vue";
import MyProfileView from "../views/customer/MyProfile.vue";
import MyReceiptView from "../views/customer/MyReceipt.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),

    

    routes: [
        // {
        //     path: "/",
        //     name: "home",
        //     component: HomeView,
        // },
        {
                path: "/home",
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


        {
            path: "/",
            redirect: "/home",
        },

        { 
            path: "/portal-x7q2/login", 
            component: AdminLogin },

        {
            path: "/my/orders",
            name: "myOrders",
            component: MyOrders,
            meta: { requiresRole: 'CUSTOMER' }
        },
        {
            path: "/my/routes",
            name: "myRoutes",
            component: MyRoutes,
            meta: { requiresRole: 'DRIVER' }
        },

        {
            path: "/users",
            name: "users",
            component: UsersView,
            meta: { requiresAdmin: true, showSideBar: true }
        },

        {
            path: '/register',
            name: 'register',
            component: RegisterView
        },

        {
            path: '/dashboard',
            name: 'dashboard',
            component: DashboardView,
            meta: { requiresAdmin: true, showSideBar: true }
        },

        {
            path: '/login',
            name: 'customer-login',
            component: CustomerLogin
        },

        {
            path: '/my/profile',
            name: 'my-profile',
            component: MyProfileView,
            meta: { requiresRole: 'CUSTOMER' }
        },
        {
            path: '/my/orders/:id/receipt',
            name: 'my-receipt',
            component: MyReceiptView,
            meta: { requiresRole: 'CUSTOMER' } }

        // {
        //     path: "/orders",
        //     component: DeliveryOrdersView,
        //     meta: { requiresAdmin: true }

        // }


    ]
})

router.beforeEach(async (to)=>{
    const needsAdmin = to.meta.requiresAdmin
    const needsRole = to.meta.requiresRole
    const loginFor = (to) => (to.path.startsWith('/my/orders') ? '/login' : '/portal-x7q2/login')

    if(!needsAdmin && !needsRole)
        return true

    const {isAdmin, checked, checkAuth, hasRole} = useAuth()
    if(!checked.value) await checkAuth()

    if(needsAdmin) {
        return isAdmin.value ? true : '/portal-x7q2/login'
    }
    return hasRole(needsRole) ? true : '/portal-x7q2/login'
})

export default router