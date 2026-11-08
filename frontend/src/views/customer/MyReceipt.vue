<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import myService from '../../services/myService.js'

const route = useRoute()
const order = ref(null)
const profile = ref(null)
const error = ref('')

const BASE_FEE = 50
const RATE_PER_KG = 10

function money(v) {
  return 'R' + Number(v || 0).toFixed(2)
}

// only show the base + per-kg lines when they add up to the real total
const breakdown = computed(() => {
  if (!order.value) return null
  const weight = Number(order.value.weightKg)
  const perKg = RATE_PER_KG * weight
  if (weight > 0 && Math.abs(BASE_FEE + perKg - order.value.totalCost) < 0.01) {
    return { base: BASE_FEE, perKg, weight }
  }
  return null
})

function printPage() {
  window.print()
}

const refunded = computed(() => order.value?.paymentStatus === 'REFUNDED')

onMounted(() => {
  Promise.all([myService.getMyOrder(route.params.id), myService.getProfile()])
      .then(([o, p]) => {
        const paid = ['COMPLETE', 'REFUNDED'].includes(o.data.paymentStatus)
        if (!paid) {
          error.value = 'There is no receipt for this order because it has not been paid.'
          return
        }
        order.value = o.data
        profile.value = p.data
      })
      .catch((err) => {
        error.value = err.response?.status === 404
            ? 'Order not found.'
            : 'Could not load the receipt.'
      })
})
</script>

<template>
  <div class="portal">
    <header class="portal-bar no-print">
      <div class="portal-bar-inner">
        <div class="portal-brand">
          <RouterLink to="/home" class="portal-logo">SWIFT</RouterLink>
          <span class="portal-bar-title">Customer portal</span>
        </div>
        <div class="bar-actions">
          <RouterLink to="/my/orders" class="btn btn-default btn-sm">Back to my orders</RouterLink>
          <button v-if="order" type="button" class="btn btn-primary btn-sm" @click="printPage">Print receipt</button>
        </div>
      </div>
    </header>

    <div class="portal-main receipt-main">
      <p v-if="error" class="alert alert-error">{{ error }}</p>

      <article v-if="order" class="panel receipt">
        <header class="receipt-head">
          <div>
            <div class="receipt-brand">SWIFT</div>
            <div class="receipt-title">Payment receipt</div>
          </div>
          <span :class="['tag', refunded ? 'tag-warn' : 'tag-good']">{{ refunded ? 'Refunded' : 'Paid' }}</span>
        </header>

        <div class="receipt-body">
          <dl class="kv receipt-meta">
            <dt>Order</dt><dd class="mono">{{ order.orderId }}</dd>
            <dt>Order date</dt><dd>{{ order.orderDate }}</dd>
            <dt>Delivery date</dt><dd>{{ order.deliveryDate }}</dd>
            <dt>Payment</dt><dd>{{ refunded ? 'Refunded' : 'Paid' }}</dd>
          </dl>

          <div class="receipt-cols">
            <section v-if="profile">
              <h2 class="subhead">Billed to</h2>
              <p class="line strong">{{ profile.customerName }}</p>
              <p v-if="profile.address" class="line">{{ profile.address }}</p>
              <p class="line">{{ profile.email }}<span v-if="profile.phoneNumber"> &middot; {{ profile.phoneNumber }}</span></p>
            </section>

            <section>
              <h2 class="subhead">Delivery</h2>
              <p class="line"><span class="lbl">From</span>{{ order.pickupAddress }}</p>
              <p class="line"><span class="lbl">To</span>{{ order.deliveryAddress }}</p>
              <p v-if="order.parcelDescription" class="line"><span class="lbl">Parcel</span>{{ order.parcelDescription }}</p>
              <p class="line"><span class="lbl">Weight</span>{{ order.weightKg }} kg</p>
            </section>
          </div>

          <table class="receipt-lines">
            <thead><tr><th>Description</th><th class="num">Amount</th></tr></thead>
            <tbody>
            <template v-if="breakdown">
              <tr><td>Base fee</td><td class="num">{{ money(breakdown.base) }}</td></tr>
              <tr><td>Weight charge ({{ breakdown.weight }} kg &times; {{ money(10) }})</td><td class="num">{{ money(breakdown.perKg) }}</td></tr>
            </template>
            <tr v-else><td>Delivery charge</td><td class="num">{{ money(order.totalCost) }}</td></tr>
            </tbody>
            <tfoot>
            <tr><th>Total {{ refunded ? 'refunded' : 'paid' }}</th><th class="num">{{ money(order.totalCost) }}</th></tr>
            </tfoot>
          </table>

          <p class="receipt-note">Simulated payment: no real money was taken. Keep this receipt for your records.</p>
        </div>
      </article>
    </div>
  </div>
</template>

<style scoped>
.receipt-main {
  max-width: 760px;
}

.bar-actions {
  display: flex;
  gap: 0.5rem;
}

.receipt-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.25rem;
  background: var(--p-head);
  border-bottom: 2px solid var(--p-ink);
  border-radius: 3px 3px 0 0;
}

.receipt-brand {
  color: var(--p-accent);
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.01em;
  line-height: 1.1;
}

.receipt-title {
  margin-top: 0.15rem;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  color: var(--p-soft);
}

.receipt-body {
  padding: 1.25rem;
}

.receipt-meta {
  grid-template-columns: 9rem minmax(0, 1fr);
  margin-bottom: 0.5rem;
}

.receipt-cols {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0 2rem;
}

.receipt-cols .subhead {
  margin-top: 1rem;
}

.line {
  margin: 0.2rem 0;
}

.line.strong {
  font-weight: 600;
}

.lbl {
  display: inline-block;
  min-width: 3.5rem;
  color: var(--p-soft);
  font-weight: 600;
}

.receipt .receipt-lines {
  margin-top: 1.5rem;
  border: 1px solid var(--p-line);
}

.receipt .receipt-lines th.num,
.receipt .receipt-lines td.num {
  text-align: right;
  white-space: nowrap;
  font-variant-numeric: tabular-nums;
}

.receipt .receipt-lines tbody tr,
.receipt .receipt-lines tbody tr:hover {
  background: transparent;
}

.receipt .receipt-lines tfoot th {
  padding: 0.75rem 0.8rem;
  background: var(--p-head);
  border-top: 2px solid var(--p-ink);
  border-bottom: none;
  font-size: 13px;
  letter-spacing: 0.04em;
  color: var(--p-ink);
}

.receipt .receipt-lines tfoot th.num {
  font-size: 16px;
}

.receipt-note {
  margin: 1.25rem 0 0;
  font-size: 12.5px;
  color: var(--p-soft);
}

@media (max-width: 600px) {
  .receipt-cols {
    grid-template-columns: 1fr;
  }

  .receipt-meta {
    grid-template-columns: 6.5rem minmax(0, 1fr);
  }

  .bar-actions .btn {
    padding-left: 0.6rem;
    padding-right: 0.6rem;
  }
}

@media print {
  .no-print {
    display: none !important;
  }

  .portal {
    background: #fff;
    min-height: 0;
  }

  .receipt-main {
    max-width: none;
    padding: 0;
  }

  .receipt {
    border: none;
    box-shadow: none;
  }

  .receipt-head {
    background: #fff;
    padding-left: 0;
    padding-right: 0;
  }

  .receipt-body {
    padding-left: 0;
    padding-right: 0;
  }
}
</style>