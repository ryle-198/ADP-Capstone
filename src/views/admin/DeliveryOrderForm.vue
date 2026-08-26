<script setup>
import{ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import deliveryOrderService from '../../services/deliveryOrderService'

const route = useRoute()
const router = useRouter()
const isEdit =!!route.params.id

const form = ref({
    orderId:'',
    customerId:'',
    orderDate: '',
    deliveryDate: '',
    deliveryStatus: 'OrderPlaced',
    paymentStatus: 'PENDING',
    totalCost: 0,
    specialInstructions: ''
})

onMounted(()=>{
    if(isEdit){
        deliveryOrderService.getById(route.params.id).then((res)=>{
            const o = res.data
            form.value = {
                orderId: o.orderId,
                customerId: o.customer?.customerId || '',
                orderDate: o.orderDate,
                deliveryDate: o.deliveryDate,
                deliveryStatus: o.deliveryStatus,
                paymentStatus: o.paymentStatus,
                totalCost: o.totalCost,
                specialInstructions: o.specialInstructions

            }
        })
    }
})

function submit(){
    const payload ={
        orderId: form.value.orderId,
        customer:{customerId: form.value.customerId},
        orderDate: form.value.orderDate,
        deliveryDate: form.value.deliveryDate,
        deliveryStatus: form.value.deliveryStatus,
        paymentStatus: form.value.paymentStatus,
        totalCost: Number(form.value.totalCost),
        specialInstructions: form.value.specialInstructions
    }

    const action = isEdit
        ? deliveryOrderService.update(payload)
        : deliveryOrderService.created(payload)

        action.then(()=> router.push('/orders'))
}
</script>

<<template>
  <main class="form-page">
    <div class="form-card">
      <div class="form-card-accent"></div>

      <div class="form-card-inner">
        <div class="form-header">
          <h1>{{ isEdit ? 'Edit Order' : 'New Order' }}</h1>
          <p v-if="isEdit" class="order-id-label">ORDER ID: #{{ form.orderId }}</p>
        </div>

        <form @submit.prevent="submit" class="order-form">
          <div class="form-grid">
            <div class="form-field">
              <label for="orderId">Order ID</label>
              <input
                id="orderId"
                v-model="form.orderId"
                type="text"
                :readonly="isEdit"
                :class="{ readonly: isEdit }"
                required
              />
            </div>

            <div class="form-field">
              <label for="customerId">Customer ID</label>
              <input id="customerId" v-model="form.customerId" type="text" required />
            </div>

            <div class="form-field">
              <label for="totalCost">Total Cost</label>
              <div class="input-with-prefix">
                <span class="prefix">R</span>
                <input
                  id="totalCost"
                  v-model="form.totalCost"
                  type="number"
                  step="0.01"
                  class="has-prefix"
                  required
                />
              </div>
            </div>

            <div class="form-field">
              <label for="orderDate">Order Date</label>
              <input id="orderDate" v-model="form.orderDate" type="date" required />
            </div>

            <div class="form-field">
              <label for="deliveryDate">Delivery Date</label>
              <input id="deliveryDate" v-model="form.deliveryDate" type="date" required />
            </div>

            <div class="form-field">
              <label for="deliveryStatus">Delivery Status</label>
              <select id="deliveryStatus" v-model="form.deliveryStatus">
                <option>OrderPlaced</option>
                <option>OrderConfirmed</option>
                <option>OrderProcessing</option>
                <option>Shipped</option>
                <option>InTransit</option>
                <option>OutForDelivery</option>
                <option>Delivered</option>
              </select>
            </div>

            <div class="form-field">
              <label for="paymentStatus">Payment Status</label>
              <select id="paymentStatus" v-model="form.paymentStatus">
                <option>PENDING</option>
                <option>COMPLETE</option>
                <option>REFUNDED</option>
                <option>FAILED</option>
                <option>REVOKED</option>
                <option>CANCELLED</option>
              </select>
            </div>
          </div>

          <div class="form-field full-width">
            <label for="specialInstructions">Special Instructions</label>
            <textarea
              id="specialInstructions"
              v-model="form.specialInstructions"
              rows="3"
            ></textarea>
          </div>

          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="cancel">Cancel</button>
            <button type="submit" class="btn-submit">
              {{ isEdit ? 'Update Order' : 'Create Order' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<style scoped>
.form-page { 
  flex: 1;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3rem 1.5rem;
      }

.form-card {
  position: relative;
  width: 100%;
  max-width: 680px;
  background: var(--primary-container);
  border: 1px solid var(--outline);
  border-radius: 8px;
    /* box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06), 0 8px 24px rgba(0, 0, 0, 0.05); */
  overflow: hidden;
}

.form-card-inner {
  position: relative;
  z-index: 1;
  padding: 3rem 2.5rem;
}

.form-header {
  text-align: center;
  margin-bottom: 2rem;
}

.form-header h1 {
  font-size: 2rem;
  line-height: 2.5rem;
  letter-spacing: -0.01em;
  font-weight: 700;
  color: var(--secondary);
  margin-bottom: 0.5rem;
}

.order-id-label {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  letter-spacing: 0.08em;
  font-weight: 700;
  color: var(--on-surface-variant);
}

.order-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.form-field.full-width {
  grid-column: 1 / -1;
}

.form-field label {
  color: var(--grey);
  font-size: 0.875rem;
  font-weight: 500;
}

.form-field input,
.form-field select,
.form-field textarea {
  width: 100%;
  border: 1px solid var(--border-muted);
  border-radius: 2px;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  line-height: 1.5;
  font-family: inherit;
  color: var(--grey);
  background: var(--dark);
  transition: all 0.2s ease;
}

.form-field input:focus,
.form-field select:focus,
.form-field textarea:focus {
  outline: none;
  border: 2px solid var(--primary);
  padding: calc(0.75rem - 1px) calc(1rem - 1px);
}

.form-field input.readonly {
  background: var(--surface-container-low);
  color: var(--on-surface-variant);
  cursor: not-allowed;
}

.input-with-prefix {
  position: relative;
}

.input-with-prefix .prefix {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--on-surface-variant);
  pointer-events: none;
}

.input-with-prefix input.has-prefix {
  padding-left: 2rem;
}

.form-actions {
  display: flex;
  flex-direction: column-reverse;
  gap: 1rem;
  padding-top: 1.5rem;
  margin-top: 0.5rem;
  border-top: 1px solid var(--border-muted);
}

.btn-cancel,
.btn-submit {
  width: 100%;
  padding: 0.75rem 2rem;
  border-radius: 2px;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s ease;
}


/* label { 
    display: flex;
     flex-direction: column;
      gap: 0.3rem; 
      } */
/* button { 
    padding: 0.6rem;
     background: var(--admin-primary);
      color: #fff; 
      border: none; 
      border-radius: 6px; } */

.btn-cancel {
  background: var(--dark);
  border: 2px solid var(--outline);
  color: var(--light);
}

/* .btn-cancel:hover {
  background: var(--secondary);
} */

.btn-submit {
  flex-grow: 1;
  border: none;
  background: var(--admin-primary);
  color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.btn-submit:hover {
  opacity: 0.9;
}

</style>
