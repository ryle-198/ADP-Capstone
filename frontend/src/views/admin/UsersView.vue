<script setup>
import { ref, computed, onMounted } from 'vue'
import userService from '../../services/userService.js'
import customerService from '../../services/customerService.js'
import driverService from '../../services/driverService.js'

const users = ref([])
const customers = ref([])
const drivers = ref([])
const loading = ref(false)
const error = ref('')
const message = ref('')
const query = ref('')

const roles = ['ADMIN', 'DISPATCHER', 'DRIVER', 'CUSTOMER']

const emptyForm = {
  username: '',
  password: '',
  role: 'CUSTOMER',
  driverId: '',
  customerId: ''
}
const form = ref({ ...emptyForm })

// display helpers (look only)
function isOn(u) {
  return u.enabled ?? u.enable
}

function pretty(s) {
  if (!s) return ''
  const t = String(s).toLowerCase()
  return t.charAt(0).toUpperCase() + t.slice(1)
}

function roleClass(role) {
  if (role === 'ADMIN') return 'badge badge-warning'
  if (role === 'DISPATCHER') return 'badge badge-info'
  return 'badge badge-neutral'
}

function linkedId(u) {
  return u.customer?.customerId || u.driver?.id || ''
}

function shortId(id) {
  return id && id.length > 14 ? id.slice(0, 8) + '…' : id
}

function fmt(t) {
  if (!t) return ''
  const d = Array.isArray(t)
      ? new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0)
      : new Date(t)
  if (isNaN(d)) return String(t)
  return d.toLocaleString('en-ZA', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' })
}

const filtered = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return users.value
  return users.value.filter((u) =>
      [u.username, u.role, linkedId(u)].some((v) => String(v || '').toLowerCase().includes(q)))
})

function fetchUsers() {
  loading.value = true
  error.value = ''
  userService.getAll()
      .then((res) => { users.value = res.data })
      .catch(() => { error.value = 'Could not load users.' })
      .finally(() => { loading.value = false })
}

function fetchLinks() {
  customerService.getAll().then((res) => { customers.value = res.data })
  driverService.getAll().then((res) => { drivers.value = res.data })
}

function createUser() {
  error.value = ''
  message.value = ''

  // only send the link that matches the role
  const payload = {
    username: form.value.username,
    password: form.value.password,
    role: form.value.role,
    driverId: form.value.role === 'DRIVER' ? form.value.driverId : null,
    customerId: form.value.role === 'CUSTOMER' ? form.value.customerId : null
  }

  userService.create(payload)
      .then(() => {
        message.value = `User "${payload.username}" created.`
        form.value = { ...emptyForm }
        fetchUsers()
      })
      .catch((err) => {
        const code = err.response?.status
        if (code === 409) error.value = 'That username is taken, or that driver/customer already has an account.'
        else if (code === 404) error.value = 'The selected driver or customer was not found.'
        else if (code === 400) error.value = err.response?.data?.message || 'Check the form: the password needs at least 8 characters and a driver/customer must be chosen for those roles.'
        else error.value = 'Could not create the user.'
      })
}

function toggleEnabled(user) {
  error.value = ''
  message.value = ''
  const action = isOn(user) ? userService.disable(user.id) : userService.enable(user.id)
  action
      .then(fetchUsers)
      .catch((err) => {
        error.value = err.response?.status === 400
            ? 'You cannot disable your own account.'
            : 'Could not change the account status.'
      })
}

function resetPassword(user) {
  const newPassword = prompt(`New password for ${user.username} (min 8 characters):`)
  if (!newPassword) return
  error.value = ''
  message.value = ''
  userService.resetPassword(user.id, newPassword)
      .then(() => { message.value = `Password reset for ${user.username}.` })
      .catch(() => { error.value = 'Could not reset the password (min 8 characters).' })
}

function deleteUser(user) {
  if (!confirm(`Delete user ${user.username}?`)) return
  error.value = ''
  message.value = ''
  userService.delete(user.id)
      .then(fetchUsers)
      .catch((err) => {
        error.value = err.response?.status === 400
            ? 'You cannot delete your own account.'
            : 'Could not delete the user.'
      })
}

onMounted(() => {
  fetchUsers()
  fetchLinks()
})
</script>

<template>
  <main class="admin-page">
    <div class="page-header">
      <div>
        <h1>USERS</h1>
        <p class="subtitle">Login accounts and the customer or driver each one belongs to</p>
      </div>
    </div>

    <p v-if="message" class="alert-success">{{ message }}</p>
    <p v-if="error" class="alert-error">{{ error }}</p>

    <div class="toolbar">
      <input v-model="query" class="search-input" type="text" placeholder="Search username, role or linked ID" aria-label="Search users" />
      <span class="toolbar-count">{{ filtered.length }} of {{ users.length }}</span>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>Username</th>
          <th>Role</th>
          <th>Linked account</th>
          <th>Status</th>
          <th>Created</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="loading" class="empty-row"><td colspan="6">Loading...</td></tr>
        <tr v-else-if="users.length === 0" class="empty-row"><td colspan="6">No users.</td></tr>
        <tr v-else-if="filtered.length === 0" class="empty-row"><td colspan="6">No users match your search.</td></tr>
        <template v-else>
          <tr v-for="u in filtered" :key="u.id" :class="{ 'row-disabled': !isOn(u) }">
            <td>{{ u.username }}</td>
            <td><span :class="roleClass(u.role)">{{ pretty(u.role) }}</span></td>
            <td>
              <span v-if="linkedId(u)" class="mono-id" :title="linkedId(u)">{{ shortId(linkedId(u)) }}</span>
              <span v-else>-</span>
            </td>
            <td>
              <span :class="['badge', isOn(u) ? 'badge-success' : 'badge-error']">{{ isOn(u) ? 'Enabled' : 'Disabled' }}</span>
            </td>
            <td>{{ fmt(u.createdAt) }}</td>
            <td class="actions">
              <button :class="['btn-sm', isOn(u) ? 'btn-secondary' : 'btn-outline']" @click="toggleEnabled(u)">{{ isOn(u) ? 'Disable' : 'Enable' }}</button>
              <button class="btn-sm btn-secondary" @click="resetPassword(u)">Reset password</button>
              <button class="btn-sm btn-danger" @click="deleteUser(u)">Delete</button>
            </td>
          </tr>
        </template>
        </tbody>
      </table>
    </div>
  </main>
</template>