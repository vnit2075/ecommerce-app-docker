// ============================================
// ShopEZ - Main JavaScript
// ============================================

document.addEventListener('DOMContentLoaded', function () {

  // --- Mobile Nav Toggle ---
  const hamburger = document.getElementById('hamburger');
  const mobileNav = document.getElementById('mobileNav');
  if (hamburger && mobileNav) {
    hamburger.addEventListener('click', () => {
      mobileNav.classList.toggle('open');
    });
  }

  // --- User Dropdown Toggle (click) ---
  const userMenuTrigger = document.getElementById('userMenuTrigger');
  const userDropdown = document.getElementById('userDropdown');
  if (userMenuTrigger && userDropdown) {
    userMenuTrigger.addEventListener('click', (e) => {
      e.stopPropagation();
      const parent = userMenuTrigger.closest('.nav-user-menu');
      parent.classList.toggle('open');
    });
    document.addEventListener('click', () => {
      const parent = userMenuTrigger.closest('.nav-user-menu');
      if (parent) parent.classList.remove('open');
    });
  }

  // --- Auto-dismiss Alerts ---
  const alerts = document.querySelectorAll('.alert');
  alerts.forEach(alert => {
    setTimeout(() => {
      alert.style.transition = 'opacity .5s ease';
      alert.style.opacity = '0';
      setTimeout(() => alert.remove(), 500);
    }, 4000);
  });

  // --- Scroll-based Navbar Shadow ---
  const navbar = document.querySelector('.navbar');
  if (navbar) {
    window.addEventListener('scroll', () => {
      if (window.scrollY > 10) {
        navbar.style.boxShadow = '0 4px 24px rgba(108,59,255,.15)';
      } else {
        navbar.style.boxShadow = '';
      }
    });
  }

  // --- Product Card Hover Animation ---
  const productCards = document.querySelectorAll('.product-card');
  productCards.forEach(card => {
    card.addEventListener('mouseenter', () => {
      card.style.willChange = 'transform';
    });
    card.addEventListener('mouseleave', () => {
      card.style.willChange = '';
    });
  });

  // --- Smooth Scroll for Hero CTA ---
  const scrollLinks = document.querySelectorAll('a[href^="#"]');
  scrollLinks.forEach(link => {
    link.addEventListener('click', (e) => {
      const target = document.querySelector(link.getAttribute('href'));
      if (target) {
        e.preventDefault();
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    });
  });

  // --- Category Card Entrance Animation ---
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.style.animation = 'fadeInUp .5s ease forwards';
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.1 });

  document.querySelectorAll('.category-card, .product-card, .stat-card').forEach(el => {
    el.style.opacity = '0';
    observer.observe(el);
  });

  // Inject fadeInUp keyframe
  if (!document.getElementById('shop-animations')) {
    const style = document.createElement('style');
    style.id = 'shop-animations';
    style.textContent = `
      @keyframes fadeInUp {
        from { opacity: 0; transform: translateY(20px); }
        to   { opacity: 1; transform: translateY(0); }
      }
    `;
    document.head.appendChild(style);
  }

  // --- Add to Cart button ripple effect ---
  document.querySelectorAll('.btn-add-cart, .btn-primary').forEach(btn => {
    btn.addEventListener('click', function (e) {
      const ripple = document.createElement('span');
      ripple.style.cssText = `
        position:absolute; border-radius:50%; background:rgba(255,255,255,.4);
        transform:scale(0); animation:ripple .5s linear;
        width:80px; height:80px; pointer-events:none;
      `;
      const rect = this.getBoundingClientRect();
      ripple.style.left = (e.clientX - rect.left - 40) + 'px';
      ripple.style.top  = (e.clientY - rect.top  - 40) + 'px';
      this.style.position = 'relative';
      this.style.overflow = 'hidden';
      this.appendChild(ripple);
      setTimeout(() => ripple.remove(), 600);
    });
  });

  const rippleStyle = document.createElement('style');
  rippleStyle.textContent = `
    @keyframes ripple { to { transform: scale(4); opacity: 0; } }
  `;
  document.head.appendChild(rippleStyle);

  // --- Admin table search ---
  const adminSearch = document.getElementById('adminTableSearch');
  if (adminSearch) {
    adminSearch.addEventListener('input', function () {
      const query = this.value.toLowerCase();
      const rows = document.querySelectorAll('.admin-table tbody tr');
      rows.forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(query) ? '' : 'none';
      });
    });
  }

  // --- Checkout form validation ---
  const checkoutForm = document.querySelector('.checkout-form');
  if (checkoutForm) {
    checkoutForm.addEventListener('submit', function (e) {
      const zip = document.getElementById('zip');
      if (zip && !/^\d{6}$/.test(zip.value)) {
        e.preventDefault();
        zip.style.borderColor = '#ef4444';
        zip.focus();
        alert('Please enter a valid 6-digit PIN code.');
        return;
      }
      const submitBtn = checkoutForm.querySelector('[type="submit"]');
      if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Placing Order...';
      }
    });
  }

  console.log('ShopEZ loaded ✓');
});
