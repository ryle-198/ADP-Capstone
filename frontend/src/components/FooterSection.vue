<template>
  <footer id="support" class="footer">

    <div class="footer-grid">

      <!-- Brand -->
      <div class="footer-brand">

        <span class="footer-logo">
          SWIFT
        </span>

        <p class="footer-copyright">
          © 2026 SWIFT Logistics.
          Reliable. Fast. Precise.
        </p>

      </div>


      <!-- Company -->
      <div class="footer-column">

        <span class="footer-heading">
          Company
        </span>

        <a href="#" class="footer-link">
          About Us
        </a>

        <a href="#" class="footer-link">
          Careers
        </a>

        <a href="#" class="footer-link">
          Global Network
        </a>

      </div>


      <!-- Legal -->
      <div class="footer-column">

        <span class="footer-heading">
          Legal
        </span>

        <a href="#" class="footer-link">
          Privacy Policy
        </a>

        <a href="#" class="footer-link">
          Terms of Service
        </a>

      </div>


      <!-- Support -->
      <div class="footer-column">

        <span class="footer-heading">
          Support
        </span>

        <a href="#" class="footer-link">
          <!-- <span class="material-symbols-outlined">
             mail 
          </span> -->

          Contact Support
        </a>

        <a href="#" class="footer-link">
          <!--<span class="material-symbols-outlined">
            help_center 
          </span>-->

          Help Center
        </a>

      </div>

    </div>

  </footer>
</template>

<style scoped>
.footer {
  background: var(--primary);

  border-top: 1px solid var(--primary-container);
}

.footer-grid {
  max-width: var(--container-max);
  margin: 0 auto;

  padding: 48px var(--desktop-margin);

  display: grid;

  grid-template-columns: repeat(4, 1fr);

  gap: 24px;
}

.footer-brand {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.footer-logo {
  color: var(--on-primary);

  font-size: 24px;
  line-height: 32px;
  font-weight: 800;
}

.footer-copyright {
  margin-top: auto;

  color: var(--on-primary-container);

  opacity: 0.8;
}

.footer-column {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.footer-heading {
  margin-bottom: 8px;

  color: var(--on-primary-container);

  font-family: var(--font-mono);
  font-size: 12px;
  font-weight: 700;

  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.footer-link {
  color: var(--on-primary-container);

  opacity: 0.8;

  transition: color 0.2s ease;
}

.footer-link:hover {
  color: var(--secondary-fixed);
  opacity: 1;
}
</style>