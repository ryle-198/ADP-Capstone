<template>
  <div class="shipment-container">
    <h1>Shipment Management</h1>

    <!-- Create Shipment Form -->
    <div class="form-section">
      <h2>Create Shipment</h2>
      <input v-model="form.shipmentId" placeholder="Shipment ID" />
      <input v-model="form.orderId" placeholder="Order ID" />
      <input v-model="form.routeId" placeholder="Route ID" />
      <input v-model="form.originAddress" placeholder="Origin Address" />
      <input v-model="form.destinationAddress" placeholder="Destination Address" />
      <select v-model="form.status">
        <option value="">Select Status</option>
        <option v-for="s in statuses" :key="s" :value="s">{{ s }}</option>
      </select>
      <label>
        <input type="checkbox" v-model="form.fragile" /> Fragile
      </label>
      <button @click="createShipment">Create</button>
    </div>

    <!-- Shipments Table -->
    <div class="table-section">
      <h2>All Shipments</h2>
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
        <tr v-for="shipment in shipments" :key="shipment.shipmentId">
          <td>{{ shipment.shipmentId }}</td>
          <td>{{ shipment.orderId }}</td>
          <td>{{ shipment.routeId }}</td>
          <td>{{ shipment.originAddress }}</td>
          <td>{{ shipment.destinationAddress }}</td>
          <td>{{ shipment.status }}</td>
          <td>{{ shipment.fragile ? 'Yes' : 'No' }}</td>
          <td>
            <button @click="deleteShipment(shipment.shipmentId)">Delete</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const shipments = ref([])

const statuses = [
  'CREATED', 'READY_FOR_PICKUP', 'PICKED_UP', 'IN_TRANSIT',
  'AT_HUB', 'OUT_FOR_DELIVERY', 'DELIVERED', 'DELAYED',
  'ON_HOLD', 'FAILED_DELIVERY', 'RETURN_INITIATED',
  'RETURN_IN_TRANSIT', 'RETURNED', 'CANCELLED', 'LOST', 'DAMAGED'
]

const form = ref({
  shipmentId: '',
  orderId: '',
  routeId: '',
  originAddress: '',
  destinationAddress: '',
  status: '',
  fragile: false
})

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
</script>

<style scoped>
.shipment-container {
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
}
</style>