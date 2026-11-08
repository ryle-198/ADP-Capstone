<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import authService from '../../services/authService.js'
import { useAuth } from '../../stores/authStore.js'

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()
const { roles, checkAuth, clearAuth } = useAuth()

async function loginWithRetry() {
  try {
    return await authService.login(username.value, password.value)
  } catch (err) {
    if (err.response) throw err
    return await authService.login(username.value, password.value)
  }
}

async function handleLogin() {
  if (loading.value) return
  error.value = ''
  loading.value = true

  try {
    try {
      await loginWithRetry()
    } catch (err) {
      if (err.response?.status === 401) error.value = 'Invalid username or password.'
      else if (!err.response) error.value = 'The server is not responding. Please try again.'
      else error.value = 'Login failed (' + err.response.status + ').'
      return
    }

    await checkAuth()

    if (roles.value.includes('ROLE_CUSTOMER')) {
      router.push('/my/orders')
    } else {
      // staff and drivers use the other portal
      await authService.logout()
      clearAuth()
      error.value = 'This is the customer login. Staff and drivers use the staff portal.'
    }
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
        <RouterLink to="/register" class="btn btn-default btn-sm">Create account</RouterLink>
      </div>
    </header>

    <div class="auth-wrap">
      <section class="panel auth-card narrow">
        <div class="panel-head"><h2>Customer login</h2></div>

        <form @submit.prevent="handleLogin">
          <div class="panel-body">
            <p class="login-intro">Sign in to place orders, pay for them and track your shipments.</p>

            <p v-if="error" class="alert alert-error" role="alert">{{ error }}</p>

            <div class="login-fields">
              <div class="field">
                <label for="l-username">Username</label>
                <input id="l-username" v-model="username" type="text" autocomplete="username" required />
              </div>
              <div class="field">
                <label for="l-password">Password</label>
                <input id="l-password" v-model="password" type="password" autocomplete="current-password" required />
              </div>
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
                {{ loading ? 'Logging in...' : 'Log in' }}
              </button>
            </div>
          </div>
        </form>

        <div class="auth-foot">
          New here? <RouterLink to="/register">Create an account</RouterLink>
          <span class="foot-sep">|</span>
          <RouterLink to="/home">Back to home</RouterLink>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.auth-card.narrow {
  max-width: 420px;
}

.login-intro {
  margin: 0 0 1rem;
  color: var(--p-soft);
}

.login-fields {
  display: flex;
  flex-direction: column;
  gap: 0.9rem;
}

.foot-sep {
  margin: 0 0.5rem;
  color: var(--p-line);
}
</style>