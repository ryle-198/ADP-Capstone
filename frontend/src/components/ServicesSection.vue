<script setup>
const services = [
  {
    title: 'Domestic Express',
    description:
      'Next-day and same-day delivery options across the nation. Precision timing for your most critical local shipments.',
    icon: 'local_shipping',
    style: 'primary'
  },
  {
    title: 'Warehouse Solutions',
    description:
      'Scalable storage, pick-and-pack services, and inventory management integrated directly with your fulfillment network.',
    icon: 'inventory_2',
    style: 'secondary'
  }
]
</script>

<template>
  <section id="services" class="services-section">

    <div class="section-container">

      <div class="section-heading">

        <span class="section-eyebrow">
          Our Capabilities
        </span>

        <h2 class="section-title">
          High-Performance Solutions
        </h2>

      </div>

      <div class="services-grid">

        <article
          v-for="service in services"
          :key="service.title"
          class="service-card"
        >

          <div
            class="service-icon"
            :class="service.style"
          >
            <span class="material-symbols-outlined fill">
              {{ service.icon }}
            </span>
          </div>

          <h3 class="service-title">
            {{ service.title }}
          </h3>

          <p class="service-description">
            {{ service.description }}
          </p>

          <a href="#" class="learn-more">
            Learn more

            <span class="material-symbols-outlined">
              chevron_right
            </span>
          </a>

        </article>

      </div>

    </div>

  </section>
</template>

<style>
.services-section {
  padding: 100px;
  /* background: #F2EFFF; */
  background: #e5e5fb
}

.section-container {
  padding-top:60px;
  padding-bottom:93px;  
  width: 100%;
  max-width: var(--container-max);
  /* margin: 0 auto; */
}

.section-heading {
  margin-bottom: 64px;

  text-align: center;
}

.section-eyebrow {
  display: block;
  padding-top: 40px;  
  margin-bottom: 8px;

  color: var(--dark-alt);

  font-family: var(--font-mono);
  font-size: 12px;
  font-weight: 700;

  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.section-title {
  color: var(--dark);

  font-size: 32px;
  line-height: 40px;
  font-weight: 700;
}

.services-grid {
  max-width: 1000px;
  margin: 0 auto;

  display: grid;
  grid-template-columns: repeat(2, 1fr);

  gap: 24px;
}

.service-card {
  display: flex;
  flex-direction: column;

  min-height: 300px;

  padding: 32px;

  /* background: #C6C4DD; */
    background: #F2EFFF;


  border: 1px solid var(--dark-alt);
  border-radius: 8px;

  transition:
    border-color 0.3s ease,
    background 0.3s ease,
    transform 0.3s ease;
}

/* .service-card:hover {
  border-color: var(--primary);
  background: var(--surface-container-low);

  transform: translateY(-2px);
} */

.service-icon {
  width: 48px;
  height: 48px;

  margin-bottom: 24px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;

  /* color: var(--primary); */

  transition: transform 0.3s ease;
}

.service-card:hover .service-icon {
  transform: scale(1.1);
}

.service-icon.primary {
  background: var(--primary-fixed);
}

.service-icon.secondary {
  background: var(--surface-variant);
}

.service-title {
  margin: 0 0 12px;

  color: var(--dark-alt);

  font-size: 24px;
  line-height: 32px;
  font-weight: 600;
}

.service-description {
  flex: 1;

  margin: 0 0 24px;

  color: var(--dark);

  font-size: 16px;
  line-height: 24px;
}

.learn-more {
  display: inline-flex;
  align-items: center;
  gap: 4px;

  margin-top: auto;

  color: var(--dark);

  font-size: 14px;
  font-weight: 500;

  transition: color 0.2s ease;
  text-decoration: none;
}
</style>