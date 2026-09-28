document.addEventListener('DOMContentLoaded', () => {
    const menuToggle = document.querySelector('[data-menu-toggle]');
    const navLinks = document.querySelector('[data-nav-links]');

    menuToggle?.addEventListener('click', () => {
        navLinks?.classList.toggle('is-open');
        menuToggle.setAttribute('aria-expanded', navLinks?.classList.contains('is-open'));
    });

    document.querySelectorAll('[data-demo-form]').forEach((form) => {
        form.addEventListener('submit', (event) => {
            event.preventDefault();
            const notice = form.querySelector('[data-form-notice]');
            if (notice) {
                notice.textContent = 'Thanks! This demo form is ready for backend integration.';
                notice.hidden = false;
            }
        });
    });
});
