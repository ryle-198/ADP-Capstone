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
          placeholder="Search by shipment, order, route or status"
          class="search-input"
      />
    </div>

    <!-- Modal Form -->
    <div class="modal-overlay" v-if="showForm" @click.self="closeForm">
      <div class="modal">
        <h2>{{ editMode ? 'Edit Shipment' : 'New Shipment' }}</h2>

        <div class="form-group" v-if="editMode">
          <label>Shipment ID</label>
          <input v-model="form.shipmentId" type="text" disabled />
        </div>

        <div class="form-group">
          <label>Order</label>
          <LookupSelect :model-value="form.orderId" @update:model-value="onOrderPicked"
                        :options="orderOptions" placeholder="Search orders..." />
        </div>

        <div class="form-group">
          <label>Route (optional)</label>
          <LookupSelect v-model="form.routeId" :options="routeOptions"
                        placeholder="Search routes..." allow-empty />
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
            <option v-for="s in statuses" :key="s" :value="s">{{ s }}</option>
          </select>
        </div>

        <div class="form-group">
          <label>
            <input type="checkbox" v-model="form.fragile" style="width: auto; margin-right: 0.5rem;" />
            Fragile
          </label>
        </div>

        <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

        <div class="form-actions">
          <button class="btn-secondary" @click="closeForm">Cancel</button>
          <button class="btn-primary" @click="submitForm">
            {{ editMode ? 'Update' : 'Create' }}
          </button>
        </div>
      </div>
    </div>

    <!-- History popup -->
    <div class="modal-overlay" v-if="historyFor" @click.self="closeHistory">
      <div class="modal">
        <h2>History for {{ historyFor }}</h2>
        <p v-if="historyError" class="error-text">{{ historyError }}</p>
        <p v-else-if="history.length === 0">No history.</p>
        <ul v-else>
          <li v-for="e in history" :key="e.eventId">
            {{ e.eventTime }} - {{ e.status }}
            <span v-if="e.location"> - {{ e.location }}</span>
            <span v-if="e.note"> - {{ e.note }}</span>
            <span> (by {{ e.recordedBy }})</span>
          </li>
        </ul>
        <div class="form-actions">
          <button class="btn-secondary" @click="closeHistory">Close</button>
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
          <td>{{ shipment.order?.orderId }}</td>
          <td>{{ shipment.route?.routeId }}</td>
          <td>{{ shipment.originAddress }}</td>
          <td>{{ shipment.destinationAddress }}</td>
          <td><span :class="statusBadgeClass(shipment.status)">{{ shipment.status }}</span></td>
          <td>{{ shipment.fragile ? 'Yes' : 'No' }}</td>
          <td>
            <button class="btn-outline" @click="showHistory(shipment.shipmentId)">History</button>
            <button class="btn-outline" @click="openEditForm(shipment)" style="margin-left: 0.5rem;">Edit</button>
            <button class="btn-danger" @click="deleteShipment(shipment.shipmentId)" style="margin-left: 0.5rem;">Delete</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import shipmentService from '../../services/shipmentService.js'
import orderService from '../../services/deliveryOrderService.js'
import routeService from '../../services/routesService.js'
import api from '../../services/api.js'
import LookupSelect from '../../components/LookupSelect.vue'

const shipments = ref([])
const orders = ref([])
const routes = ref([])

// lists behind the two dropdowns
const orderOptions = computed(() => orders.value.map(o => ({
  value: o.orderId,
  label: `${o.orderId} - ${o.customer?.customerName || o.customer?.customerId || 'no customer'} (${o.deliveryDate})`
})))
const routeOptions = computed(() => routes.value.map(r => ({
  value: r.routeId,
  label: `${r.routeId} - ${r.startLocation} to ${r.endLocation}`
})))

function fetchLookups(){
  orderService.getAll().then(res => { orders.value = res.data }).catch(err => console.error('Error loading orders:', err))
  routeService.getAll().then(res => { routes.value = res.data }).catch(err => console.error('Error loading routes:', err))
}

function onOrderPicked(orderId){
  form.value.orderId = orderId
  const order = orders.value.find(o => o.orderId === orderId)
  if(!order) return
  if(order.pickupAddress && !form.value.originAddress){
    form.value.originAddress = order.pickupAddress
  }
  const destination = order.deliveryAddress || order.customer?.address
  if(destination && !form.value.destinationAddress){
    form.value.destinationAddress = destination
  }
}
const loading = ref(false)
const search = ref('')
const showForm = ref(false)
const editMode = ref(false)
const errorMessage = ref('')

// history popup
const historyFor = ref('')
const history = ref([])
const historyError = ref('')

function showHistory(id){
  historyFor.value = id
  history.value = []
  historyError.value = ''
  api.get(`/shipment/history/${id}`)
      .then((res)=>{ history.value = res.data })
      .catch(()=>{ historyError.value = 'Could not load the history.' })
}

function closeHistory(){
  historyFor.value = ''
}

const statuses = [
  'CREATED', 'READY_FOR_PICKUP', 'PICKED_UP', 'IN_TRANSIT',
  'AT_HUB', 'OUT_FOR_DELIVERY', 'DELIVERED', 'DELAYED',
  'ON_HOLD', 'FAILED_DELIVERY', 'RETURN_INITIATED',
  'RETURN_IN_TRANSIT', 'RETURNED', 'CANCELLED', 'LOST', 'DAMAGED'
]


const emptyForm = {
  shipmentId: '',
  orderId: '',
  routeId: '',
  originAddress: '',
  destinationAddress: '',
  status: 'CREATED',
  fragile: false
}

const form = ref({ ...emptyForm })

function fetchShipments(){
  loading.value = true
  shipmentService.getAll().then((response)=>{
    shipments.value = response.data
  }).catch((error)=>{
    console.error('Error fetching shipments:', error)
  }).finally(()=>{
    loading.value = false
  })
}

function openAddForm(){
  fetchLookups()
  form.value = { ...emptyForm }
  editMode.value = false
  errorMessage.value = ''
  showForm.value = true
}


function openEditForm(shipment){
  fetchLookups()
  form.value = {
    shipmentId: shipment.shipmentId,
    orderId: shipment.order?.orderId || '',
    routeId: shipment.route?.routeId || '',
    originAddress: shipment.originAddress,
    destinationAddress: shipment.destinationAddress,
    status: shipment.status || 'CREATED',
    fragile: shipment.fragile
  }
  editMode.value = true
  errorMessage.value = ''
  showForm.value = true
}

function closeForm(){
  showForm.value = false
  form.value = { ...emptyForm }
  errorMessage.value = ''
}


function buildPayload(){
  return {
    ...(editMode.value ? { shipmentId: form.value.shipmentId } : {}),
    order: { orderId: form.value.orderId },
    route: form.value.routeId ? { routeId: form.value.routeId } : null,
    originAddress: form.value.originAddress,
    destinationAddress: form.value.destinationAddress,
    status: form.value.status || 'CREATED',
    fragile: form.value.fragile
  }
}

function submitForm(){
  errorMessage.value = ''

  if(!form.value.orderId){
    errorMessage.value = 'Please choose an order.'
    return
  }

  const payload = buildPayload()

  const action = editMode.value
      ? shipmentService.update(payload)
      : shipmentService.create(payload)

  action.then(()=>{
    fetchShipments()
    closeForm()
  }).catch((error)=>{
    console.error('Error saving shipment:', error)
    errorMessage.value = error.response?.status === 409
        ? 'That status change is not allowed from the current status.'
        : (error.response?.status === 500 || error.response?.status === 400)
            ? 'Could not save the shipment. The chosen order or route no longer exists.'
            : 'Could not save the shipment.'
  })
}

function deleteShipment(id) {
  if(!confirm(`Delete shipment ${id}?`)) return

  shipmentService.delete(id).then(()=>{
    fetchShipments()
  }).catch((error)=>{
    console.error('Error deleting shipment:', error)
  })
}

function statusBadgeClass(status){
  const terminal = ['DELIVERED']
  const bad = ['DELAYED', 'FAILED_DELIVERY', 'CANCELLED', 'LOST', 'DAMAGED']
  if(terminal.includes(status)) return 'badge badge-success'
  if(bad.includes(status)) return 'badge badge-error'
  return 'badge badge-info'
}

const filteredShipments = () =>{
  if(!search.value) return shipments.value
  const term = search.value.toLowerCase()
  return shipments.value.filter(s =>
      s.shipmentId?.toLowerCase().includes(term) ||
      s.order?.orderId?.toLowerCase().includes(term) ||
      s.route?.routeId?.toLowerCase().includes(term) ||
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

.error-text {
  color: var(--error);
  font-size: 0.85rem;
  margin: 0;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  overflow-y: auto;
  padding: 2rem 1rem;
  z-index: 999;
}

.modal {
  width: 100%;
  max-width: 480px;
  box-sizing: border-box;
  margin: auto;
}

</style>