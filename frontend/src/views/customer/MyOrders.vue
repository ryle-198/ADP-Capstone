<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import myService from '../../services/myService.js'
import authService from '../../services/authService.js'
import { useAuth } from '../../stores/authStore.js'

const router = useRouter()
const { clearAuth } = useAuth()

const orders = ref([])
const loading = ref(false)
const error = ref('')
const payMessage = ref('')
const payOk = ref(false)

// place order form
const emptyOrder = { deliveryDate: '', pickupAddress: '', deliveryAddress: '', weightKg: '', parcelDescription: '', specialInstructions: '' }
const form = ref({ ...emptyOrder })
const formError = ref('')

// shipments of the selected order
const selectedOrderId = ref('')
const shipments = ref([])
const shipmentsError = ref('')

// track by shipment id
const trackId = ref('')
const tracked = ref(null)
const trackError = ref('')
const history = ref([])

// display helpers (look only, no behaviour)
const today = new Date().toLocaleDateString('en-CA')

// same rule as the server: R50 + R10 per kg
const estimate = computed(() => {
  const w = Number(form.value.weightKg)
  return w > 0 ? 50 + 10 * w : null
})

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

function money(v) {
  return v == null ? '-' : 'R' + Number(v).toFixed(2)
}

function fmtTime(t) {
  if (!t) return ''
  const d = Array.isArray(t)
      ? new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0)
      : new Date(t)
  if (isNaN(d)) return String(t)
  return d.toLocaleString('en-ZA', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' })
}

function fetchOrders() {
  loading.value = true
  error.value = ''
  myService.getMyOrders()
      .then((res) => { orders.value = res.data })
      .catch(() => { error.value = 'Could not load your orders.' })
      .finally(() => { loading.value = false })
}

function placeOrder() {
  formError.value = ''
  myService.placeOrder({
    deliveryDate: form.value.deliveryDate,
    pickupAddress: form.value.pickupAddress,
    deliveryAddress: form.value.deliveryAddress,
    weightKg: Number(form.value.weightKg),
    parcelDescription: form.value.parcelDescription,
    specialInstructions: form.value.specialInstructions
  })
      .then(() => {
        form.value = { ...emptyOrder }
        fetchOrders()
      })
      .catch((err) => {
        if (err.response?.status === 400) {
          formError.value = 'Please fill in both addresses, a weight above 0 and a delivery date of today or later.'
        } else {
          formError.value = 'Could not place the order.'
        }
      })
}

function cancelOrder(order) {
  payMessage.value = ''
  if (!confirm(`Cancel order ${order.orderId}?`)) return
  myService.cancelOrder(order.orderId)
      .then(() => {
        payOk.value = true
        payMessage.value = `Order ${order.orderId} cancelled.`
        fetchOrders()
        if (selectedOrderId.value === order.orderId) viewShipments(order.orderId)
      })
      .catch((err) => {
        payOk.value = false
        payMessage.value = err.response?.status === 409
            ? 'This order can no longer be cancelled (it is already on its way or cancelled).'
            : 'Could not cancel the order.'
      })
}

function payOrder(order) {
  payMessage.value = ''
  const amount = order.totalCost?.toFixed(2)
  if (!confirm(`Pay R${amount} for order ${order.orderId}? (This is a simulated payment, no money is taken.)`)) return
  myService.payOrder(order.orderId)
      .then(() => {
        payOk.value = true
        payMessage.value = `Order ${order.orderId} paid.`
        fetchOrders()
      })
      .catch((err) => {
        payOk.value = false
        payMessage.value = err.response?.status === 409
            ? 'This order cannot be paid (already paid or no price yet).'
            : 'Could not complete the payment.'
      })
}

function viewShipments(orderId) {
  selectedOrderId.value = orderId
  shipments.value = []
  shipmentsError.value = ''
  myService.getMyOrderShipments(orderId)
      .then((res) => { shipments.value = res.data })
      .catch(() => { shipmentsError.value = 'Could not load shipments for this order.' })
}

function track(id) {
  const shipmentId = (id || trackId.value).trim()
  trackId.value = shipmentId
  tracked.value = null
  history.value = []
  trackError.value = ''

  if (id) window.scrollTo({ top: 0, behavior: 'smooth' })
  myService.trackShipment(shipmentId)
      .then((res) => {
        tracked.value = res.data
        return myService.getShipmentHistory(shipmentId)
      })
      .then((res) => { history.value = res.data })
      .catch((err) => {
        trackError.value = err.response?.status === 404
            ? 'Shipment not found.'
            : 'Could not track this shipment.'
      })
}

function logout() {
  authService.logout().finally(() => {
    clearAuth()
    router.push('/login')
  })
}

onMounted(fetchOrders)
</script>

<template>
  <div class="portal">
    <header class="portal-bar">
      <div class="portal-bar-inner">
        <div class="portal-brand">
          <RouterLink to="/home" class="portal-logo">SWIFT</RouterLink>
          <span class="portal-bar-title">Customer portal</span>
        </div>
        <div style="display: flex; gap: 0.5rem; align-items: center">
          <RouterLink to="/my/profile" class="btn btn-default btn-sm">My profile</RouterLink>
          <button class="btn btn-default btn-sm" @click="logout">Logout</button>
        </div>
      </div>
    </header>

    <div class="portal-main">
      <div class="portal-head">
        <h1 style = "color: #00a572">My Orders</h1>
        <p>Place a delivery, pay for it and follow it until it arrives.</p>
      </div>

      <div class="portal-grid">
        <!-- Place an order -->
        <section class="panel">
          <div class="panel-head"><h2>Place an order</h2></div>
          <form @submit.prevent="placeOrder">
            <div class="panel-body">
              <p v-if="formError" class="alert alert-error">{{ formError }}</p>
              <div class="field-grid">
                <div class="field">
                  <label for="o-pickup">Pickup address</label>
                  <input id="o-pickup" v-model="form.pickupAddress" type="text" required />
                </div>
                <div class="field">
                  <label for="o-delivery">Delivery address</label>
                  <input id="o-delivery" v-model="form.deliveryAddress" type="text" required />
                </div>
                <div class="field">
                  <label for="o-weight">Parcel weight (kg)</label>
                  <input id="o-weight" v-model="form.weightKg" type="number" min="0.1" step="0.1" required />
                </div>
                <div class="field">
                  <label for="o-date">Delivery date</label>
                  <input id="o-date" v-model="form.deliveryDate" type="date" :min="today" required />
                </div>
                <div class="field wide">
                  <label for="o-desc">Parcel description</label>
                  <input id="o-desc" v-model="form.parcelDescription" type="text" />
                </div>
                <div class="field wide">
                  <label for="o-notes">Special instructions</label>
                  <textarea id="o-notes" v-model="form.specialInstructions" rows="3"></textarea>
                </div>
              </div>
              <div class="form-actions">
                <span class="field-hint">
                  <template v-if="estimate !== null">Estimated cost: <strong>{{ money(estimate) }}</strong> (R50 base + R10 per kg)</template>
                  <template v-else>Price is R50 base + R10 per kg.</template>
                </span>
                <button type="submit" class="btn btn-primary">Place order</button>
              </div>
            </div>
          </form>
        </section>

        <!-- Track a shipment -->
        <section class="panel">
          <div class="panel-head"><h2>Track a shipment</h2></div>
          <div class="panel-body">
            <form class="input-group" @submit.prevent="track()">
              <input v-model="trackId" type="text" placeholder="Shipment ID" aria-label="Shipment ID" required />
              <button type="submit" class="btn btn-primary">Track</button>
            </form>
            <p class="field-hint" style="margin: 0.5rem 0 0">
              You can also press Track next to a shipment in the list below.
            </p>

            <p v-if="trackError" class="alert alert-error" style="margin-top: 0.9rem">{{ trackError }}</p>

            <div v-if="tracked">
              <h3 class="subhead">Shipment</h3>
              <dl class="kv">
                <dt>Shipment</dt><dd class="mono">{{ tracked.shipmentId }}</dd>
                <dt>Order</dt><dd class="mono">{{ tracked.orderId }}</dd>
                <dt>Status</dt><dd><span :class="tagClass(tracked.status)">{{ pretty(tracked.status) }}</span></dd>
                <dt>From</dt><dd>{{ tracked.originAddress }}</dd>
                <dt>To</dt><dd>{{ tracked.destinationAddress }}</dd>
                <dt>Fragile</dt><dd>{{ tracked.fragile ? 'Yes' : 'No' }}</dd>
              </dl>

              <h3 class="subhead">History</h3>
              <p v-if="history.length === 0" class="muted">No history yet.</p>
              <ol v-else class="timeline">
                <li v-for="(e, i) in history" :key="i">
                  <div class="tl-top">
                    <span :class="tagClass(e.status)">{{ pretty(e.status) }}</span>
                    <span class="tl-time">{{ fmtTime(e.eventTime) }}</span>
                  </div>
                  <div v-if="e.location" class="tl-loc">{{ e.location }}</div>
                  <div v-if="e.note" class="tl-note">{{ e.note }}</div>
                </li>
              </ol>
            </div>
          </div>
        </section>
      </div>

      <!-- Orders -->
      <section class="panel">
        <div class="panel-head"><h2>My orders</h2></div>
        <div v-if="error || payMessage" class="panel-msg">
          <p v-if="error" class="alert alert-error">{{ error }}</p>
          <p v-if="payMessage" :class="['alert', payOk ? 'alert-success' : 'alert-error']">{{ payMessage }}</p>
        </div>
        <div class="table-scroll">
          <table class="wide-table">
            <thead>
            <tr>
              <th>Order ID</th>
              <th>Order Date</th>
              <th>Delivery Date</th>
              <th>Pickup</th>
              <th>Delivery</th>
              <th>Weight (kg)</th>
              <th>Status</th>
              <th>Payment</th>
              <th>Total</th>
              <th>Instructions</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="loading"><td class="empty" colspan="11">Loading...</td></tr>
            <tr v-else-if="orders.length === 0"><td class="empty" colspan="11">You have no orders yet.</td></tr>
            <template v-else>
              <tr v-for="order in orders" :key="order.orderId" :class="{ selected: order.orderId === selectedOrderId }">
                <td class="mono">{{ order.orderId }}</td>
                <td>{{ order.orderDate }}</td>
                <td>{{ order.deliveryDate }}</td>
                <td>{{ order.pickupAddress }}</td>
                <td>{{ order.deliveryAddress }}</td>
                <td class="num">{{ order.weightKg }}</td>
                <td><span :class="tagClass(order.deliveryStatus)">{{ pretty(order.deliveryStatus) }}</span></td>
                <td><span :class="tagClass(order.paymentStatus)">{{ pretty(order.paymentStatus) }}</span></td>
                <td class="num">{{ money(order.totalCost) }}</td>
                <td>{{ order.specialInstructions }}</td>
                <td class="actions">
                  <button class="btn btn-default btn-sm" @click="viewShipments(order.orderId)">Shipments</button>
                  <button v-if="order.paymentStatus === 'PENDING' && order.totalCost > 0" class="btn btn-primary btn-sm" @click="payOrder(order)">Pay</button>
                  <button v-if="!['CANCELLED', 'REFUNDED'].includes(order.paymentStatus)" class="btn btn-default btn-sm" @click="cancelOrder(order)">Cancel</button>
                  <RouterLink v-if="order.paymentStatus === 'COMPLETE'" :to="`/my/orders/${order.orderId}/receipt`" class="btn btn-default btn-sm">Receipt</RouterLink>
                </td>
              </tr>
            </template>
            </tbody>
          </table>
        </div>
      </section>

      <!-- Shipments for the selected order -->
      <section v-if="selectedOrderId" class="panel">
        <div class="panel-head">
          <h2>Shipments for order <span class="mono">{{ selectedOrderId }}</span></h2>
        </div>
        <div v-if="shipmentsError" class="panel-msg"><p class="alert alert-error">{{ shipmentsError }}</p></div>
        <p v-else-if="shipments.length === 0" class="panel-body muted" style="margin: 0">No shipments have been created for this order yet.</p>
        <div v-else class="table-scroll">
          <table>
            <thead>
            <tr>
              <th>Shipment ID</th>
              <th>Status</th>
              <th>Origin</th>
              <th>Destination</th>
              <th>Fragile</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="s in shipments" :key="s.shipmentId">
              <td class="mono">{{ s.shipmentId }}</td>
              <td><span :class="tagClass(s.status)">{{ pretty(s.status) }}</span></td>
              <td>{{ s.originAddress }}</td>
              <td>{{ s.destinationAddress }}</td>
              <td>
                <span v-if="s.fragile" class="tag tag-warn">Fragile</span>
                <span v-else class="muted">No</span>
              </td>
              <td class="actions"><button class="btn btn-default btn-sm" @click="track(s.shipmentId)">Track</button></td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>
  </div>
</template>