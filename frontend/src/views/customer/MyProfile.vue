<script setup>
import { ref, onMounted } from 'vue'
import myService from '../../services/myService.js'

const username = ref('')
const form = ref({ customerName: '', phoneNumber: '', email: '', address: '' })
const profileMessage = ref('')
const profileError = ref('')

const pw = ref({ currentPassword: '', newPassword: '', confirm: '' })
const pwMessage = ref('')
const pwError = ref('')

onMounted(() => {
  myService.getProfile()
      .then(res => {
        username.value = res.data.username
        form.value = {
          customerName: res.data.customerName || '',
          phoneNumber: res.data.phoneNumber || '',
          email: res.data.email || '',
          address: res.data.address || ''
        }
      })
      .catch(() => { profileError.value = 'Could not load your profile.' })
})

function saveProfile() {
  profileMessage.value = ''
  profileError.value = ''
  myService.updateProfile(form.value)
      .then(() => { profileMessage.value = 'Profile saved.' })
      .catch(err => {
        const code = err.response?.status
        if (code === 409) profileError.value = 'That email is already registered.'
        else if (code === 400) profileError.value = 'Name and email are required.'
        else profileError.value = 'Could not save your profile.'
      })
}

function savePassword() {
  pwMessage.value = ''
  pwError.value = ''
  if (pw.value.newPassword.length < 8) {
    pwError.value = 'New password must be at least 8 characters.'
    return
  }
  if (pw.value.newPassword !== pw.value.confirm) {
    pwError.value = 'The new passwords do not match.'
    return
  }
  myService.changePassword(pw.value.currentPassword, pw.value.newPassword)
      .then(() => {
        pwMessage.value = 'Password changed.'
        pw.value = { currentPassword: '', newPassword: '', confirm: '' }
      })
      .catch(err => {
        pwError.value = err.response?.status === 400
            ? 'Current password is wrong, or the new one is too short.'
            : 'Could not change the password.'
      })
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
        <RouterLink to="/my/orders" class="btn btn-default btn-sm">Back to my orders</RouterLink>
      </div>
    </header>

    <div class="portal-main">
      <div class="portal-head">
        <h1>My profile</h1>
        <p>Signed in as <strong class="mono-user">{{ username }}</strong></p>
      </div>

      <div class="portal-grid">
        <!-- Details -->
        <section class="panel">
          <div class="panel-head"><h2>Your details</h2></div>
          <form @submit.prevent="saveProfile">
            <div class="panel-body">
              <p v-if="profileError" class="alert alert-error">{{ profileError }}</p>
              <p v-if="profileMessage" class="alert alert-success">{{ profileMessage }}</p>

              <div class="field-grid">
                <div class="field">
                  <label for="p-name">Full name</label>
                  <input id="p-name" v-model="form.customerName" type="text" required />
                </div>
                <div class="field">
                  <label for="p-email">Email</label>
                  <input id="p-email" v-model="form.email" type="email" required />
                </div>
                <div class="field">
                  <label for="p-phone">Phone number</label>
                  <input id="p-phone" v-model="form.phoneNumber" type="text" />
                </div>
                <div class="field">
                  <label for="p-address">Address</label>
                  <input id="p-address" v-model="form.address" type="text" />
                </div>
              </div>

              <div class="form-actions end">
                <button type="submit" class="btn btn-primary">Save changes</button>
              </div>
            </div>
          </form>
        </section>

        <!-- Password -->
        <section class="panel">
          <div class="panel-head"><h2>Change password</h2></div>
          <form @submit.prevent="savePassword">
            <div class="panel-body">
              <p v-if="pwError" class="alert alert-error">{{ pwError }}</p>
              <p v-if="pwMessage" class="alert alert-success">{{ pwMessage }}</p>

              <div class="field-grid">
                <div class="field wide">
                  <label for="pw-current">Current password</label>
                  <input id="pw-current" v-model="pw.currentPassword" type="password" autocomplete="current-password" required />
                </div>
                <div class="field wide">
                  <label for="pw-new">New password</label>
                  <input id="pw-new" v-model="pw.newPassword" type="password" autocomplete="new-password" minlength="8" required />
                  <span class="field-hint">At least 8 characters</span>
                </div>
                <div class="field wide">
                  <label for="pw-confirm">Repeat new password</label>
                  <input id="pw-confirm" v-model="pw.confirm" type="password" autocomplete="new-password" required />
                </div>
              </div>

              <div class="form-actions end">
                <button type="submit" class="btn btn-primary">Change password</button>
              </div>
            </div>
          </form>
        </section>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mono-user {
  font-family: var(--font-mono, ui-monospace, 'SFMono-Regular', Consolas, monospace);
  font-size: 13px;
  color: var(--p-ink);
}
</style>