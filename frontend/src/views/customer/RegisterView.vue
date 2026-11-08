<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../services/api.js'
import authService from '../../services/authService.js'
import { useAuth } from '../../stores/authStore.js'

const router = useRouter()
const { checkAuth } = useAuth()

const form = ref({
  username: '',
  password: '',
  customerName: '',
  phoneNumber: '',
  email: '',
  address: ''
})
const error = ref('')
const loading = ref(false)

async function register() {
  if (loading.value) return
  error.value = ''
  loading.value = true

  try {
    await api.post('/auth/register', form.value)
  } catch (err) {
    const code = err.response?.status
    if (code === 409) error.value = 'That username or email is already registered.'
    else if (code === 400) error.value = 'Please fill in username, name and email, and use a password of at least 8 characters.'
    else error.value = 'Could not create the account. Please try again.'
    loading.value = false
    return
  }

  // account created: log straight in and go to the customer page
  try {
    await authService.login(form.value.username, form.value.password)
    await checkAuth()
    router.push('/my/orders')
  } catch {
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="portal">
    <header class="portal-bar">
      <div class="portal-bar-inner">
        <div class="portal-brand">
          <RouterLink to="/home" class="portal-logo">SWIFT</RouterLink>
          <span class="portal-bar-title">Customer portal</span>
        </div>
        <RouterLink to="/login" class="btn btn-default btn-sm">Log in</RouterLink>
      </div>
    </header>

    <div class="auth-wrap">
      <section class="panel auth-card">
        <div class="panel-head"><h2>Create a customer account</h2></div>

        <form @submit.prevent="register">
          <div class="panel-body">
            <p v-if="error" class="alert alert-error">{{ error }}</p>

            <div class="field-grid">
              <h3 class="form-section">Login details</h3>
              <div class="field">
                <label for="r-username">Username</label>
                <input id="r-username" v-model="form.username" type="text" autocomplete="username" required />
              </div>
              <div class="field">
                <label for="r-password">Password</label>
                <input id="r-password" v-model="form.password" type="password" autocomplete="new-password" minlength="8" required />
                <span class="field-hint">At least 8 characters</span>
              </div>

              <h3 class="form-section">Your details</h3>
              <div class="field">
                <label for="r-name">Full name</label>
                <input id="r-name" v-model="form.customerName" type="text" required />
              </div>
              <div class="field">
                <label for="r-email">Email</label>
                <input id="r-email" v-model="form.email" type="email" required />
              </div>
              <div class="field">
                <label for="r-phone">Phone number</label>
                <input id="r-phone" v-model="form.phoneNumber" type="text" />
              </div>
              <div class="field">
                <label for="r-address">Address</label>
                <input id="r-address" v-model="form.address" type="text" />
              </div>
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
                {{ loading ? 'Creating...' : 'Create account' }}
              </button>
            </div>
          </div>
        </form>

        <div class="auth-foot">
          Already have an account? <RouterLink to="/login">Log in</RouterLink>
        </div>
      </section>
    </div>
  </div>
</template>