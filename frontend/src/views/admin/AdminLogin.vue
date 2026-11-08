<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import authService from '../../services/authService'
import {useAuth} from '../../stores/authStore'

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()
const {isAdmin, roles, checkAuth, clearAuth} = useAuth()

async function loginWithRetry(){
  try{
    return await authService.login(username.value, password.value)
  }catch(err){
    if(err.response) throw err
    return await authService.login(username.value, password.value)
  }
}

async function handleLogin(){
  if(loading.value) return
  error.value=''
  loading.value = true

  try{
    try{
      await loginWithRetry()
    }catch(err){
      console.error('Login failed:', err)
      if(err.response?.status === 401){
        error.value = 'Invalid username or password.'
      }else if(!err.response){
        error.value = 'The server is not responding. Please try again.'
      }else{
        error.value = 'Login failed (' + err.response.status + ').'
      }
      return
    }

    await checkAuth()

    if(isAdmin.value){
      router.push({name: 'orders'})
    }else if(roles.value.includes('ROLE_DRIVER')){
      router.push('/my/routes')
    }else if(roles.value.includes('ROLE_CUSTOMER')){

      await authService.logout()
      clearAuth()
      error.value='This is the staff portal. Customers log in on the customer login page.'
    }else{
      await authService.logout()
      clearAuth()
      error.value='This account does not have access to the app yet.'
    }
  }finally{
    loading.value = false
  }
}

</script>

<template>
  <div class="login-page">
    <div class="login-header">
      <!--    <p>username: <span style="color:#4edea3">admin</span></p>-->
      <!--    <p>password: <span style="color:#4edea3">admin</span></p>-->
    </div>
    <form class ="login-card" @submit.prevent="handleLogin">
      <h1>Staff Portal Login</h1>
      <input v-model="username" type="text" placeholder="Username" autocomplete="username"/>
      <input v-model="password" type="password" placeholder="Password" autocomplete="current-password"/>
      <p v-if ="error" class="error">{{error}}</p>
      <button type="submit" :disabled="loading">{{ loading ? 'Logging in...' : 'Login' }}</button>
    </form>
  </div>
</template>

<style scoped>
.login-page{
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--dark);
  flex-direction: column;
}

.login-header{
  margin-bottom: 1rem;
}

.header{
  margin-bottom:1rem;
}
.login-card{
  background: var(--dark-alt);
  padding: 2rem;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 320px;
}

.login-card h1{color: var(--light);
  font-size:1.25rem;
  margin:0;
}

.login-card input{
  padding: 0.6rem 0.8rem;
  border-radius:6px;
  border: 1px solid #444;
  background: var(--dark);
  color: var(--light);
}

.login-card button{
  padding: 0.6rem;
  border-radius: 6px;
  border: none;
  background: var(--admin-primary);
  color:#fff;
  font-weight: 600;
}

.error { color: #ff6b6b; font-size: 0.85rem; margin: 0; }
</style>