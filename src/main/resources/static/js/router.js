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
        const app  = document.getElementById('app');

        // innerHTML não executa <script> — precisamos recriar os nós manualmente
        app.innerHTML = html;
        reexecutarScripts(app);

    } catch (e) {
        document.getElementById('app').innerHTML =
            '<p style="padding:2rem;color:#f87171">Erro ao carregar a página.</p>';
    }
}

// Recria e executa cada <script> injetado via innerHTML
function reexecutarScripts(container) {
    container.querySelectorAll('script').forEach(scriptAntigo => {
        const scriptNovo = document.createElement('script');

        // Copia atributos (src, type, etc.)
        Array.from(scriptAntigo.attributes).forEach(attr => {
            scriptNovo.setAttribute(attr.name, attr.value);
        });

        // Copia o conteúdo inline
        scriptNovo.textContent = scriptAntigo.textContent;

        // Substitui o script inerte pelo novo (que vai executar)
        scriptAntigo.parentNode.replaceChild(scriptNovo, scriptAntigo);
    });
}

// Escuta cliques em links internos com data-route
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

window.navigate = navigate;