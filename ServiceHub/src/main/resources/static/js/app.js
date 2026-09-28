document.addEventListener('DOMContentLoaded', () => {
    const menuToggle = document.querySelector('[data-menu-toggle]');
    const navLinks = document.querySelector('[data-nav-links]');
    menuToggle?.addEventListener('click', () => {
        navLinks?.classList.toggle('is-open');
        menuToggle.setAttribute('aria-expanded', navLinks?.classList.contains('is-open'));
    });
});
