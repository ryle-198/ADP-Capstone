<script setup>
import {ref, onMounted} from 'vue'
import {RouterLink} from 'vue-router'
import  deliveryOrderServices from '../services/deliveryOrderService'

const orders = ref([])
const loading = ref(false)
 
function fetchOrders(){
    loading.value=true
    deliveryOrderServices.getAll().then((response)=>{
        orders.value=response.data
    }).finally(()=>{
        loading.value=false
    })
}

function removeOrder(id){
    if(!confirm(`Delete order ${id}?`)) return
    deliveryOrderServices.delete(id).then(fetchOrders)
}

onMounted(fetchOrders)
</script>
<template>
    <main class="admin-page">
        <div class="page-header">
            <div>
                <h1 class="text-center">LIST OF DELIVERY ORDERS</h1>
                <p class ="subtitle">Manage InBound Logistics</p>
            </div>
            <RouterLink to="/orders/new" class="new-btn">+ new order</RouterLink>
        </div>

        <!--<div class="card">
            <div class="search-bar">
            <input
            v-model="search"
            type="text"
            placeholder="Search by order ID" 
            class="search-input"/>maybe make it so it can search with customer id as well
            </div>
        </div>-->

        <div class=table-wrap>
            <table class="orders-table">
                <thead>
                    <tr>
                    <th>Order ID</th>
                    <th>Customer ID</th>
                    <th>Order Date</th>
                    <th>Delivery Date</th>
                    <th>Status</th>
                    <th>Payment</th>
                    <th>Total</th>
                    <th>Instructions</th>
                    <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading">
                        <td colspan="9">Loading...</td>
                    </tr>

                    <tr v-else-if="orders.length === 0">
                        <td colspan="9">No Orders Found.</td>
                    </tr>

                    <tr v-else v-for="order in orders" :key="order.orderId">
                        <td>{{order.orderId}}</td>
                        <td>{{order.customer?.customerId}}</td>
                        <td>{{order.orderDate}}</td>
                        <td>{{order.deliveryDate}}</td>
                        <td>{{order.deliveryStatus}}</td>
                        <td>{{order.paymentStatus}}</td>
                        <td>{{order.totalCost?.toFixed(2)}}</td>
                        <td>{{order.specialInstructions}}</td>
                        <td>
                            <RouterLink :to="`/orders/${order.orderId}/edit`" class="btn-outline">Edit</RouterLink>
                            <button @click="removeOrder(order.orderId)" class="btn-danger">Delete</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- <div class="pagination">
            <button class="page-btn" :disabled="page === 0" @click="goToPage(page - 1)">Prev</button>
            <span class="page-info">Page {{ page + 1 }} of {{ totalPages || 1 }}</span>
            <button class="page-btn" :disabled="page >= totalPages - 1" @click="goToPage(page + 1)">Next</button>
        </div> -->
    </main>
    
</template>

<style scoped>

/* .page-header{
    padding-bottom: 1rem;
    border-bottom: 1px solid var(--outline-variant);
    margin-bottom: 1.5rem;
    display: grid;
    justify-content: center;
} */

.text-center{
    display: grid;
    justify-content: center;
}

/* .subtitle{
    color: var(--outline-variant);
    display: grid;
    justify-content: center;
    padding-top:1rem;
} */

.card{
    /* background-color:#060e20; */
}

.search-bar{
    display: grid;
    justify-content: center;
    padding:1.25rem;
}

.search-input {
  border: 2px solid var(--outline-variant);
  border-radius: 2rem;
  padding: 0.6rem 1rem 0.6rem 2.5rem;  margin-bottom: 1rem;
  width: 100%;
  font-size: 0.9rem;
  max-width: 400px;
  outline: none;
  transition: 0.2s ease-in-out;
  background-color:var(--dark-alt);
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

.new-btn { 
    display: grid;
    justify-content: center;

    border: none;
    border-radius: 4px;

    padding: 9px 24px;
    
    font-size: 14px;
    font-weight: bolder;
    width: 150px;
    background: var(--admin-primary);
    color: #fff;
    text-decoration: none; 
    }
    
</style>