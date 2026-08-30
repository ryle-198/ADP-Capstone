<script setup>
import { ref, onMounted, computed } from 'vue'
import customerService from '../services/customerService'

const customers = ref([])
const loading = ref(false)
const search = ref('')
const showForm = ref(false)

const newCustomer = ref({
    customerId: '',
    customerName: '',
    phoneNumber: '',
    email: '',
    address: ''
})


// Get customers
function fetchCustomers() {
    loading.value = true

    customerService.getAll()
        .then(response => {
            customers.value = response.data
        })
        .catch(error => {
            console.error('Error loading customers:', error)
        })
        .finally(() => {
            loading.value = false
        })
}


// Search customers
const filteredCustomers = computed(() => {
    const value = search.value.toLowerCase()

    if (!value) {
        return customers.value
    }

    return customers.value.filter(customer =>
        customer.customerId?.toLowerCase().includes(value) ||
        customer.customerName?.toLowerCase().includes(value) ||
        customer.phoneNumber?.toLowerCase().includes(value) ||
        customer.email?.toLowerCase().includes(value)
    )
})


// OPEN ADD CUSTOMER FORM
function openAddCustomer() {
    showForm.value = true
}


// CLOSE ADD CUSTOMER FORM
function closeForm() {
    showForm.value = false
}


// SAVE CUSTOMER
function saveCustomer() {

    customerService.create(newCustomer.value)
        .then(() => {
            alert('Customer created successfully!')

            showForm.value = false

            newCustomer.value = {
                customerId: '',
                customerName: '',
                phoneNumber: '',
                email: '',
                address: ''
            }

            fetchCustomers()
        })
        .catch(error => {
            console.error('Error creating customer:', error)
            alert('Failed to create customer.')
        })
}


// DELETE CUSTOMER
function deleteCustomer(id) {

    if (!confirm('Are you sure you want to delete this customer?')) {
        return
    }

    customerService.delete(id)
        .then(() => {
            alert('Customer deleted successfully!')
            fetchCustomers()
        })
        .catch(error => {
            console.error('Error deleting customer:', error)
        })
}


onMounted(() => {
    fetchCustomers()
})
</script>


<template>

    <main class="admin-page">

        <!-- HEADER -->

        <div class="page-header">

            <div>
                <h1>CUSTOMERS</h1>

                <p class="subtitle">
                    Manage Customer Information
                </p>
            </div>

            <!-- ADD CUSTOMER BUTTON -->

            <button
                class="add-btn"
                @click="openAddCustomer"
            >
                 Add Customer
            </button>

        </div>


        <!-- ADD CUSTOMER FORM -->

        <div
            v-if="showForm"
            class="customer-form"
        >

            <h2>Add Customer</h2>

            <div class="form-grid">

                <div class="form-group">
                    <label>Customer ID</label>

                    <input
                        v-model="newCustomer.customerId"
                        type="text"
                        placeholder="Customer ID"
                    >
                </div>


                <div class="form-group">
                    <label>Customer Name</label>

                    <input
                        v-model="newCustomer.customerName"
                        type="text"
                        placeholder="Customer Name"
                    >
                </div>


                <div class="form-group">
                    <label>Phone Number</label>

                    <input
                        v-model="newCustomer.phoneNumber"
                        type="text"
                        placeholder="Phone Number"
                    >
                </div>


                <div class="form-group">
                    <label>Email</label>

                    <input
                        v-model="newCustomer.email"
                        type="email"
                        placeholder="Email"
                    >
                </div>


                <div class="form-group full-width">
                    <label>Address</label>

                    <input
                        v-model="newCustomer.address"
                        type="text"
                        placeholder="Address"
                    >
                </div>

            </div>


            <div class="form-actions">

                <button
                    class="save-btn"
                    @click="saveCustomer"
                >
                    Save Customer
                </button>

                <button
                    class="cancel-btn"
                    @click="closeForm"
                >
                    Cancel
                </button>

            </div>

        </div>


        <!-- SEARCH -->

        <div class="search-container">

            <input
                v-model="search"
                type="text"
                placeholder="Search customers..."
                class="search-input"
            >

        </div>


        <!-- TABLE -->

        <div class="table-wrap">

            <table class="customers-table">

                <thead>

                    <tr>
                        <th>Customer ID</th>
                        <th>Customer Name</th>
                        <th>Phone Number</th>
                        <th>Email</th>
                        <th>Address</th>
                        <th>Actions</th>
                    </tr>

                </thead>


                <tbody>

                    <!-- LOADING -->

                    <tr v-if="loading">

                        <td colspan="6">
                            Loading customers...
                        </td>

                    </tr>


                    <!-- NO CUSTOMERS -->

                    <tr v-else-if="filteredCustomers.length === 0">

                        <td colspan="6">
                            No customers found.
                        </td>

                    </tr>


                    <!-- CUSTOMERS -->

                    <tr
                        v-for="customer in filteredCustomers"
                        :key="customer.customerId"
                    >

                        <td>
                            {{ customer.customerId }}
                        </td>

                        <td>
                            {{ customer.customerName }}
                        </td>

                        <td>
                            {{ customer.phoneNumber }}
                        </td>

                        <td>
                            {{ customer.email }}
                        </td>

                        <td>
                            {{ customer.address }}
                        </td>

                        <td class="actions">

<!--                            <button class="edit-btn">-->
<!--                                Edit-->
<!--                            </button>-->

                            <button
                                class="btn-danger"
                                @click="deleteCustomer(customer.customerId)"
                            >
                                Delete
                            </button>

                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

    </main>

</template>


<style scoped>

.customers-page {

    --surface: #0b1326;
    --surface-container-lowest: #060e20;
    --surface-container: #171f33;
    --surface-container-highest: #2d3449;

    --on-surface: #dae2fd;
    --on-surface-variant: #c2c6d6;

    --outline-variant: #424754;

    --primary: #adc6ff;
    --primary-container: #4d8eff;

    --secondary-container: #00a572;

    --tertiary: #ffb95f;

    min-height: 100vh;

    padding: 2rem;

    background: var(--surface);

    color: var(--on-surface);

    font-family: 'Inter', sans-serif;
}


/* HEADER */

.page-header {

    display: flex;

    justify-content: space-between;

    align-items: center;

    padding-bottom: 1rem;

    margin-bottom: 1.5rem;

    border-bottom: 1px solid var(--outline-variant);
}


.page-header h1 {

    margin: 0;

    font-size: 1.7rem;
}


.subtitle {

    color: var(--on-surface-variant);

    margin-top: 0.4rem;
}


/* ADD BUTTON */

.add-btn {

  display: grid;
  justify-content: center;

  border: none;
  border-radius: 4px;

  padding: 9px 24px;

  font-size: 14px;
  font-weight: bolder;
  width: 150px;
  background: var(--admin-primary);
  color: #fff;
  text-decoration: none;
}


.add-btn:hover {

    opacity: 0.8;
}


/* FORM */

.customer-form {

    background: var(--surface-container);

    border: 1px solid var(--outline-variant);

    border-radius: 1rem;

    padding: 1.5rem;

    margin-bottom: 1.5rem;
}


.customer-form h2 {

    margin-top: 0;

    margin-bottom: 1.5rem;
}


.form-grid {

    display: grid;

    grid-template-columns: repeat(2, 1fr);

    gap: 1rem;
}


.form-group {

    display: flex;

    flex-direction: column;

    gap: 0.4rem;
}


.form-group label {

    color: var(--on-surface-variant);

    font-size: 0.8rem;
}


.form-group input {

    padding: 0.7rem 1rem;

    border: 1px solid var(--outline-variant);

    border-radius: 0.5rem;

    background: var(--surface-container-lowest);

    color: var(--on-surface);

    outline: none;
}


.form-group input:focus {

    border-color: var(--primary);
}


.full-width {

    grid-column: span 2;
}


/* FORM BUTTONS */

.form-actions {

    display: flex;

    gap: 1rem;

    margin-top: 1.5rem;
}


.save-btn {

    padding: 0.6rem 1.2rem;

    border: none;

    border-radius: 999px;

    background: var(--secondary-container);

    color: white;

    cursor: pointer;
}


.cancel-btn {

    padding: 0.6rem 1.2rem;

    border: 1px solid var(--outline-variant);

    border-radius: 999px;

    background: transparent;

    color: var(--on-surface);

    cursor: pointer;
}


/* SEARCH */

.search-container {

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
}


/* TABLE */

.table-wrap {

    overflow-x: auto;
}


.customers-table {

    width: 100%;

    border-collapse: collapse;
}


.customers-table thead {

    background: var(--surface-container-highest);
}


.customers-table th {

    padding: 1rem;

    color: var(--on-surface-variant);

    font-size: 0.8rem;

    text-transform: uppercase;

    text-align: center;
}


.customers-table td {

    padding: 1rem;

    border-bottom: 1px solid rgba(66, 71, 84, 0.5);

    text-align: center;

    color: var(--on-surface);
}


.customers-table tbody tr:hover {

    background: var(--surface-container);
}


/* ACTION BUTTONS */

.actions {

    display: flex;

    justify-content: center;

    gap: 0.5rem;
}


.edit-btn,
.delete-btn {

    padding: 0.4rem 0.8rem;

    border-radius: 999px;

    background: transparent;

    cursor: pointer;
}


.edit-btn {

    border: 1px solid var(--primary);

    color: var(--primary);
}


.delete-btn {

    border: 1px solid var(--tertiary);

    color: var(--tertiary);
}


/* MOBILE */

@media (max-width: 700px) {

    .page-header {

        flex-direction: column;

        align-items: flex-start;

        gap: 1rem;
    }


    .form-grid {

        grid-template-columns: 1fr;
    }


    .full-width {

        grid-column: span 1;
    }

}

</style>