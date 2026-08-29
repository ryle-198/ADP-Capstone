<script setup>
import {ref} from 'vue'
import routeService from '../services/routesService'

const routes = ref([])
const loading = ref(false)
const search = ref('')
const showForm = ref(false)
const editMode = ref(false)

const emptyForm = {
  routeId: '',
  driverId: '',
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
  form.value = { ...emptyForm }
  editMode.value = false
  showForm.value = true
}

function openEditForm(route) {
  form.value = { ...route }
  editMode.value = true
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  form.value = { ...emptyForm }
}

function submitForm() {
  if (editMode.value) {
    routeService.update(form.value).then(() => {
      fetchRoutes()
      closeForm()
    }).catch((error) => {
      console.error('Error updating route:', error)
    })
  } else {
    routeService.create(form.value).then(() => {
      fetchRoutes()
      closeForm()
    }).catch((error) => {
      console.error('Error creating route:', error)
    })
  }
}

function deleteRoute(routeId) {
  if (confirm('Are you sure you want to delete this route?')) {
    routeService.delete(routeId).then(() => {
      fetchRoutes()
    }).catch((error) => {
      console.error('Error deleting route:', error)
    })
  }
}

const filteredRoutes = () => {
  if (!search.value) return routes.value
  return routes.value.filter(route =>
      route.routeId?.toLowerCase().includes(search.value.toLowerCase()) ||
      route.driverId?.toLowerCase().includes(search.value.toLowerCase()) ||
      route.startLocation?.toLowerCase().includes(search.value.toLowerCase()) ||
      route.endLocation?.toLowerCase().includes(search.value.toLowerCase())
  )
}

fetchRoutes()
</script>

<template>
  <main class="routes-page">
    <div class="page-header">
      <div>
        <h1 class="text-centre">LIST OF ROUTES</h1>
        <p class="subtitle">Manage Routes</p>
      </div>
    </div>

    <div class="card">
      <div class="search-bar">
        <input
            v-model="search"
            type="text"
            placeholder="Search by route, driver or location"
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

        <div class="form-group">
          <label>Route ID</label>
          <input
              v-model="form.routeId"
              type="text"
              placeholder="Route ID"
              :disabled="editMode"
          />
        </div>

        <div class="form-group">
          <label>Driver ID</label>
          <input
              v-model="form.driverId"
              type="text"
              placeholder="Driver ID"
          />
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

        <div calss="form-group">
          <label>Estimated Time (minutes)</label>
          <input
              v-model="form.estimateTime"
              type="number"
              min="0"
              placeholder="Estimated time"
          />
        </div>

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
          <th>Start Location</th>
          <th>End Location</th>
          <th>Distance</th>
          <th>Estimated Time</th>
          <th>Actions</th>
        </tr>
        </thead>

        <tbody>

        <tr v-if="loading">
          <td colspan="7">Loading...</td>
        </tr>

        <tr v-else-if="filteredRoutes().length === 0">
          <td colspan="7">No Route Found.</td>
        </tr>

        <tr v-for="route in filteredRoutes()" :key="route.routeId">
          <td>{{ route.routeId }}</td>
          <td>{{ route.driverId }}</td>
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

.subtitle {
  text-align: center;
  color: var(--on-surface-variant);
}

.card {
  background: var(--surface-container-low);
  border: 1px solid var(--outline-variant);
  border-radius: 1rem;
}

.search-bar {
  padding: 1.25rem;
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-input {
  border: 1px solid var(--outline-variant);
  border-radius: 2rem;
  padding: 0.6rem 1rem;
  width: 100%;
  max-width: 400px;
  font-size: 0.9rem;
  outline: none;
  transition: 0.2s ease-in-out;
}

.search-input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(173, 198, 255, 0.15);
}

.add-btn {
  padding: 0.5rem 1.2rem;
  border-radius: 999px;
  border: none;
  background: var(--primary-container);
  color: white;
  font-size: 0.85rem;
  cursor: pointer;
  transition: 0.2s;
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
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal {
  background: #171f33;
  border: 1px solid #424754;
  border-radius: 1rem;
  padding: 2rem;
  width: 100%;
  max-width: 450px;
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

.card {
  background: var(--surface-container-low);
  border: 1px solid var(--outline-variant);
  border-radius: 1rem;
}

</style>