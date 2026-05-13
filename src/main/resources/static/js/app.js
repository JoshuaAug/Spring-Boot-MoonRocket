// =====================================================
// app.js — inicialização e lógica de login
// =====================================================

// --- Usuário válido (temporário — substituir por API futuramente) ---
const VALID_USER = { username: 'admin', password: 'Sky' };

// --- Inicialização ---
document.addEventListener('DOMContentLoaded', () => {
  const path = window.location.pathname;
  navigate(path === '/' ? '/login' : path);
});

// --- Login: chamado pelo botão após o fragmento ser carregado ---
function handleLogin() {
  const username = document.getElementById('login-id')?.value.trim();
  const password = document.getElementById('login-pass')?.value.trim();

  clearLoginError();

  if (!username || !password) {
    showLoginError('Preencha todos os campos.');
    return;
  }

  if (username !== VALID_USER.username || password !== VALID_USER.password) {
    showLoginError('ID ou senha incorretos.');
    shakeForm();
    return;
  }

  // Login bem-sucedido
  sessionStorage.setItem('user', JSON.stringify({ username }));
  navigate('/dashboard');
}

// --- Helpers de feedback ---
function showLoginError(msg) {
  let el = document.getElementById('login-error');
  if (!el) {
    el = document.createElement('p');
    el.id = 'login-error';
    el.className = 'login-error';
    const btn = document.querySelector('.login-btn');
    btn?.insertAdjacentElement('beforebegin', el);
  }
  el.textContent = msg;
}

function clearLoginError() {
  document.getElementById('login-error')?.remove();
}

function shakeForm() {
  const box = document.querySelector('.login-form-box');
  if (!box) return;
  box.classList.add('shake');
  box.addEventListener('animationend', () => box.classList.remove('shake'), { once: true });
}

// --- Permite submeter com Enter ---
document.addEventListener('keydown', (e) => {
  if (e.key !== 'Enter') return;
  if (document.getElementById('login-id') || document.getElementById('login-pass')) {
    handleLogin();
  }
});