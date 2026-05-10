// =====================================================
// router.js — carrega fragmentos HTML nas rotas
// =====================================================

const routes = {
    '/':          'js/views/login.html',
    '/login':     'js/views/login.html',
    '/dashboard': 'js/views/dashboard.html',
};

async function navigate(path) {
    const file = routes[path] || routes['/'];

    try {
        const res  = await fetch(file);
        const html = await res.text();
        document.getElementById('app').innerHTML = html;
    } catch (e) {
        document.getElementById('app').innerHTML =
            '<p style="padding:2rem;color:#f87171">Erro ao carregar a página.</p>';
    }
}

// Escuta cliques em links internos
document.addEventListener('click', (e) => {
    const link = e.target.closest('[data-route]');
    if (!link) return;
    e.preventDefault();
    const path = link.dataset.route;
    history.pushState({}, '', path);
    navigate(path);
});

// Botão voltar do browser
window.addEventListener('popstate', () => {
    navigate(window.location.pathname);
});

// Exporta para uso no app.js
window.navigate = navigate;