<script setup>
import { ref, onMounted } from "vue";
import vehicleService from "../services/vehicleService";

const vehicles = ref([]);
const loading = ref(true);
const error = ref("");

const fetchVehicles = async () => {
  loading.value = true;
  error.value = "";

  try {
    const response = await vehicleService.getAll();

    vehicles.value = response.data;
  } catch (err) {
    console.error("Error fetching vehicles:", err);
    error.value = "Unable to load vehicles. Please make sure the backend is running.";
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchVehicles();
});
</script>

<template>
  <div class="vehicles-page">

    <div class="vehicles-header">
      <h1>Vehicles</h1>
      <p>View all vehicles registered in the delivery system.</p>
    </div>

    <div v-if="loading" class="message">
      Loading vehicles...
    </div>

    <div v-else-if="error" class="error-message">
      {{ error }}

      <button @click="fetchVehicles">
        Try Again
      </button>
    </div>

    <div v-else-if="vehicles.length === 0" class="message">
      No vehicles found in the database.
    </div>

    <div v-else class="vehicle-table-container">

      <table class="vehicle-table">

        <thead>
        <tr>
          <th>Vehicle ID</th>
          <th>Number Plate</th>
          <th>Type</th>
          <th>Capacity</th>
          <th>Status</th>
          <th>Mileage</th>
          <th>Last Service</th>
        </tr>
        </thead>

        <tbody>
        <tr
            v-for="vehicle in vehicles"
            :key="vehicle.vehicleId"
        >
          <td>{{ vehicle.vehicleId }}</td>

          <td>
            <strong>{{ vehicle.numberPlate }}</strong>
          </td>

          <td>{{ vehicle.type }}</td>

          <td>{{ vehicle.capacity }}</td>

          <td>
                            <span
                                class="status"
                                :class="vehicle.currentStatus?.toLowerCase()"
                            >
                                {{ vehicle.currentStatus }}
                            </span>
          </td>

          <td>{{ vehicle.mileage }}</td>

          <td>{{ vehicle.lastService }}</td>
        </tr>
        </tbody>

      </table>

    </div>

  </div>
</template>

<style scoped>

.vehicles-page {
  min-height: 100vh;
  padding: 40px;
  box-sizing: border-box;
  background: var(--bg);
  color: var(--text);
}

.vehicles-header {
  margin-bottom: 30px;
  text-align: left;
}

.vehicles-header h1 {
  margin: 0 0 10px;
  font-size: 40px;
  color: var(--text-h);
}

.vehicles-header p {
  color: var(--text);
}

.vehicle-table-container {
  width: 100%;
  overflow-x: auto;
  border: 1px solid var(--border);
  border-radius: 10px;
  box-shadow: var(--shadow);
  background: var(--bg);
}

.vehicle-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 850px;
}

.vehicle-table th {
  padding: 16px;
  text-align: left;
  background: var(--accent-bg);
  color: var(--text-h);
  border-bottom: 2px solid var(--border);
}

.vehicle-table td {
  padding: 15px 16px;
  text-align: left;
  border-bottom: 1px solid var(--border);
}

.vehicle-table tbody tr:hover {
  background: var(--accent-bg);
}

.status {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
}

.status.available {
  color: var(--accent);
  background: var(--accent-bg);
}

.status.in_use {
  color: var(--accent);
  background: var(--accent-bg);
}

.status.in_service {
  color: var(--accent);
  background: var(--accent-bg);
}

.status.out_of_service {
  color: var(--text);
  background: var(--code-bg);
}

.status.reserved {
  color: var(--accent);
  background: var(--accent-bg);
}

.message {
  padding: 30px;
  border: 1px solid var(--border);
  border-radius: 10px;
  text-align: center;
  background: var(--bg);
}

.error-message {
  padding: 25px;
  border: 1px solid var(--accent-border);
  border-radius: 10px;
  background: var(--accent-bg);
  text-align: center;
}

.error-message button {
  margin-top: 15px;
  padding: 10px 18px;
  border: none;
  border-radius: 6px;
  background: var(--accent);
  color: white;
  cursor: pointer;
}

.error-message button:hover {
  box-shadow: var(--shadow);
}

@media (max-width: 768px) {
  .vehicles-page {
    padding: 20px;
  }

  .vehicles-header h1 {
    font-size: 32px;
  }
}

</style>