<script setup>
import { ref } from 'vue'
import driverService from '../services/driverService'

const drivers = ref([])
const loading = ref(false)
const search = ref('')
const showForm = ref(false)
const editMode = ref(false)

const emptyForm = {
  id: '',
  firstName: '',
  lastName: '',
  licenseNumber: '',
  phoneNumber: '',
  availability: true
}

const form = ref({ ...emptyForm })

function fetchDrivers() {
  loading.value = true
  driverService.getAll().then((response) => {
    drivers.value = response.data
  }).finally(() => {
    loading.value = false
  })
}

function openAddForm() {
  form.value = { ...emptyForm }
  editMode.value = false
  showForm.value = true
}

function openEditForm(driver) {
  form.value = { ...driver }
  editMode.value = true
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  form.value = { ...emptyForm }
}

function submitForm() {
  if (editMode.value) {
    driverService.update(form.value).then(() => {
      fetchDrivers()
      closeForm()
    })
  } else {
    driverService.create(form.value).then(() => {
      fetchDrivers()
      closeForm()
    })
  }
}

function deleteDriver(id) {
  if (confirm('Are you sure you want to delete this driver?')) {
    driverService.delete(id).then(() => {
      fetchDrivers()
    })
  }
}

const filteredDrivers = () => {
  if (!search.value) return drivers.value
  return drivers.value.filter(d =>
      d.id?.toLowerCase().includes(search.value.toLowerCase()) ||
      d.firstName?.toLowerCase().includes(search.value.toLowerCase()) ||
      d.lastName?.toLowerCase().includes(search.value.toLowerCase())
  )
}

fetchDrivers()
</script>

<template>
  <main class="admin-page">
    <div class="page-header">
      <div>
        <h1 class="text-center">LIST OF DRIVERS</h1>
        <p class="subtitle">Manage Freight Drivers</p>
      </div>
    </div>

    <div class="card0">
      <div class="search-bar">
        <input
            v-model="search"
            type="text"
            placeholder="Search by name or ID"
            class="search-input" />
        <button class="add-btn" @click="openAddForm">+ Add Driver</button>
      </div>
    </div>

    <!-- Modal Form -->
    <div class="modal-overlay" v-if="showForm" @click.self="closeForm">
      <div class="modal">
        <h2>{{ editMode ? 'Edit Driver' : 'Add Driver' }}</h2>
        <div class="form-group">
          <label>First Name</label>
          <input v-model="form.firstName" type="text" placeholder="First Name" />
        </div>
        <div class="form-group">
          <label>Last Name</label>
          <input v-model="form.lastName" type="text" placeholder="Last Name" />
        </div>
        <div class="form-group">
          <label>License Number</label>
          <input v-model="form.licenseNumber" type="text" placeholder="License Number" />
        </div>
        <div class="form-group">
          <label>Phone Number</label>
          <input v-model="form.phoneNumber" type="text" placeholder="Phone Number" />
        </div>
        <div class="form-group">
          <label>Availability</label>
          <select v-model="form.availability">
            <option :value="true">Available</option>
            <option :value="false">Unavailable</option>
          </select>
        </div>
        <div class="form-actions">
          <button class="cancel-btn" @click="closeForm">Cancel</button>
          <button class="submit-btn" @click="submitForm">
            {{ editMode ? 'Update' : 'Create' }}
          </button>
        </div>
      </div>
    </div>

    <div class="table-wrap">
      <table class="drivers-table">
        <thead>
        <tr>
          <th>Driver ID</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>License Number</th>
          <th>Phone Number</th>
          <th>Availability</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="loading">
          <td colspan="7">Loading...</td>
        </tr>
        <tr v-else-if="filteredDrivers().length === 0">
          <td colspan="7">No Drivers Found.</td>
        </tr>
        <tr v-for="driver in filteredDrivers()" :key="driver.id">
          <td>{{ driver.id }}</td>
          <td>{{ driver.firstName }}</td>
          <td>{{ driver.lastName }}</td>
          <td>{{ driver.licenseNumber }}</td>
          <td>{{ driver.phoneNumber }}</td>
          <td>
                            <span :class="driver.availability ? 'badge-available' : 'badge-unavailable'">
                                {{ driver.availability ? 'Available' : 'Unavailable' }}
                            </span>
          </td>
          <td>
            <button class="edit-btn" @click="openEditForm(driver)">Edit</button>
            <button class="delete-btn" @click="deleteDriver(driver.id)">Delete</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<style scoped>

.page-header {
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--outline-variant);
  margin-bottom: 1.5rem;
}

.search-bar {
  /* //padding: 1.25rem;
  //display: flex;
  //gap: 1rem;
  //align-items: center; */
  margin-bottom: 1.5rem;
}

.search-input {

  width: 100%;

  max-width: 450px;

  padding: 0.7rem 1rem;

  border: 1px solid var(--outline-variant);

  border-radius: 2rem;

  background: var(--surface-container-lowest);

  color: var(--on-surface);

  outline: none;
}

.search-input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(173, 198, 255, 0.15);
}

.add-btn {
  /* //padding: 0.5rem 1.2rem;
  //border-radius: 999px;
  //border: none;
  //background: var(--primary-container);
  //color: white;
  //font-size: 0.85rem;
  //cursor: pointer;
  //transition: 0.2s; */


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
}

.drivers-table {
  width: 100%;
  border-collapse: collapse;
}

.drivers-table thead tr {
  background: var(--surface-container-highest);
  border-bottom: 1px solid var(--outline-variant);
}

.drivers-table th {
  text-align: center;
  font-size: 0.79rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  color: var(--on-surface-variant);
  padding: 0.75rem;
}

.drivers-table tbody tr {
  border-bottom: 1px solid rgba(66, 71, 84, 0.5);
  transition: background 0.2s ease-in-out;
}

.drivers-table tbody tr:hover {
  background: var(--surface-container);
}

.drivers-table td {
  padding: 1rem;
  color: #dae2fd;
  font-size: 0.875rem;
  text-align: center;
}
.badge-available {
  background: rgba(78, 222, 163, 0.15);
  color: #4edea3;
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  font-size: 0.8rem;
}

.badge-unavailable {
  background: rgba(255, 180, 171, 0.15);
  color: #ffb4ab;
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  font-size: 0.8rem;
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
  background: rgba(0,0,0,0.6);
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

.form-group input,
.form-group select {
  padding: 0.6rem 1rem;
  border-radius: 0.5rem;
  border: 1px solid var(--outline-variant);
  background: var(--surface-container-lowest);
  color: var(--on-surface);
  font-size: 0.9rem;
  outline: none;
}

.form-group input:focus,
.form-group select:focus {
  border-color: var(--primary);
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

.submit-btn {
  padding: 0.5rem 1.2rem;
  border-radius: 999px;
  border: none;
  background: var(--primary-container);
  color: white;
  cursor: pointer;
}
</style>