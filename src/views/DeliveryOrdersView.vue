<script setup>
import {ref, watch} from 'vue'
import  deliveryOrderServices from '../services/deliveryOrderService'

const orders = ref([])
const page = ref(0)
const size = ref(10)
const totalPages = ref(0)
const totalElements = ref(0)
const sortBy = ref('orderDate')
const direction = ref('desc')
const search = ref('')
const loading  = ref(false)

let searchTimeout = null

// function getAllDeliveryOrders(){
//     deliveryOrderServices.getAll().then((response) => {
//         deliveryOrders.value = response.data
//     })
// }

function fetchOrders(){
    loading.value = true
    deliveryOrderServices.getPaged({
        page: page.value,
        size: size.value,
        sortBy: sortBy.value,
        direction: direction.value,
        search: search.value
    }).then((response)=>{
        orders.value = response.data.content
        totalPages.value = response.data.totalPages
        totalElements.value = response.data.totalElements
    }).finally(()=>{
        loading.value= false
    })
}

function sortByColumn(column){
    if(sortBy.value === column){
        direction.value = direction.value ==='asc' ? 'desc' : 'asc'
    }else{
        sortBy.value = column
        direction.value = 'asc'
    }
    page.value = 0 
    fetchOrders()
}

function goToPage(newPage){
    if(newPage < 0 || newPage >= totalPages.value) return 
    page.value = newPage
    fetchOrders()
 }

 watch(search, () => {
    clearTimeout(searchTimeout)
    searchTimeout = setTimeout(()=>{
        page.value = 0
        fetchOrders()
    }, 400)
 })

fetchOrders() // this could MAYBE cause issues since the one at the bottom is being called as well

// onMounted(() => {
//     getAllDeliveryOrders()
//     // fetchOrders()
// })


</script>
<template>
    <main class="orders-page">
        <div class="page-header">
            <div>
                <h1 class="text-center">LIST OF DELIVERY ORDERS</h1>
                <p class ="subtitle">Manage InBound Logistics</p>
            </div>
        </div>

        <div class="card">
            <div class="search-bar">
            <input
            v-model="search"
            type="text"
            placeholder="Search by order ID" 
            class="search-input"/><!--maybe make it so it can search with customer id as well-->
            </div>
        </div>

        <div class=table-wrap>
            <table class="orders-table">
                <thead>
                    <tr>
                    <th @click="sortByColumn('orderId')" class="sortable">
                        Order ID<span v-if="sortBy === 'orderDate'">{{direction === 'asc' ? '^': 'v'}}</span>
                    </th>
                    <th>Customer ID</th>
                    <th @click="sortByColumn('orderDate')">
                        Order Date<span v-if="sortBy === 'orderDate'" class="sortable">{{direction === 'asc' ? '^': 'v'}}</span>
                    </th>
                    <th @click ="sortByColumn('deliveryDate')">
                        Delivery Date<span v-if="sortBy === 'deliveryDate'" class="sortable">{{direction === 'asc' ? '^': 'v'}}</span>
                    </th>
                    <th>Delivery Status</th>
                    <th>Payment Status</th>
                    <th>Total Cost</th>
                    <th>Special Instructions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading">
                        <td colspan="7">Loading...</td>
                    </tr>

                    <tr v-else-if="orders.length === 0">
                        <td colspan="7">No Orders Found.</td>
                    </tr>

                    <tr v-for ="order in orders" v-bind:key ="order.orderId">
                        <td>{{order.orderId}}</td>
                        <td>{{order.customer?.customerId}}</td>
                        <td>{{order.orderDate}}</td>
                        <td>{{order.deliveryDate}}</td>
                        <td>{{order.deliveryStatus}}</td>
                        <td>{{order.paymentStatus}}</td>
                        <td>{{order.totalCost}}</td>
                        <td>{{order.specialInstructions}}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="pagination">
            <button class="page-btn" :disabled="page === 0" @click="goToPage(page - 1)">Prev</button>
            <span class="page-info">Page {{ page + 1 }} of {{ totalPages || 1 }}</span>
            <button class="page-btn" :disabled="page >= totalPages - 1" @click="goToPage(page + 1)">Next</button>
        </div>
    </main>
    
</template>

<style scoped>
.orders-page {

  --surface: #0b1326;
  --surface-container-lowest: #060e20;
  --surface-container-low: #131b2e;
  --surface-container: #171f33;
  --surface-container-high: #222a3d;
  --surface-container-highest: #2d3449;
  --surface-variant: #2d3449;
  --on-surface: #dae2fd;
  --on-surface-variant: #c2c6d6;
  --outline: #8c909f;
  --outline-variant: #424754;
  --primary: #adc6ff;
  --primary-container: #4d8eff;
  --on-primary-container: #00285d;
  --secondary: #4edea3;
  --secondary-container: #00a572;
  --tertiary: #ffb95f;
  --tertiary-container: #ca8100;
  --error: #ffb4ab;
  --error-container: #93000a;
  
  min-height: 100vh;
  padding: 2rem;
  font-family: 'Inter', sans-serif;
}

.page-header{
    padding-bottom: 1rem;
    border-bottom: 1px solid var(--outline-varient);
    margin-bottom: 1.5rem;
}

.card{
    /* background-color:#060e20; */
}

.search-bar{

    padding:1.25rem;
}

.search-input {
  border: 1px solid var(--outline-variant);
  border-radius: 2rem;
  padding: 0.6rem 1rem 0.6rem 2.5rem;  margin-bottom: 1rem;
  width: 100%;
  font-size: 0.9rem;
  max-width: 400px;
  outline: none;
  transition: 0.2s ease-in-out;
}

.search-input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(173, 198, 255, 0.15);
}

th {
  cursor: pointer;
  user-select: none;
}
.pagination {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-top: 1rem;
}

.table-wrap {
  overflow-x: auto;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
}

.orders-table thead tr {
  background: var(--surface-container-highest);
  border-bottom: 1px solid var(--outline-variant);
}

.orders-table th {
  /* padding: 1rem; */
  text-align: center;
  font-size: 0.79rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  color: var(--on-surface-variant);
}

.orders-table th.sortable {
  cursor: pointer;
  user-select: none;
}

.orders-table tbody tr {
  border-bottom: 1px solid rgba(66, 71, 84, 0.5);
  transition: background 0.2s ease-in-out;
}

.orders-table tbody tr:hover {
  background: var(--surface-container);
}

.orders-table td {
  padding: 1rem;
  color: var(--on-surface);
  font-size: 0.875rem;
  /* white-space: nowrap; */
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 1.25rem;
  /* border-top: 1px solid var(--outline-variant); */
}

.page-btn {
  padding: 0.5rem 1.1rem;
  border-radius: 999px;
  border: 1px solid var(--outline-variant);
  background: var(--surface-container-lowest);
  color: var(--on-surface);
  font-size: 0.85rem;
  font-family: inherit;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--primary);
  color: var(--primary);
}

.page-btn:disabled {
  opacity: 0.4;
  /* cursor: not-allowed; */
}

.page-info {
  font-size: 0.85rem;
  color: var(--on-surface-variant);
}
</style>