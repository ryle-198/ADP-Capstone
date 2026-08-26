<script setup>
import { ref } from 'vue'
</script>

<template>
  <header class="navbar">
    <div class="navbar-inner">

      <router-link to="/" class="logo">NAME</router-link>

      <nav class="nav-links">
        <a href="#track" class="nav-link">Track</a>

        <a href="#services" class="nav-link">Services</a>

        <a href="#quote" class="nav-link">Get a Quote</a>

        <a href="#support" class="nav-link">Support</a>
      </nav>

      <div class="nav-actions">
        <router-link to="/login" class="login-link">
          Login
        </router-link>

        <button class="ship-button">
          Ship Now
        </button>

      </div>
    </div>
  </header>
</template>

<style>
.navbar {
  position: sticky;
  top: 0;
  z-index: 50;
  width: 100%;
  background: white;
  box-shadow: 0 2px 8px rgba(21, 22, 40, 0.06);
}

.navbar-inner {
  min-height: 73px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: var(--container-max);
  margin: 0 auto;
  padding: 16px var(--desktop-margin);

}


.logo {
  color: var(--admin-primary);
  margin-left: 1rem;
  font-size: 32px;
  line-height: 40px;
  font-weight: 700;
  letter-spacing: -0.01em;

  transition: opacity 0.2s ease;
  text-decoration: none;
}

.logo:hover {
  opacity: 0.8;
}


.nav-links {
  display: flex;
  align-items: center;
  gap: 32px;
  
}

.nav-link {
  color: var(--on-surface-variant);
  font-size: 14px;
  line-height: 20px;
  font-weight: 1000;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  transition: color 0.2s ease;
  text-decoration: none;
}

.nav-link:hover {
  color: var(--secondary);
}

.nav-link.active {
  color: var(--primary);
  padding-bottom: 4px;
}

.nav-actions {
  margin-right:1rem;  
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-link {
  color: var(--on-surface-variant);

  font-size: 14px;
  line-height: 20px;
  font-weight: 500;

  transition: color 0.2s ease;
    text-decoration: none;

}

.login-link:hover {
  color: var(--primary);
}

.ship-button {
  border: none;
  border-radius: 4px;

  padding: 9px 24px;

  background: var(--primary);
  color: white;

  font-size: 14px;
  font-weight: 500;

  transition:
    background 0.2s ease,
    transform 0.1s ease;
      text-decoration: none;

}

.ship-button:hover {
  background: var(--secondary);
}

.ship-button:active {
  transform: scale(0.96);
}
/* 
.mobile-menu-button {
  display: none;

  border: none;
  background: transparent;

  color: var(--primary);

  padding: 8px;
}

.mobile-nav {
  display: none;
} */
</style>