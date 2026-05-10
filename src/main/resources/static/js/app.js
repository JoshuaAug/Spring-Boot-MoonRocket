// =====================================================
// app.js — ponto de entrada da SPA
// =====================================================

document.addEventListener('DOMContentLoaded', () => {
    // Rota inicial: decide com base na URL atual
    const path = window.location.pathname;
    navigate(path === '/' ? '/login' : path);
});