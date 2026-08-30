<template>
  <main class="admin-page">
    <div class="page-header">
      <div>
        <h1>SHIPMENTS</h1>
        <p class="subtitle">Track shipments moving through the network</p>
      </div>
      <button class="btn-primary" @click="openAddForm">+ New Shipment</button>
    </div>

    <div class="toolbar">
      <input
        v-model="search"
        type="text"
        placeholder="Search by shipment, order, or status"
        class="search-input"
      />
    </div>

    <!-- Modal Form -->
    <div class="modal-overlay" v-if="showForm" @click.self="closeForm">
      <div class="modal">
        <h2>{{ editMode ? 'Edit Shipment' : 'New Shipment' }}</h2>

        <div class="form-group">
          <label>Shipment ID</label>
          <input v-model="form.shipmentId" type="text" placeholder="e.g. SHP-001" :disabled="editMode" />
        </div>

        <div class="form-group">
          <label>Order ID</label>
          <input v-model="form.orderId" type="text" placeholder="e.g. 001" />
        </div>

        <div class="form-group">
          <label>Route ID</label>
          <input v-model="form.routeId" type="text" placeholder="e.g. RT-001" />
        </div>

        <div class="form-group">
          <label>Origin Address</label>
          <input v-model="form.originAddress" type="text" placeholder="Origin Address" />
        </div>

        <div class="form-group">
          <label>Destination Address</label>
          <input v-model="form.destinationAddress" type="text" placeholder="Destination Address" />
        </div>

        <div class="form-group">
          <label>Status</label>
          <select v-model="form.status">
            <option value="">Select Status</option>
            <option v-for="s in statuses" :key="s" :value="s">{{ s }}</option>
          </select>
        </div>

        <div class="form-group">
          <label>
            <input type="checkbox" v-model="form.fragile" style="width: auto; margin-right: 0.5rem;" />
            Fragile
          </label>
        </div>

        <div class="form-actions">
          <button class="btn-secondary" @click="closeForm">Cancel</button>
          <button class="btn-primary" @click="submitForm">
            {{ editMode ? 'Update' : 'Create' }}
          </button>
        </div>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Shipment ID</th>
            <th>Order ID</th>
            <th>Route ID</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Status</th>
            <th>Fragile</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading" class="empty-row"><td colspan="8">Loading...</td></tr>
          <tr v-else-if="filteredShipments().length === 0" class="empty-row"><td colspan="8">No shipments found.</td></tr>
          <tr v-else v-for="shipment in filteredShipments()" :key="shipment.shipmentId">
            <td>{{ shipment.shipmentId }}</td>
            <td>{{ shipment.orderId }}</td>
            <td>{{ shipment.routeId }}</td>
            <td>{{ shipment.originAddress }}</td>
            <td>{{ shipment.destinationAddress }}</td>
            <td><span :class="statusBadgeClass(shipment.status)">{{ shipment.status }}</span></td>
            <td>{{ shipment.fragile ? 'Yes' : 'No' }}</td>
            <td>
              <button class="btn-outline" @click="openEditForm(shipment)">Edit</button>
              <button class="btn-danger" @click="deleteShipment(shipment.shipmentId)" style="margin-left: 0.5rem;">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import shipmentService from '../services/shipmentService'

const shipments = ref([])
const loading = ref(false)
const search = ref('')
const showForm = ref(false)
const editMode = ref(false)

const statuses = [
  'CREATED', 'READY_FOR_PICKUP', 'PICKED_UP', 'IN_TRANSIT',
  'AT_HUB', 'OUT_FOR_DELIVERY', 'DELIVERED', 'DELAYED',
  'ON_HOLD', 'FAILED_DELIVERY', 'RETURN_INITIATED',
  'RETURN_IN_TRANSIT', 'RETURNED', 'CANCELLED', 'LOST', 'DAMAGED'
]

const emptyForm = ref({
  shipmentId: '',
  orderId: '',
  routeId: '',
  originAddress: '',
  destinationAddress: '',
  status: '',
  fragile: false
})

const form = ref({...emptyForm})

function fetchShipments(){
  loading.value = true
  shipmentService.getAll().then((response)=>{
    shipments.value = response.data
  }).finally(()=>{
    loading.value = false
  })
}

function openAddForm(){
  form.value = {...emptyForm}
  editMode.value = false
  showForm.value = true
}

function openEditForm(shipment){
  form.value = {...shipment}
  editMode.value = true
  showForm.value = true
}

function closeForm(){
  showForm.value= false
  form.value = {...emptyForm}
}

function submitForm(){
  if(!form.value.shipmentId || !form.value.orderId){
    alert('Shipment ID and Order ID are required')
    return
  }

  const action = editMode.value
  ? shipmentService.update(form.value)
  : shipmentService.create(form.value)

  action.then(()=>{
    fetchShipments()
    closeForm()
  })
}


function createShipment() {
  if (!form.value.shipmentId || !form.value.orderId) {
    alert('Please fill in all fields')
    return
  }
  shipments.value.push({ ...form.value })
  form.value = {
    shipmentId: '',
    orderId: '',
    routeId: '',
    originAddress: '',
    destinationAddress: '',
    status: '',
    fragile: false
  }
}

function deleteShipment(id) {
  shipments.value = shipments.value.filter(s => s.shipmentId !== id)
}

function statusBadgeClass(status){
  const terminal = ['DELIVERED']
  const bad = ['DELAYED', 'FAILED_DELIVERY', 'CANCELLED', 'LOST', 'DAMAGED']
  if(terminal.includes(status)) return 'badge badge-success'
  if(bad.includes(status)) return 'badege badge-error'
  return 'badge badge-info'
}

const filteredShipments = () =>{
  if(!search.value) return shipments.value
  const term = search.value.toLowerCase()
  return shipments.value.filter(s =>
  s.shipmentId?.toLowerCase().includes(term) ||
  s.orderId?.toLowerCase().includes(term) ||
  s.status?.toLowerCase().includes(term)
  )
}

onMounted(fetchShipments)

</script>

<style scoped>

.btn-primary { 
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
/*
.shipment-containxer {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.form-section {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 500px;
}

input, select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 8px 16px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background: #45a049;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

th {
  background: #4CAF50;
  color: white;
}

tr:nth-child(even) {
  background: #f9f9f9;
}*/
</style>