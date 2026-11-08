<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../../services/api.js'

const stats = ref(null)
const loading = ref(false)
const error = ref('')

function load() {
  loading.value = true
  error.value = ''
  api.get('/admin/stats')
      .then((res) => { stats.value = res.data })
      .catch(() => { error.value = 'Could not load the dashboard.' })
      .finally(() => { loading.value = false })
}

function money(n) {
  return 'R' + Number(n || 0).toFixed(2)
}

// To help with the format of enum e.g OrderPlaced then becomes Order Placed
function pretty(s) {
  if (!s) return ''
  const t = String(s).replace(/_/g, ' ').replace(/([a-z])([A-Z])/g, '$1 $2').toLowerCase()
  return t.charAt(0).toUpperCase() + t.slice(1)
}

// turns { STATUS: count } into a list in workflow order, hiding statuses with no records
function rows(map, order) {
  return Object.entries(map || {})
      .map(([key, n]) => ({ key, label: pretty(key), count: Number(n || 0) }))
      .filter((r) => r.count > 0)
      .sort((a, b) => {
        const ia = order.indexOf(a.key)
        const ib = order.indexOf(b.key)
        return (ia < 0 ? 99 : ia) - (ib < 0 ? 99 : ib)
      })
}

const ORDER_STATUS = ['OrderPlaced', 'OrderConfirmed', 'OrderProcessing', 'Shipped', 'InTransit', 'OutForDelivery', 'Delivered']
const PAYMENT = ['PENDING', 'COMPLETE', 'REFUNDED', 'FAILED', 'REVOKED', 'CANCELLED']
const SHIPMENT = [
  'CREATED', 'READY_FOR_PICKUP', 'PICKED_UP', 'IN_TRANSIT', 'AT_HUB', 'OUT_FOR_DELIVERY', 'DELIVERED',
  'DELAYED', 'ON_HOLD', 'FAILED_DELIVERY', 'RETURN_INITIATED', 'RETURN_IN_TRANSIT', 'RETURNED',
  'CANCELLED', 'LOST', 'DAMAGED'
]
const TOTAL_ORDER = ['customers', 'orders', 'shipments', 'routes', 'drivers', 'vehicles']

// stats.totals
const totals = computed(() => {
  const t = stats.value?.totals || {}
  const keys = [...TOTAL_ORDER.filter((k) => k in t), ...Object.keys(t).filter((k) => !TOTAL_ORDER.includes(k))]
  return keys.map((k) => ({ key: k, label: pretty(k), value: t[k] }))
})

// stats.revenuePaid and stats.revenueOutstanding
const revenue = computed(() => {
  const paid = Number(stats.value?.revenuePaid || 0)
  const out = Number(stats.value?.revenueOutstanding || 0)
  const total = paid + out
  return { paid, out, total, paidPct: total ? (paid / total) * 100 : 0, outPct: total ? (out / total) * 100 : 0 }
})

// stats.driversAvailable / driversUnavailable and vehiclesAvailable / vehiclesNotAvailable
function fleet(label, avail, not) {
  const a = Number(avail || 0)
  const total = a + Number(not || 0)
  return { label, avail: a, total, pct: total ? (a / total) * 100 : 0 }
}

const fleetRows = computed(() => [
  fleet('Drivers', stats.value?.driversAvailable, stats.value?.driversUnavailable),
  fleet('Vehicles', stats.value?.vehiclesAvailable, stats.value?.vehiclesNotAvailable)
])

// stats.ordersByStatus, stats.ordersByPayment, stats.shipmentsByStatus
const orderRows = computed(() => rows(stats.value?.ordersByStatus, ORDER_STATUS))
const paymentRows = computed(() => rows(stats.value?.ordersByPayment, PAYMENT))
const shipmentRows = computed(() => rows(stats.value?.shipmentsByStatus, SHIPMENT))

onMounted(load)
</script>

<template>
  <main class="admin-page">
    <div class="page-header">
      <div>
        <h1>DASHBOARD</h1>
        <p class="subtitle">Overview of the delivery system</p>
      </div>
      <button class="btn-primary" @click="load" :disabled="loading">{{ loading ? 'Loading...' : 'Refresh' }}</button>
    </div>

    <p v-if="error" class="alert-error">{{ error }}</p>
    <p v-else-if="!stats && loading" class="muted-text">Loading...</p>

    <div v-if="stats">
      <!-- Record counts -->
      <div class="stat-strip">
        <div v-for="t in totals" :key="t.key" class="stat">
          <div class="stat-label">{{ t.label }}</div>
          <div class="stat-value">{{ t.value }}</div>
        </div>
      </div>

      <!-- Revenue and fleet -->
      <div class="dash-grid two">
        <section class="panel">
          <div class="panel-head"><h2>Revenue</h2></div>
          <div class="panel-body">
            <div class="figures">
              <div>
                <div class="figure-label">Paid</div>
                <div class="figure-value paid">{{ money(revenue.paid) }}</div>
              </div>
              <div>
                <div class="figure-label">Outstanding (unpaid orders)</div>
                <div class="figure-value outstanding">{{ money(revenue.out) }}</div>
              </div>
            </div>
            <div class="meter" role="img" aria-label="Paid compared with outstanding revenue">
              <span class="seg-paid" :style="{ width: revenue.paidPct + '%' }"></span>
              <span class="seg-out" :style="{ width: revenue.outPct + '%' }"></span>
            </div>
          </div>
        </section>

        <section class="panel">
          <div class="panel-head"><h2>Fleet</h2></div>
          <div class="panel-body">
            <div v-for="f in fleetRows" :key="f.label" class="fleet-row">
              <div class="fleet-top">
                <span>{{ f.label }}</span>
                <span class="num">{{ f.avail }} of {{ f.total }} available</span>
              </div>
              <div class="meter"><span class="seg-avail" :style="{ width: f.pct + '%' }"></span></div>
            </div>
          </div>
        </section>
      </div>

      <!-- Breakdowns -->
      <div class="dash-grid three">
        <section class="panel">
          <div class="panel-head"><h2>Orders by status</h2></div>
          <p v-if="orderRows.length === 0" class="panel-empty">No orders yet.</p>
          <ul v-else class="count-list">
            <li v-for="r in orderRows" :key="r.key"><span>{{ r.label }}</span><span class="n">{{ r.count }}</span></li>
          </ul>
        </section>

        <section class="panel">
          <div class="panel-head"><h2>Orders by payment</h2></div>
          <p v-if="paymentRows.length === 0" class="panel-empty">No orders yet.</p>
          <ul v-else class="count-list">
            <li v-for="r in paymentRows" :key="r.key"><span>{{ r.label }}</span><span class="n">{{ r.count }}</span></li>
          </ul>
        </section>

        <section class="panel">
          <div class="panel-head"><h2>Shipments by status</h2></div>
          <p v-if="shipmentRows.length === 0" class="panel-empty">No shipments yet.</p>
          <ul v-else class="count-list">
            <li v-for="r in shipmentRows" :key="r.key"><span>{{ r.label }}</span><span class="n">{{ r.count }}</span></li>
          </ul>
          <div class="panel-foot">
            <span>Not yet on a route</span>
            <span :class="['badge', stats.shipmentsWithoutRoute > 0 ? 'badge-warning' : 'badge-success']">{{ stats.shipmentsWithoutRoute }}</span>
          </div>
        </section>
      </div>
    </div>
  </main>
</template>