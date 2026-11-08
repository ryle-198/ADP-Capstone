<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import myService from '../../services/myService.js'
import authService from '../../services/authService.js'
import { useAuth } from '../../stores/authStore.js'

const router = useRouter()
const { clearAuth } = useAuth()

const routes = ref([])
const loading = ref(false)
const error = ref('')

const selectedRouteId = ref('')
const shipments = ref([])
const shipmentsError = ref('')


const picked = ref({})

const messages = ref({})

const locations = ref({})
const notes = ref({})


const driverStatuses = [
  'PICKED_UP',
  'IN_TRANSIT',
  'AT_HUB',
  'OUT_FOR_DELIVERY',
  'DELIVERED',
  'DELAYED',
  'FAILED_DELIVERY'
]


function pretty(s) {
  if (!s) return ''
  const t = String(s).replace(/_/g, ' ').replace(/([a-z])([A-Z])/g, '$1 $2').toLowerCase()
  return t.charAt(0).toUpperCase() + t.slice(1)
}

const TAGS = {
  delivered: 'tag-good', complete: 'tag-good', returned: 'tag-good',
  shipped: 'tag-info', intransit: 'tag-info', outfordelivery: 'tag-info', pickedup: 'tag-info',
  athub: 'tag-info', returninitiated: 'tag-info', returnintransit: 'tag-info',
  pending: 'tag-warn', delayed: 'tag-warn', onhold: 'tag-warn',
  failed: 'tag-bad', faileddelivery: 'tag-bad', lost: 'tag-bad', damaged: 'tag-bad',
  cancelled: 'tag-bad', revoked: 'tag-bad'
}

function tagClass(s) {
  const key = String(s || '').toLowerCase().replace(/[^a-z]/g, '')
  return 'tag ' + (TAGS[key] || 'tag-neutral')
}

function fetchRoutes() {
  loading.value = true
  error.value = ''
  myService.getMyRoutes()
      .then((res) => { routes.value = res.data })
      .catch(() => { error.value = 'Could not load your routes.' })
      .finally(() => { loading.value = false })
}

function viewShipments(routeId) {
  selectedRouteId.value = routeId
  loadShipments()
}

function loadShipments() {
  shipments.value = []
  shipmentsError.value = ''
  messages.value = {}
  myService.getMyRouteShipments(selectedRouteId.value)
      .then((res) => {
        shipments.value = res.data
        const start = {}
        res.data.forEach((s) => { start[s.shipmentId] = s.status })
        picked.value = start
      })
      .catch(() => { shipmentsError.value = 'Could not load shipments for this route.' })
}

function updateStatus(shipment) {
  const status = picked.value[shipment.shipmentId]
  messages.value[shipment.shipmentId] = ''
  myService.updateShipmentStatus(
      shipment.shipmentId, status,
      locations.value[shipment.shipmentId], notes.value[shipment.shipmentId])
      .then((res) => {
        locations.value[shipment.shipmentId] = ''
        notes.value[shipment.shipmentId] = ''
        // replace the row with the updated shipment
        const i = shipments.value.findIndex((s) => s.shipmentId === shipment.shipmentId)
        if (i !== -1) shipments.value[i] = res.data
        messages.value[shipment.shipmentId] = 'Updated.'
      })
      .catch((err) => {
        const code = err.response?.status
        if (code === 409) messages.value[shipment.shipmentId] = 'That change is not allowed from the current status.'
        else if (code === 400) messages.value[shipment.shipmentId] = 'That status is not allowed for drivers.'
        else messages.value[shipment.shipmentId] = 'Could not update the status.'
      })
}

function logout() {
  authService.logout().finally(() => {
    clearAuth()
    router.push('/portal-x7q2/login')
  })
}

onMounted(fetchRoutes)
</script>

<template>
  <div class="portal">
    <header class="portal-bar">
      <div class="portal-bar-inner">
        <div class="portal-brand">
          <RouterLink to="/home" class="portal-logo">SWIFT</RouterLink>
          <span class="portal-bar-title">Driver portal</span>
        </div>
        <button class="btn btn-default btn-sm" @click="logout">Logout</button>
      </div>
    </header>

    <div class="portal-main">
      <div class="portal-head">
        <h1 style = "color: #00a572">My Routes</h1>
        <p>Your assigned routes and the shipments on them. Update a shipment as it moves.</p>
      </div>

      <!-- Routes -->
      <section class="panel">
        <div class="panel-head"><h2>Assigned routes</h2></div>
        <div v-if="error" class="panel-msg"><p class="alert alert-error">{{ error }}</p></div>
        <div class="table-scroll">
          <table class="mid-table">
            <thead>
            <tr>
              <th>Route ID</th>
              <th>Start</th>
              <th>End</th>
              <th>Distance (km)</th>
              <th>Est. time (min)</th>
              <th>Vehicle</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="loading"><td class="empty" colspan="7">Loading...</td></tr>
            <tr v-else-if="routes.length === 0"><td class="empty" colspan="7">No routes have been assigned to you.</td></tr>
            <template v-else>
              <tr v-for="r in routes" :key="r.routeId" :class="{ selected: r.routeId === selectedRouteId }">
                <td class="mono">{{ r.routeId }}</td>
                <td>{{ r.startLocation }}</td>
                <td>{{ r.endLocation }}</td>
                <td class="num">{{ r.distance }}</td>
                <td class="num">{{ r.estimateTime }}</td>
                <td class="mono">{{ r.vehicle?.numberPlate }}</td>
                <td class="actions"><button class="btn btn-default btn-sm" @click="viewShipments(r.routeId)">Shipments</button></td>
              </tr>
            </template>
            </tbody>
          </table>
        </div>
      </section>

      <!-- Shipments on the selected route -->
      <section v-if="selectedRouteId" class="panel">
        <div class="panel-head">
          <h2>Shipments on route <span class="mono">{{ selectedRouteId }}</span></h2>
          <button class="btn btn-default btn-sm" @click="loadShipments">Refresh</button>
        </div>
        <div v-if="shipmentsError" class="panel-msg"><p class="alert alert-error">{{ shipmentsError }}</p></div>
        <p v-else-if="shipments.length === 0" class="panel-body muted" style="margin: 0">No shipments on this route.</p>
        <div v-else class="job-list">
          <article v-for="s in shipments" :key="s.shipmentId" class="job">
            <div class="job-top">
              <div class="job-ids">
                <span class="mono">{{ s.shipmentId }}</span>
                <span class="muted">Order <span class="mono">{{ s.orderId }}</span></span>
              </div>
              <div class="job-tags">
                <span v-if="s.fragile" class="tag tag-warn">Fragile</span>
                <span :class="tagClass(s.status)">{{ pretty(s.status) }}</span>
              </div>
            </div>

            <dl class="job-grid">
              <div><dt>From</dt><dd>{{ s.originAddress }}</dd></div>
              <div><dt>To</dt><dd>{{ s.destinationAddress }}</dd></div>
              <div><dt>Customer</dt><dd>{{ s.customerName }}</dd></div>
              <div>
                <dt>Phone</dt>
                <dd>
                  <a v-if="s.customerPhone" :href="'tel:' + s.customerPhone">{{ s.customerPhone }}</a>
                  <span v-else class="muted">-</span>
                </dd>
              </div>
              <div v-if="s.specialInstructions" class="wide"><dt>Instructions</dt><dd>{{ s.specialInstructions }}</dd></div>
            </dl>

            <div class="job-update">
              <div class="field">
                <label :for="'st-' + s.shipmentId">New status</label>
                <select :id="'st-' + s.shipmentId" v-model="picked[s.shipmentId]">
                  <option v-for="st in driverStatuses" :key="st" :value="st">{{ pretty(st) }}</option>
                </select>
              </div>
              <div class="field">
                <label :for="'loc-' + s.shipmentId">Location</label>
                <input :id="'loc-' + s.shipmentId" v-model="locations[s.shipmentId]" type="text" placeholder="Optional" />
              </div>
              <div class="field">
                <label :for="'note-' + s.shipmentId">Note</label>
                <input :id="'note-' + s.shipmentId" v-model="notes[s.shipmentId]" type="text" placeholder="Optional" />
              </div>
              <button class="btn btn-primary" @click="updateStatus(s)">Update</button>
            </div>
            <p v-if="messages[s.shipmentId]" :class="['alert', messages[s.shipmentId] === 'Updated.' ? 'alert-success' : 'alert-error']">
              {{ messages[s.shipmentId] }}
            </p>
          </article>
        </div>
      </section>
    </div>
  </div>
</template>