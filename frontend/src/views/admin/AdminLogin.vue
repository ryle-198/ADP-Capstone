<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import authService from '../../services/authService' 
import {useAuth} from '../../stores/authStore'

const username = ref('')
const password = ref('')
const error = ref('')
const router = useRouter()
const {isAdmin, checked} = useAuth()

async function handleLogin(){
 error.value=''

 try{
    const res = await authService.login(username.value, password.value)
    const roles = res.data.roles || []

    if(roles.some(r=>r.authority==='ROLE_ADMIN')){
        isAdmin.value = true
        checked.value = true
        router.push({name: 'orders'})
    }else{
        error.value='This account does not have admin access.'
    }
 }catch{
    error.value="Invalid username or password."
 }
}

</script>

<template>
<div class="login-page">
  <div class="login-header">
    <h2 class="header">Login Creds:</h2>
    <p>username: <span style="color:#4edea3">admin</span></p>
    <p>password: <span style="color:#4edea3">admin</span></p>
  </div>
  <form class ="login-card" @submit.prevent="handleLogin">
        <h1>Admin Login</h1>
        <input v-model="username" type="text" placeholder="Username" autocomplete="username"/>
        <input v-model="password" type="password" placeholder="Password" autocomplete="current-password"/>
        <p v-if ="error" class="error">{{error}}</p>
        <button type="submit">Login</button>
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