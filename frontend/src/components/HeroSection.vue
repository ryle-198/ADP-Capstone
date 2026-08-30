<script setup>
import { ref } from 'vue'

const trackingNumber = ref('')

const trackShipment = () => {
  if (!trackingNumber.value.trim()) {
    alert('Please enter your tracking number.')
    return
  }

  console.log('Tracking:', trackingNumber.value)
}
</script>

<template>
  <section id="track" class="hero">

    <div class="hero-background"></div>

    <div class="hero-overlay"></div>

    <div class="hero-content">

      <div class="hero-text">

        <h1 class="hero-title">
          Shipping Simplified.<br />

          <span>
            Reliability Delivered.
          </span>
        </h1>

        <p class="hero-description">
          Streamline your logistics with our high-performance courier
          network. Real-time visibility, uncompromising security, and
          momentum that drives your business forward.
        </p>

        <div class="tracking-card">

          <!-- <div class="tracking-accent"></div> -->

          <label
            for="tracking-number"
            class="tracking-label"
          >
            <span
              class="material-symbols-outlined"
              style="font-size: 18px"
            >
              location_on
            </span>

            Track Your Shipment
          </label>

          <div class="tracking-form">

            <div class="tracking-input-wrapper">

              <span class="material-symbols-outlined tracking-input-icon">
                search
              </span>

              <input
                id="tracking-number"
                v-model="trackingNumber"
                class="tracking-input"
                type="text"
                placeholder="Enter your tracking number"
                @keyup.enter="trackShipment"
              />

            </div>

            <button
              class="track-button"
              @click="trackShipment"
            >
              Track Now

              <span
                class="material-symbols-outlined"
                style="font-size: 18px"
              >
                arrow_forward
              </span>
            </button>

          </div>

        </div>

      </div>

    </div>

  </section>
</template>

<style scoped>
.hero {
  position: relative;

  width: 100%;
  min-height: 700px;

  display: flex;
  align-items: center;

  overflow: hidden;
}

.hero-background {
  position: absolute;
  inset: 0;

  background-image:
    url('../assets/hero-background.jpg');

  background-size: cover;
  background-position: center;

  z-index: 0;
}

.hero-overlay {
  position: absolute;
  inset: 0;

  background:
    linear-gradient(
      90deg,
      rgba(21, 22, 40, 0.94) 0%,
      rgba(21, 22, 40, 0.75) 50%,
      rgba(21, 22, 40, 0) 100%
    );

  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;

  width: 100%;
  max-width: var(--container-max);

  margin: 0 auto;
  padding: 80px var(--desktop-margin);
}

.hero-text {
  max-width: 720px;
  margin-left:2rem;
}

.hero-title {
  margin: 0 0 24px;

  color: var(--on-primary);

  font-size: 48px;
  line-height: 56px;
  font-weight: 800;

  letter-spacing: -0.02em;
}

.hero-title span {
  color:var(--admin-primary);
}

.hero-description {
  max-width: 650px;
    
  margin: 0 0 40px;

  color: var(--surface-container-high);

  font-size: 18px;
  line-height: 28px;
}

.tracking-card {
  position: relative;

  max-width: 650px;

  padding: 24px;

  display: flex;
  flex-direction: column;
  gap: 16px;

  background: white;

  border: 1px solid var(--border-muted);
  border-radius: 8px;

  box-shadow:
    0 4px 20px rgba(42, 42, 61, 0.08);

  overflow: hidden;
}

/* .tracking-accent {
  position: absolute;
  top: 0;
  left: 0;

  width: 100%;
  height: 4px;

  background: var(--secondary);
} */

.tracking-label {
  display: flex;
  align-items: center;
  gap: 8px;

  color: var(--primary);

  font-family: var(--font-mono);
  font-size: 12px;
  line-height: 16px;
  font-weight: 700;

  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.tracking-form {
  display: flex;
  gap: 12px;
}

.tracking-input-wrapper {
  position: relative;
  flex: 1;
}

.tracking-input-icon {
  position: absolute;
  left: 12px;
  top: 50%;

  transform: translateY(-50%);

  color: var(--outline);
}

.tracking-input {
  /* width: 100%; */

  padding: 12px 16px 12px 40px;

  border: 1px solid var(--border-muted);
  border-radius: 4px;

  background: var(--surface);

  color: var(--on-surface);

  transition: border 0.2s ease;
}

.tracking-input:focus {
  outline: none;

  border-color: var(--primary);
  border-width: 2px;
}

.track-button {
  border: none;
  border-radius: 4px;

  padding: 12px 32px;

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  background: var(--primary);
  color: white;

  font-size: 14px;
  font-weight: 500;

  white-space: nowrap;

  transition: background 0.2s ease;
}

.track-button:hover {
  background: var(--secondary);
}
</style>