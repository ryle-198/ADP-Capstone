<script setup>
import { reactive } from 'vue'

const quote = reactive({
  from: '',
  to: '',
  weight: '',
  length: '',
  width: '',
  height: '',
  unit: 'cm'
})

const addPackage = () => {
  console.log('Add another package')
}

const calculateRate = () => {
  console.log('Quote data:', quote)

  // API call will go here later.
}
</script>

<template>
  <section id="quote" class="quote-section">

    <div class="section-container quote-layout">
      <div class="quote-information">
        <h2 class="quote-title">
          Instant Logistics Intelligence
        </h2>

        <p class="quote-description">
          Calculate shipping costs, transit times, and carbon footprint
          in seconds. Our algorithmic quoting engine provides guaranteed
          rates you can trust.
        </p>

        <ul class="quote-benefits">

          <li class="quote-benefit">
            <span class="material-symbols-outlined">
              check_circle
            </span>
            <span>
              Upfront, transparent pricing
            </span>
          </li>

          <li class="quote-benefit">
            <span class="material-symbols-outlined">
              check_circle
            </span>

            <span>
              Multiple transit speed options
            </span>
          </li>

          <li class="quote-benefit">
            <span class="material-symbols-outlined">
              check_circle
            </span>

            <span>
              Volume discounts applied automatically
            </span>
          </li>

        </ul>

      </div>


      <!-- Form -->
      <div class="quote-card">

        <div class="quote-header">
          <h3>
            Get an Instant Quote
          </h3>

          <span class="estimator-badge">
            Fast Estimator
          </span>

        </div>


        <form
          class="quote-form"
          @submit.prevent="calculateRate"
        >

          <div class="form-group">

            <label
              for="quote-from"
              class="form-label"
            >
              Origin (From)
            </label>

            <div class="input-wrapper">

              <span class="material-symbols-outlined input-icon">
                trip_origin
              </span>

              <input
                id="quote-from"
                v-model="quote.from"
                class="form-input"
                type="text"
                placeholder="City or ZIP code"
              />

            </div>

          </div>

          <div class="form-group">

            <label
              for="quote-to"
              class="form-label"
            >
              Destination (To)
            </label>

            <div class="input-wrapper">

              <span class="material-symbols-outlined input-icon">
                place
              </span>

              <input
                id="quote-to"
                v-model="quote.to"
                class="form-input"
                type="text"
                placeholder="City or ZIP code"
              />

            </div>

          </div>


          <!-- Package -->
          <div class="form-group full-width">

            <label class="form-label">
              Package Details
            </label>

            <div class="package-fields">

              <div class="weight-field">

                <div class="input-wrapper">

                  <span class="material-symbols-outlined input-icon">
                    scale
                  </span>

                  <input
                    v-model="quote.weight"
                    class="form-input"
                    type="number"
                    placeholder="Weight (kg)"
                  />

                </div>

              </div>


              <div class="dimensions">

                <input
                  v-model="quote.length"
                  class="form-input dimension-input"
                  type="number"
                  placeholder="L"
                />

                <span class="dimension-separator">
                  ×
                </span>

                <input
                  v-model="quote.width"
                  class="form-input dimension-input"
                  type="number"
                  placeholder="W"
                />

                <span class="dimension-separator">
                  ×
                </span>

                <input
                  v-model="quote.height"
                  class="form-input dimension-input"
                  type="number"
                  placeholder="H"
                />

                <select
                  v-model="quote.unit"
                  class="form-select"
                >
                  <option value="cm">
                    cm
                  </option>

                  <option value="in">
                    inch
                  </option>
                </select>

              </div>

            </div>

          </div>


          <!-- Actions -->
          <div class="form-actions">

            <button
              type="button"
              class="add-package"
              @click="addPackage"
            >
              + Add another package
            </button>

            <button
              type="submit"
              class="calculate-button"
            >
              Calculate Rate

              <span class="material-symbols-outlined">
                calculate
              </span>
            </button>

          </div>

        </form>

      </div>

    </div>

  </section>
</template>

<style scoped>
.quote-section {
  padding: 100px;
  /* background: var(--light); */
  background: #DBE1FF;
  padding-bottom:100px;
}

.quote-layout {
  display: grid;

  grid-template-columns: 5fr 7fr;

  gap: 48px;

  align-items: center;
}

.quote-information {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding-left: 50px;
}

.quote-title {
  margin: 0;

  color: var(--dark-alt);

  font-size: 32px;
  line-height: 40px;
  font-weight: 700;
}

.quote-description {
  margin: 0;

  color: var(--on-surface-variant);

  font-size: 18px;
  line-height: 28px;
}

.quote-benefits {
  margin: 16px 0 0;
  padding: 0;

  list-style: none;

  display: flex;
  flex-direction: column;
  gap: 16px;
}

.quote-benefit {
  display: flex;
  align-items: flex-start;
  gap: 12px;

  color: var(--on-surface);
}

.quote-benefit .material-symbols-outlined {
  color: var(--secondary);
}


/* =========================================================
   QUOTE CARD
   ========================================================= */

.quote-card {
  padding: 32px;

  background: var(--light);

  border: 1px solid var(--border-muted);
  border-radius: 8px;

  box-shadow:
    0 4px 20px rgba(42, 42, 61, 0.08);
}

.quote-header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  margin-bottom: 24px;
  padding-bottom: 16px;

  border-bottom: 1px solid var(--border-muted);
}

.quote-header h3 {
  margin: 0;

  color: var(--primary);

  font-size: 24px;
  line-height: 32px;
  font-weight: 600;
}

.estimator-badge {
  padding: 4px 12px;

  border-radius: 4px;

  background: var(--surface-container-low);
  color: var(--primary);

  font-family: var(--font-mono);
  font-size: 12px;
  font-weight: 700;

  letter-spacing: 0.08em;
  text-transform: uppercase;
}


/* =========================================================
   FORM
   ========================================================= */

.quote-form {
  display: grid;

  grid-template-columns: repeat(2, 1fr);

  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-label {
  color: var(--on-surface-variant);

  font-size: 14px;
  line-height: 20px;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;

  left: 12px;
  top: 50%;

  transform: translateY(-50%);

  color: var(--primary);
}

.form-input {
  /* width: 100%; */

  padding: 10px 16px;

  border: 1px solid var(--dark);
  border-radius: 4px;

  background: var(--background);

  color: var(--on-surface);

  transition: border 0.2s ease;
}

.input-wrapper .form-input {
  padding-left: 40px;
}

.form-input:focus,
.form-select:focus {
  outline: none;

  border-color: var(--primary);
  border-width: 2px;
}

.package-fields {
  display: flex;
  gap: 16px;
}

.weight-field {
  flex: 1;
}

.dimensions {
  flex: 2;

  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.dimension-input {
  text-align: center;
  min-width: 0;
}

.dimension-separator {
  color: var(--outline-variant);
}

.form-select {
  padding: 10px 12px;

  border: 1px solid var(--border-muted);
  border-radius: 4px;

  background: var(--background);

  color: var(--on-surface);
}

.form-actions {
  grid-column: 1 / -1;

  margin-top: 16px;

  display: flex;
  align-items: center;
  justify-content: space-between;
}

.add-package {
  border: none;
  background: transparent;

  color: var(--primary);

  font-size: 14px;
  font-weight: 500;
}

.add-package:hover {
  text-decoration: underline;
}

.calculate-button {
  border: none;
  border-radius: 4px;

  padding: 12px 32px;

  display: flex;
  align-items: center;
  gap: 8px;

  background: var(--primary);
  color: var(--light);

  font-size: 14px;
  font-weight: 500;

  transition: background 0.2s ease;
}

.calculate-button:hover {
  background: var(--primary);
}
</style>