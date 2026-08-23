<script setup>
import { ref, onMounted } from "vue";
import vehicleService from "../services/vehicleService";

const vehicles = ref([]);
const loading = ref(false);
const error = ref("");
const success = ref("");

const search = ref("");
const isEditing = ref(false);

const form = ref({
  vehicleId: "",
  numberPlate: "",
  type: "TRUCK",
  capacity: 0,
  currentStatus: "AVAILABLE",
  mileage: 0,
  lastService: ""
});

const vehicleTypes = [
  "TRUCK",
  "VAN",
  "BAKKIE",
  "MOTORBIKE",
  "TRAILER"
];

const vehicleStatuses = [
  "AVAILABLE",
  "IN_USE",
  "IN_SERVICE",
  "OUT_OF_SERVICE",
  "RESERVED"
];

const fetchVehicles = async () => {
  loading.value = true;
  error.value = "";

  try {
    const response = await vehicleService.getAll();
    vehicles.value = response.data;
  } catch (err) {
    console.error(err);
    error.value = "Could not load vehicles from the database.";
  } finally {
    loading.value = false;
  }
};

const clearForm = () => {
  form.value = {
    vehicleId: "",
    numberPlate: "",
    type: "TRUCK",
    capacity: 0,
    currentStatus: "AVAILABLE",
    mileage: 0,
    lastService: ""
  };

  isEditing.value = false;
};

const saveVehicle = async () => {
  error.value = "";
  success.value = "";

  try {
    if (isEditing.value) {
      await vehicleService.update(form.value);
      success.value = "Vehicle updated successfully.";
    } else {
      await vehicleService.create(form.value);
      success.value = "Vehicle created successfully.";
    }

    clearForm();
    await fetchVehicles();

  } catch (err) {
    console.error(err);
    error.value = "Could not save the vehicle.";
  }
};

const editVehicle = (vehicle) => {
  form.value = {
    vehicleId: vehicle.vehicleId,
    numberPlate: vehicle.numberPlate,
    type: vehicle.type,
    capacity: vehicle.capacity,
    currentStatus: vehicle.currentStatus,
    mileage: vehicle.mileage,
    lastService: vehicle.lastService
  };

  isEditing.value = true;

  window.scrollTo({
    top: 0,
    behavior: "smooth"
  });
};

const deleteVehicle = async (vehicleId) => {
  if (!confirm("Are you sure you want to delete this vehicle?")) {
    return;
  }

  error.value = "";
  success.value = "";

  try {
    await vehicleService.delete(vehicleId);

    success.value = "Vehicle deleted successfully.";

    await fetchVehicles();

  } catch (err) {
    console.error(err);
    error.value = "Could not delete the vehicle.";
  }
};

const filteredVehicles = () => {
  if (!search.value) {
    return vehicles.value;
  }

  const searchTerm = search.value.toLowerCase();

  return vehicles.value.filter(vehicle =>
      vehicle.vehicleId?.toLowerCase().includes(searchTerm) ||
      vehicle.numberPlate?.toLowerCase().includes(searchTerm) ||
      vehicle.type?.toLowerCase().includes(searchTerm) ||
      vehicle.currentStatus?.toLowerCase().includes(searchTerm)
  );
};

onMounted(() => {
  fetchVehicles();
});
</script>

<template>
  <div class="vehicles-page">

    <div class="page-header">
      <h1>VEHICLE</h1>
      <p>Manage all vehicles registered in the delivery system.</p>
    </div>

    <div v-if="success" class="success-message">
      {{ success }}
    </div>

    <div v-if="error" class="error-message">
      {{ error }}
    </div>

    <section class="vehicle-form-card">

      <div class="section-header">
        <div>
          <h2>
            {{ isEditing ? "Update Vehicle" : "Add Vehicle" }}
          </h2>

          <p>
            {{
              isEditing
                  ? "Update the vehicle information below."
                  : "Enter the details of a new vehicle."
            }}
          </p>
        </div>
      </div>

      <form @submit.prevent="saveVehicle">

        <div class="form-grid">

          <div class="form-group">
            <label>Vehicle ID</label>

            <input
                v-model="form.vehicleId"
                type="text"
                placeholder="e.g. V001"
                :disabled="isEditing"
                required
            />
          </div>

          <div class="form-group">
            <label>Number Plate</label>

            <input
                v-model="form.numberPlate"
                type="text"
                placeholder="e.g. CA12345"
                required
            />
          </div>

          <div class="form-group">
            <label>Vehicle Type</label>

            <select v-model="form.type" required>
              <option
                  v-for="type in vehicleTypes"
                  :key="type"
                  :value="type"
              >
                {{ type }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>Capacity</label>

            <input
                v-model.number="form.capacity"
                type="number"
                min="0"
                step="0.01"
                placeholder="e.g. 5000"
                required
            />
          </div>

          <div class="form-group">
            <label>Status</label>

            <select
                v-model="form.currentStatus"
                required
            >
              <option
                  v-for="status in vehicleStatuses"
                  :key="status"
                  :value="status"
              >
                {{ status }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>Mileage</label>

            <input
                v-model.number="form.mileage"
                type="number"
                min="0"
                step="0.01"
                placeholder="e.g. 12000"
                required
            />
          </div>

          <div class="form-group">
            <label>Last Service</label>

            <input
                v-model="form.lastService"
                type="date"
                required
            />
          </div>

        </div>

        <div class="form-buttons">

          <button
              type="submit"
              class="primary-button"
          >
            {{ isEditing ? "Update Vehicle" : "Create Vehicle" }}
          </button>

          <button
              v-if="isEditing"
              type="button"
              class="secondary-button"
              @click="clearForm"
          >
            Cancel
          </button>

        </div>

      </form>
    </section>

    <section class="vehicle-list-section">

      <div class="list-header">

        <div>
          <h2>Registered Vehicles</h2>

          <p>
            {{ filteredVehicles().length }} vehicle(s) found
          </p>
        </div>

        <button
            class="refresh-button"
            @click="fetchVehicles"
            :disabled="loading"
        >
          {{ loading ? "Loading..." : "Refresh" }}
        </button>

      </div>

      <div class="search-container">

        <input
            v-model="search"
            type="text"
            class="search-input"
            placeholder="Search by Vehicle ID, Number Plate, Type or Status"
        />

        <button
            v-if="search"
            class="clear-search-button"
            @click="search = ''"
        >
          Clear
        </button>

      </div>

      <div
          v-if="loading"
          class="empty-message"
      >
        Loading vehicles...
      </div>

      <div
          v-else-if="filteredVehicles().length === 0"
          class="empty-message"
      >
        <h3>No vehicles found</h3>

        <p>
          No vehicles match your search.
        </p>
      </div>

      <div
          v-else
          class="table-container"
      >

        <table>

          <thead>
          <tr>
            <th>Vehicle ID</th>
            <th>Number Plate</th>
            <th>Type</th>
            <th>Capacity</th>
            <th>Status</th>
            <th>Mileage</th>
            <th>Last Service</th>
            <th>Actions</th>
          </tr>
          </thead>

          <tbody>

          <tr
              v-for="vehicle in filteredVehicles()"
              :key="vehicle.vehicleId"
          >

            <td>
              {{ vehicle.vehicleId }}
            </td>

            <td>
              <strong>
                {{ vehicle.numberPlate }}
              </strong>
            </td>

            <td>
              {{ vehicle.type }}
            </td>

            <td>
              {{ vehicle.capacity }}
            </td>

            <td>

                <span
                    class="status-badge"
                    :class="{
                    'status-available':
                      vehicle.currentStatus === 'AVAILABLE',

                    'status-unavailable':
                      vehicle.currentStatus !== 'AVAILABLE'
                  }"
                >
                  {{ vehicle.currentStatus }}
                </span>

            </td>

            <td>
              {{ vehicle.mileage }}
            </td>

            <td>
              {{ vehicle.lastService }}
            </td>

            <td>

              <div class="actions">

                <button
                    class="edit-button"
                    @click="editVehicle(vehicle)"
                >
                  Edit
                </button>

                <button
                    class="delete-button"
                    @click="deleteVehicle(vehicle.vehicleId)"
                >
                  Delete
                </button>

              </div>

            </td>

          </tr>

          </tbody>

        </table>

      </div>

    </section>

  </div>
</template>

<style scoped>

.vehicles-page {
  --surface: #0b1326;
  --surface-container-lowest: #060e20;
  --surface-container-low: #131b2e;
  --surface-container: #171f33;
  --surface-container-high: #222a3d;
  --surface-container-highest: #2d3449;
  --surface-variant: #2d3449;

  --on-surface: #dae2fd;
  --on-surface-variant: #c2c6d6;

  --outline: #8c909f;
  --outline-variant: #424754;

  --primary: #adc6ff;
  --primary-container: #4d8eff;

  --secondary: #4edea3;
  --error: #ffb4ab;

  width: 100%;
  max-width: 1126px;
  margin: 0 auto;
  padding: 40px 25px;
  box-sizing: border-box;

  text-align: left;

  background: var(--surface);
  color: var(--on-surface);

  min-height: 100vh;

  font-family: 'Inter', sans-serif;
}


.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-header h1 {
  color: var(--on-surface);
  margin-bottom: 0 0 15px 0;
  text-align: center;
}

.page-header p {
  color: var(--on-surface-variant);
  text-align: center;
}


.success-message,
.error-message {
  padding: 14px 18px;
  margin-bottom: 20px;
  border-radius: 8px;
}

.success-message {
  color: var(--secondary);
  background: rgba(78, 222, 163, 0.1);
  border: 1px solid rgba(78, 222, 163, 0.3);
}

.error-message {
  color: var(--error);
  background: rgba(255, 180, 171, 0.1);
  border: 1px solid rgba(255, 180, 171, 0.3);
}


.vehicle-form-card,
.vehicle-list-section {
  background: var(--surface-container);

  border: 1px solid var(--outline-variant);

  border-radius: 12px;

  box-shadow:
      rgba(0, 0, 0, 0.25) 0 10px 15px -3px,
      rgba(0, 0, 0, 0.15) 0 4px 6px -2px;

  padding: 30px;

  margin-bottom: 30px;
}

.section-header {
  margin-bottom: 25px;
}

.section-header h2,
.list-header h2 {
  color: var(--on-surface);
  margin-bottom: 5px;
}

.section-header p,
.list-header p {
  color: var(--on-surface-variant);
}


.form-grid {
  display: grid;

  grid-template-columns: repeat(2, 1fr);

  gap: 20px;
}

.form-group {
  display: flex;

  flex-direction: column;

  gap: 7px;
}

.form-group label {
  font-size: 15px;

  font-weight: 600;

  color: var(--on-surface-variant);
}

.form-group input,
.form-group select {
  width: 100%;

  box-sizing: border-box;

  padding: 11px 12px;

  border: 1px solid var(--outline-variant);

  border-radius: 7px;

  background: var(--surface-container-lowest);

  color: var(--on-surface);

  font: inherit;

  outline: none;
}

.form-group input::placeholder {
  color: var(--on-surface-variant);
}

.form-group input:focus,
.form-group select:focus {
  border-color: var(--primary);

  box-shadow:
      0 0 0 3px rgba(173, 198, 255, 0.15);
}

.form-group input:disabled {
  opacity: 0.6;

  cursor: not-allowed;
}


.form-buttons {
  display: flex;

  gap: 10px;

  margin-top: 25px;
}

.primary-button,
.secondary-button,
.refresh-button,
.edit-button,
.delete-button {
  border-radius: 999px;

  padding: 10px 16px;

  cursor: pointer;

  font: inherit;
}

.primary-button {
  color: white;

  background: var(--primary-container);

  border: none;
}

.secondary-button,
.refresh-button {
  color: var(--on-surface);

  background: transparent;

  border: 1px solid var(--outline-variant);
}

.primary-button:hover,
.refresh-button:hover,
.secondary-button:hover {
  opacity: 0.85;
}

.refresh-button:disabled {
  opacity: 0.6;

  cursor: not-allowed;
}


.list-header {
  display: flex;

  justify-content: space-between;

  align-items: center;

  margin-bottom: 20px;
}


.search-container {
  display: flex;

  gap: 10px;

  margin-bottom: 20px;
}

.search-input {
  width: 100%;

  padding: 11px 15px;

  border: 1px solid var(--outline-variant);

  border-radius: 999px;

  background: var(--surface-container-lowest);

  color: var(--on-surface);

  font: inherit;

  outline: none;
}

.search-input::placeholder {
  color: var(--on-surface-variant);
}

.search-input:focus {
  border-color: var(--primary);

  box-shadow:
      0 0 0 3px rgba(173, 198, 255, 0.15);
}

.clear-search-button {
  padding: 10px 16px;

  border-radius: 999px;

  border: 1px solid var(--outline-variant);

  background: transparent;

  color: var(--on-surface);

  cursor: pointer;

  white-space: nowrap;
}

.clear-search-button:hover {
  background: var(--surface-container-high);
}

/* TABLE */

.table-container {
  width: 100%;

  overflow-x: auto;

  border: 1px solid var(--outline-variant);

  border-radius: 8px;
}

table {
  width: 100%;

  min-width: 1000px;

  border-collapse: collapse;

  background: var(--surface-container);
}

th,
td {
  padding: 14px;

  border-bottom: 1px solid var(--outline-variant);

  text-align: left;

  white-space: nowrap;
}

th {
  color: var(--on-surface-variant);

  background: var(--surface-container-highest);

  font-weight: 600;
}

td {
  color: var(--on-surface);
}

tr:hover td {
  background: var(--surface-container-high);
}

tr:last-child td {
  border-bottom: none;
}

/* STATUS */

.status-badge {
  display: inline-block;

  padding: 5px 9px;

  border-radius: 999px;

  font-size: 13px;
}

.status-available {
  color: var(--secondary);

  background: rgba(78, 222, 163, 0.15);

  border: 1px solid rgba(78, 222, 163, 0.3);
}

.status-unavailable {
  color: var(--error);

  background: rgba(255, 180, 171, 0.15);

  border: 1px solid rgba(255, 180, 171, 0.3);
}

/* ACTIONS */

.actions {
  display: flex;

  gap: 7px;
}

.edit-button {
  color: var(--primary);

  background: transparent;

  border: 1px solid var(--primary);
}

.delete-button {
  color: var(--error);

  background: transparent;

  border: 1px solid var(--error);
}

.edit-button:hover {
  background: var(--primary);

  color: #000;
}

.delete-button:hover {
  background: var(--error);

  color: #000;
}

/* EMPTY */

.empty-message {
  text-align: center;

  padding: 50px 20px;

  border: 1px dashed var(--outline-variant);

  border-radius: 8px;

  color: var(--on-surface-variant);
}

.empty-message h3 {
  color: var(--on-surface);

  margin-bottom: 8px;
}

.empty-message p {
  color: var(--on-surface-variant);
}

/* RESPONSIVE */

@media (max-width: 768px) {

  .vehicles-page {
    padding: 25px 15px;
  }

  .vehicle-form-card,
  .vehicle-list-section {
    padding: 20px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .list-header {
    align-items: flex-start;

    gap: 15px;

    flex-direction: column;
  }

  .search-container {
    flex-direction: column;
  }

  .clear-search-button {
    width: 100%;
  }

  .form-buttons {
    flex-direction: column;
  }

  .primary-button,
  .secondary-button {
    width: 100%;
  }
}

</style>