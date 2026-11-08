<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import authService from '../services/authService'
import { useAuth } from '../stores/authStore'
import vueLogo from '../assets/vue.svg'
import customerIcon from '../assets/customer-icon.png'
import ordersIcon from '../assets/orders-icon.png'
import vehicleIcon from '../assets/vehicle-icon.png'
import driverIcon from '../assets/driver-icon.png'
import routeIcon from '../assets/routes-icon.png'
import shipmentIcon from '../assets/shipment-icon.png'
import dashboardIcon from '../assets/dashboard.png'
import usersIcon from '../assets/users.png'
const isExpanded = ref(localStorage.getItem("isExpanded") === "true")

const router = useRouter()
const route = useRoute()
const { clearAuth } = useAuth()

function logout() {
  authService.logout().finally(() => {
    clearAuth()
    router.push('/portal-x7q2/login')
  })
}

const toggleMenu = () => {
  isExpanded.value = !isExpanded.value
  localStorage.setItem("isExpanded", isExpanded.value)
}

const usersOpen = ref(
    localStorage.getItem("usersOpen") === "true" || ['customers', 'drivers'].includes(route.name)
)

const toggleUsers = () => {
  usersOpen.value = !usersOpen.value
  localStorage.setItem("usersOpen", usersOpen.value)
}

</script>

<template>
  <!-- <div class="layout"> -->
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
      <RouterLink :to="{name: 'dashboard'}" class="button">
        <span class="icon"><img :src="dashboardIcon" alt="dashboardIcon"></span>
        <span class="text">Dashboard</span>
      </RouterLink>

      <div class="users-row" :class="{ active: route.name === 'users' }">
        <RouterLink :to="{ name: 'users' }" class="button">
          <span class="icon"><img :src="usersIcon" alt="usersIcon" /></span>
          <span class="text">Users</span>
        </RouterLink>
        <button type="button" class="tree-arrow" :aria-expanded="usersOpen"
                title="Show or hide customers and drivers" @click="toggleUsers">
          <span class="material-symbols-outlined" :class="{ open: usersOpen }">chevron_right</span>
        </button>
      </div>

      <!-- Users sub folder/children or whatever only shown when the sidebar is expanded and the folder is open -->
      <div class="child" v-show="usersOpen && isExpanded">
        <RouterLink :to="{ name: 'customers' }" class="button">
          <span class="icon"><img :src="customerIcon" alt="customerIcon" /></span>
          <span class="text">Customers</span>
        </RouterLink>

        <RouterLink :to="{ name: 'drivers' }" class="button">
          <span class="icon"><img :src="driverIcon" alt="driversIcon" /></span>
          <span class="text">Drivers</span>
        </RouterLink>
      </div>

      <RouterLink :to="{ name: 'orders' }" class="button">
        <span class="icon"><img :src="ordersIcon" alt="ordersIcon" /></span>
        <span class="text">Orders</span>
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

      <a href="#" class="button" @click.prevent="logout">
        <span class="icon"><span class="material-symbols-outlined">logout</span></span>
        <span class="text">Logout</span>
      </a>



    </div>

    <div class="flex"></div>

<!--    <div class="menu">-->
<!--      <a href="#" class="button" @click.prevent="logout">-->
<!--        <span class="icon"><span class="material-symbols-outlined">logout</span></span>-->
<!--        <span class="text">Logout</span>-->
<!--      </a>-->
<!--    </div>-->
  </aside>

  <!-- <main class="content">
    <RouterView />
  </main> -->
  <!-- </div> -->
</template>

<style lang="scss" scoped>
// .layout {
//   display: flex;
//   min-height: 100vh;
// }

// .content {
//   flex: 1;
//   padding: 2rem;
// }

.aside {
  display: flex;
  flex-direction: column;

  background-color: var(--dark);
  color: var(--light);

  width: calc(5rem + 32px);
  overflow: hidden;
  height: auto;
  padding: 1rem;
  transition: 0.2s ease-in-out;

  .flex {
    flex: 1 1 0%;
  }

  .logo {
    // padding-left: 1rem;
    // margin-bottom: 1rem;
    // margin-right: 1rem;
    img {
      width: 2rem;
    }
  }

  .menu-toggle-wrap {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 0.5rem;
    margin-right: 1rem;
    position: relative;
    top: 0;
    transition: 0.2s ease-in-out;

    .menu-toggle {
      background: none;
      border: none;
      cursor: pointer;
      margin-top: 1rem;
      margin-right:0.5rem;
      transition: 0.2s ease-in-out;

      .material-symbols-outlined {
        font-size: 2rem;
        color: var(--light);
        transition: 0.2s ease-out;
      }

      &:hover .material-symbols-outlined {
        color: var(--secondary);
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

      .material-symbols-outlined {
        color: var(--light);
      }

      .text {
        color: var(--light);
        margin-left: 1rem;
        transition: 0.2s ease-in-out;
      }

      &:hover {
        background-color: var(--dark-alt);
        .text {
          color: var(--secondary);
        }
      }

      &.router-link-exact-active {
        background-color: var(--dark-alt);
        border-right: 5px solid var(--secondary);
        .text {
          color: var(--secondary);
        }
      }
    }
  }

  // The highlight belongs to the whole row (link + arrow), so the green bar ends after the arrow.
  .users-row {
    display: flex;
    align-items: center;

    .button {
      flex: 1;
    }

    // the link's own highlight is turned off here; the row draws it instead
    .button.router-link-exact-active {
      background-color: transparent;
      border-right: none;
    }

    &:hover {
      background-color: var(--dark-alt);

      .text {
        color: var(--secondary);
      }
    }

    &.active {
      background-color: var(--dark-alt);
      border-right: 5px solid var(--secondary);

      .text {
        color: var(--secondary);
      }
    }
  }

  .tree-arrow {
    display: none;
    background: none;
    border: none;
    cursor: pointer;
    padding: 0 1rem 0 0;
    color: var(--light);

    .material-symbols-outlined {
      transition: transform 0.2s ease-in-out;

      &.open {
        transform: rotate(90deg);
      }
    }

    &:hover {
      color: var(--secondary);
    }
  }

  &.is-expanded {
    width: var(--sidebar-width);

    .tree-arrow {
      display: flex;
    }

    .child .button {
      padding-left: 3.6rem;
    }

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