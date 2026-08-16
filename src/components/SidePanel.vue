<script setup>
import { ref } from 'vue'
import vueLogo from '../assets/vue.svg'
import customerIcon from '../assets/customer-icon.png'
import ordersIcon from '../assets/orders-icon.png'
import vehicleIcon from '../assets/vehicle-icon.png'
import driverIcon from '../assets/driver-icon.png'
import routeIcon from '../assets/routes-icon.png'
import shipmentIcon from '../assets/shipment-icon.png'
const isExpanded = ref(localStorage.getItem("isExpanded") === "true")

const toggleMenu = () => {
  isExpanded.value = !isExpanded.value
  localStorage.setItem("isExpanded", isExpanded.value)  
}

</script>

<template>
  <div class="layout">
    <aside :class="`aside ${isExpanded ? 'is-expanded' : ''}`">
      <div class="logo">
        <img :src="vueLogo" alt="logo" />
      </div>

      <div class="menu-toggle-wrap">
        <button class="menu-toggle" @click="toggleMenu">
          <span class="material-symbols-outlined">double_arrow</span>
        </button>
      </div>

      <h3>Tabs</h3>
      <div class="menu">
        <RouterLink :to="{ name: 'orders' }" class="button">
          <span class="icon"><img :src="ordersIcon" alt="ordersIcon" /></span>
          <span class="text">Orders</span>
        </RouterLink>

        <RouterLink :to="{ name: 'customers' }" class="button">
          <span class="icon"><img :src="customerIcon" alt="customerIcon" /></span>
          <span class="text">Customers</span>
        </RouterLink>

        <RouterLink :to="{ name: 'drivers' }" class="button">
          <span class="icon"><img :src="driverIcon" alt="driversIcon" /></span>
          <span class="text">Drivers</span>
        </RouterLink>

        <RouterLink :to="{ name: 'routes' }" class="button">
          <span class="icon"><img :src="routeIcon" alt="routesIcon" /></span>
          <span class="text">Routes</span>
        </RouterLink>

        <RouterLink :to="{ name: 'shipment' }" class="button">
          <span class="icon"><img :src="shipmentIcon" alt="shipmentIcon" /></span>
          <span class="text">Shipments</span>
        </RouterLink>

        <RouterLink :to="{ name: 'vehicle' }" class="button">
          <span class="icon"><img :src="vehicleIcon" alt="vehicleIcon" /></span>
          <span class="text">Vehicles</span>
        </RouterLink>

      </div>

      <div class="flex"></div>
    </aside>

    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<style lang="scss" scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.content {
  flex: 1;
  padding: 2rem;
}

.aside {
  display: flex;
  flex-direction: column;

  background-color: var(--dark);
  color: var(--light);
  
  width: calc(2rem + 32px);
  overflow: hidden;
  min-height: 100vh;
  padding: 1rem;
  transition: 0.2s ease-in-out;

  .flex {
    flex: 1 1 0%;
  }

  .logo {
    margin-bottom: 1rem;
    img {
      width: 2rem;
    }
  }

  .menu-toggle-wrap {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 1rem;
    position: relative;
    top: 0;
    transition: 0.2s ease-in-out;

    .menu-toggle {
      background: none;
      border: none;
      cursor: pointer;
      margin-right:0.5rem;
      transition: 0.2s ease-in-out;

      .material-symbols-outlined {
        font-size: 2rem;
        color: var(--light);
        transition: 0.2s ease-out;
      }

      &:hover .material-symbols-outlined {
        color: var(--primary);
        transform: translateX(0.5rem);
      }
    }
  }

  h3,
  .button .text {
    opacity: 0;
    transition: opacity 0.3s ease-in-out;
  }

  h3 {
    color: var(--grey);
    font-size: 0.875rem;
    margin-bottom: 0.5rem;
    text-transform: uppercase;
  }

  .menu {
    margin: 0 -1rem;

    .button {
      display: flex;
      align-items: center;
      text-decoration: none;
      transition: 0.2s ease-in-out;
      padding: 0.5rem 2.3rem;

      .text {
        color: var(--light);
        margin-left: 1rem;
        transition: 0.2s ease-in-out;
      }

      &:hover {
        background-color: var(--dark-alt);
        .text {
          color: var(--primary);
        }
      }

      &.router-link-exact-active {
        background-color: var(--dark-alt);
        border-right: 5px solid var(--primary);
        .text {
          color: var(--primary);
        }
      }
    }
  }

  &.is-expanded {
    width: var(--sidebar-width);

    .menu-toggle-wrap {
      top: -3.5rem;
      .menu-toggle {
        transform: rotate(-180deg);
      }
    }

    h3,
    .button .text {
      opacity: 1;
    }
  }

  @media (max-width: 1024px) {
    position: absolute;
    z-index: 99;
  }
}

</style>