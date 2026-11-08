<script setup>
import {ref, computed} from 'vue'
import routeService from '../../services/routesService.js'
import driverService from '../../services/driverService.js'
import vehicleService from '../../services/vehicleService.js'
import LookupSelect from '../../components/LookupSelect.vue'

const routes = ref([])
const drivers = ref([])
const vehicles = ref([])

const driverOptions = computed(() => drivers.value.map(d => ({
  value: d.id,
  label: `${d.firstName} ${d.lastName} - ${d.licenseNumber}${d.availability ? '' : ' (unavailable)'}`
})))
const vehicleOptions = computed(() => vehicles.value.map(v => ({
  value: v.vehicleId,
  label: `${v.numberPlate} - ${v.type} (${v.currentStatus})`
})))

function fetchLookups() {
  driverService.getAll().then(res => { drivers.value = res.data }).catch(err => console.error('Error loading drivers:', err))
  vehicleService.getAll().then(res => { vehicles.value = res.data }).catch(err => console.error('Error loading vehicles:', err))
}
const loading = ref(false)
const search = ref('')
const showForm = ref(false)
const editMode = ref(false)
const errorMessage = ref('')

const emptyForm = {
  routeId: '',
  driverId: '',
  vehicleId: '',
  startLocation: '',
  endLocation: '',
  distance: '',
  estimateTime: ''
}

const form = ref({ ...emptyForm })

function fetchRoutes() {
  loading.value = true

  routeService.getAll().then((response) => {
    routes.value = response.data
  }).catch((error) => {
    console.error('Error fetching routes:', error)
  }).finally(() => {
    loading.value = false
  })
}

function openAddForm() {
  fetchLookups()
  form.value = { ...emptyForm }
  editMode.value = false
  errorMessage.value = ''
  showForm.value = true
}

function openEditForm(route) {
  fetchLookups()
  form.value = {
    routeId: route.routeId,
    driverId: route.driver?.id || '',
    vehicleId: route.vehicle?.vehicleId || '',
    startLocation: route.startLocation,
    endLocation: route.endLocation,
    distance: route.distance,
    estimateTime: route.estimateTime
  }
  editMode.value = true
  errorMessage.value = ''
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  form.value = { ...emptyForm }
  errorMessage.value = ''
}


function buildPayload() {
  return {

    ...(editMode.value ? { routeId: form.value.routeId } : {}),
    driver: { id: form.value.driverId },
    vehicle: form.value.vehicleId ? { vehicleId: form.value.vehicleId } : null,
    startLocation: form.value.startLocation,
    endLocation: form.value.endLocation,
    distance: Number(form.value.distance),
    estimateTime: Number(form.value.estimateTime)
  }
}

function submitForm() {
  errorMessage.value = ''

  if (!form.value.driverId) {
    errorMessage.value = 'Please choose a driver.'
    return
  }

  const payload = buildPayload()

  const action = editMode.value
      ? routeService.update(payload)
      : routeService.create(payload)

  action.then(() => {
    fetchRoutes()
    closeForm()
  }).catch((error) => {
    console.error('Error saving route:', error)
    errorMessage.value = (error.response?.status === 500 || error.response?.status === 400)
        ? 'Could not save the route. The chosen driver or vehicle no longer exists.'
        : 'Could not save the route.'
  })
}

function deleteRoute(routeId) {
  if (confirm('Are you sure you want to delete this route?')) {
    routeService.delete(routeId).then(() => {
      fetchRoutes()
    }).catch((error) => {
      console.error('Error deleting route:', error)
      alert(error.response?.status === 409
          ? 'This route still has shipments. Move or delete them first.'
          : 'Could not delete the route.')
    })
  }
}

const filteredRoutes = () => {
  if (!search.value) return routes.value
  const term = search.value.toLowerCase()
  return routes.value.filter(route =>
      route.routeId?.toLowerCase().includes(term) ||
      route.driver?.id?.toLowerCase().includes(term) ||
      route.vehicle?.vehicleId?.toLowerCase().includes(term) ||
      route.startLocation?.toLowerCase().includes(term) ||
      route.endLocation?.toLowerCase().includes(term)
  )
}

fetchRoutes()
</script>

<template>
  <main class="admin-page">
    <div class="page-header">
      <div>
        <h1 class="text-centre">LIST OF ROUTES</h1>
        <p class="subtitle">Manage Routes</p>
      </div>
    </div>

    <div class="cardi">
      <div class="search-bar">
        <input
            v-model="search"
            type="text"
            placeholder="Search by route, driver, vehicle or location"
            class="search-input"
        />

        <button class="add-btn" @click="openAddForm">+ Add Route</button>
      </div>
    </div>

    <!-- Modal Form -->
    <div
        class="modal-overlay"
        v-if="showForm"
        @click.self="closeForm"
    >
      <div class="modal">
        <h2>
          {{ editMode ? 'Edit Route' : 'Add Route' }}
        </h2>

        <div class="form-group" v-if="editMode">
          <label>Route ID</label>
          <input v-model="form.routeId" type="text" disabled />
        </div>

        <div class="form-group">
          <label>Driver</label>
          <LookupSelect v-model="form.driverId" :options="driverOptions" placeholder="Search drivers..." />
        </div>

        <div class="form-group">
          <label>Vehicle (optional)</label>
          <LookupSelect v-model="form.vehicleId" :options="vehicleOptions" placeholder="Search vehicles..." allow-empty />
        </div>

        <div class="form-group">
          <label>Start Location</label>
          <input
              v-model="form.startLocation"
              type="text"
              placeholder="Start Location"
          />
        </div>

        <div class="form-group">
          <label>End Location</label>
          <input
              v-model="form.endLocation"
              type="text"
              placeholder="End Location"
          />
        </div>

        <div class="form-group">
          <label>Distance (km)</label>
          <input
              v-model="form.distance"
              type="number"
              step="0.1"
              min="0"
              placeholder="Distance in km"
          />
        </div>

        <div class="form-group">
          <label>Estimated Time (minutes)</label>
          <input
              v-model="form.estimateTime"
              type="number"
              min="0"
              placeholder="Estimated time"
          />
        </div>

        <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

        <div class="form-actions">
          <button class="cancel-btn" @click="closeForm">Cancel</button>
          <button class="submit-btn" @click="submitForm">{{ editMode ? 'Update' : 'Create' }}</button>
        </div>

      </div>
    </div>

    <!-- Routes Table -->
    <div class="table-wrap">
      <table class="routes-table">

        <thead>
        <tr>
          <th>Route ID</th>
          <th>Driver ID</th>
          <th>Vehicle ID</th>
          <th>Start Location</th>
          <th>End Location</th>
          <th>Distance</th>
          <th>Estimated Time</th>
          <th>Actions</th>
        </tr>
        </thead>

        <tbody>

        <tr v-if="loading">
          <td colspan="8">Loading...</td>
        </tr>

        <tr v-else-if="filteredRoutes().length === 0">
          <td colspan="8">No Route Found.</td>
        </tr>

        <tr v-for="route in filteredRoutes()" :key="route.routeId">
          <td>{{ route.routeId }}</td>
          <td>{{ route.driver?.id }}</td>
          <td>{{ route.vehicle?.vehicleId }}</td>
          <td>{{ route.startLocation }}</td>
          <td>{{ route.endLocation }}</td>
          <td>{{ route.distance }} km</td>
          <td>{{ route.estimateTime }} min</td>

          <td>
            <button class="edit-btn" @click="openEditForm(route)">Edit</button>
            <button class="delete-btn" @click="deleteRoute(route.routeId)">Delete</button>
          </td>
        </tr>

        </tbody>

      </table>
    </div>

  </main>
</template>

<style scoped>
.routes-page {
  --surface: #0b1326;
  --surface-container-lowest: #060e20;
  --surface-container-low: #131b2e;
  --surface-container: #171f33;
  --surface-container-high: #222a3d;
  --surface-container-highest: #2d3449;
  --surface-variant:#2d3449;
  --on-surface: #dae2fd;
  --on-surface-variant: #c2c6d6;
  --outline: #8c909f;
  --outline-variant: #424754;
  --primary: #adc6ff;
  --primary-container: #4d8eff;
  --on-primary-container: #00285d;
  --error: #ffb4ab;

  background: var(--surface);
  color: var(--on-surface);
  min-height: 100vh;
  padding: 2rem;
  font-family: 'Inter', sans-serif;
}

.page-header {
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--outline-variant);
  margin-bottom: 1.5rem;
}

.page-header h1 {
  margin-bottom: 0.5rem;
}

.card {
  background: var(--surface-container-low);
  border: 1px solid var(--outline-variant);
  border-radius: 1rem;
}

.search-input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(173, 198, 255, 0.15);
}

.add-btn {
  border: none;
  border-radius: 4px;

  padding: 9px 24px;

  font-size: 14px;
  font-weight: bolder;
  width: 150px;
  background: var(--admin-primary);
  color: #fff;
  text-decoration: none;
  margin-left:1rem;
}

.add-btn:hover {
  opacity: 0.85;
}

.table-wrap {
  overflow-x: auto;
  margin-top: 1.5rem;
}

.routes-table {
  width: 100%;
  border-collapse: collapse;
}

.routes-table thead tr {
  background: var(--surface-container-highest);
  border-bottom: 1px solid var(--outline-variant);
}

.routes-table th {
  text-align: center;
  font-size: 0.79rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  color: var(--on-surface-variant);
  padding: 0.75rem;
}

.routes-table tbody tr {
  border-bottom: 1px solid rgba(66, 71, 84, 0.5);
  transition: background 0.2s ease-in-out;
}

.routes-table tbody tr:hover {
  background: var(--surface-container);
}

.routes-table td {
  padding: 1rem;
  color: #dae2fd;
  font-size: 0.875rem;
  text-align: center;
}

.edit-btn {
  padding: 0.3rem 0.8rem;
  border-radius: 999px;
  border: 1px solid var(--primary);
  background: transparent;
  color: var(--primary);
  font-size: 0.8rem;
  cursor: pointer;
  margin-right: 0.5rem;
  transition: 0.2s;
}

.edit-btn:hover {
  background: var(--primary);
  color: #000;
}

.delete-btn {
  padding: 0.3rem 0.8rem;
  border-radius: 999px;
  border: 1px solid var(--error);
  background: transparent;
  color: var(--error);
  font-size: 0.8rem;
  cursor: pointer;
  transition: 0.2s;
}

.delete-btn:hover {
  background: var(--error);
  color: #000;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  overflow-y: auto;
  padding: 2rem 1rem;
  z-index: 999;
}

.modal {
  background: #171f33;
  border: 1px solid #424754;
  border-radius: 1rem;
  padding: 2rem;
  width: 100%;
  max-width: 450px;
  box-sizing: border-box;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.modal h2 {
  color: var(--on-surface);
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.form-group label {
  color: var(--on-surface-variant);
  font-size: 0.85rem;
}

.form-group input {
  padding: 0.6rem 1rem;
  border-radius: 0.5rem;
  border: 1px solid var(--outline-variant);
  background: var(--surface-container-lowest);
  color: var(--on-surface);
  font-size: 0.9rem;
  outline: none;
}

.form-group input:focus {
  border-color: var(--primary);
}

.form-group input:disabled {
  background: var(--surface-container);
  color: var(--on-surface-variant);
  cursor: not-allowed;
}

.error-text {
  color: var(--error);
  font-size: 0.85rem;
  margin: 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 0.5rem;
}

.cancel-btn {
  padding: 0.5rem 1.2rem;
  border-radius: 999px;
  border: 1px solid var(--outline-variant);
  background: transparent;
  color: var(--on-surface);
  cursor: pointer;
}

.cancel-btn:hover {
  background: var(--surface-container-high);
}

.submit-btn {
  padding: 0.5rem 1.2rem;
  border-radius: 999px;
  border: none;
  background: var(--primary-container);
  color: white;
  cursor: pointer;
}

.submit-btn:hover {
  opacity: 0.85;
}
</style>