// ---- Estado ----
let _foguetes = [];
let _satelites = [];
let _missoes = [];

// ---- Carregamento inicial ----
async function carregarTudo() {
    try {
        [_foguetes, _satelites, _missoes] = await Promise.all([
            FogueteAPI.listar(),
            SateliteAPI.listar(),
            MissaoAPI.listar(),
        ]);
        renderSummary();
        renderFoguetes();
        renderSatelites();
        renderMissoes();
    } catch (e) {
        console.error('Erro ao conectar com o backend:', e.message);
    }
}

function renderSummary() {
    document.getElementById('count-foguetes').textContent = _foguetes.length;
    document.getElementById('count-satelites').textContent = _satelites.length;
    document.getElementById('count-missoes').textContent = _missoes.length;
    document.getElementById('count-ativas').textContent = _missoes.filter(m => m.status === 'EM_ANDAMENTO').length;
}

// ---- Foguetes ----
function renderFoguetes() {
    const tbody = document.getElementById('tabela-foguetes');
    if (!_foguetes.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Nenhum foguete cadastrado.</td></tr>';
        return;
    }
    tbody.innerHTML = _foguetes.map(f => `
  <tr>
    <td>${f.nome}</td>
    <td>${f.combustivel} ton</td>
    <td>${f.carga} kg</td>
    <td>${badgeStatus(f.status)}</td>
    <td>${f.distanciaMaxima != null ? f.distanciaMaxima.toFixed(0) + ' km' : '—'}</td>
    <td><button class="btn btn-ghost btn-sm" onclick="window.deletarFoguete(${f.id})">🗑️</button></td>
  </tr>`).join('');
}

window.criarFoguete = async function () {
    const nome = document.getElementById('fog-nome').value.trim();
    const combustivel = parseFloat(document.getElementById('fog-combustivel').value);
    const carga = parseFloat(document.getElementById('fog-carga').value);
    if (!nome || isNaN(combustivel) || isNaN(carga)) {
        mostrarErroModal('fog-erro', 'Preencha todos os campos.');
        return;
    }
    try {
        await FogueteAPI.criar({nome, combustivel, carga});
        window.fecharModal('modal-foguete');
        limparCampos(['fog-nome', 'fog-combustivel', 'fog-carga']);
        await carregarTudo();
    } catch (e) {
        mostrarErroModal('fog-erro', e.message);
    }
};

window.deletarFoguete = async function (id) {
    if (!confirm('Deletar este foguete?')) return;
    try {
        await FogueteAPI.deletar(id);
        await carregarTudo();
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

// ---- Satélites ----
function renderSatelites() {
    const tbody = document.getElementById('tabela-satelites');
    if (!_satelites.length) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhum satélite cadastrado.</td></tr>';
        return;
    }
    tbody.innerHTML = _satelites.map(s => `
  <tr>
    <td>${s.nome}</td>
    <td>${s.massa} kg</td>
    <td>
      <div class="energia-bar"><div class="energia-fill" style="width:${s.energia}%;background:${energiaCor(s.energia)}"></div></div>
      <span style="font-size:0.78rem;color:var(--text-2)">${s.energia.toFixed(0)}%</span>
    </td>
    <td>${badgeStatus(s.status)}</td>
    <td>
      <button class="btn btn-ghost btn-sm" title="Painéis solares" onclick="window.ativarPaineis(${s.id})">☀️</button>
      <button class="btn btn-ghost btn-sm" title="Deletar" onclick="window.deletarSatelite(${s.id})">🗑️</button>
    </td>
  </tr>`).join('');
}

window.criarSatelite = async function () {
    const nome = document.getElementById('sat-nome').value.trim();
    const massa = parseFloat(document.getElementById('sat-massa').value);
    if (!nome || isNaN(massa)) {
        mostrarErroModal('sat-erro', 'Preencha todos os campos.');
        return;
    }
    try {
        await SateliteAPI.criar({nome, massa});
        window.fecharModal('modal-satelite');
        limparCampos(['sat-nome', 'sat-massa']);
        await carregarTudo();
    } catch (e) {
        mostrarErroModal('sat-erro', e.message);
    }
};

window.ativarPaineis = async function (id) {
    try {
        await SateliteAPI.paineis(id);
        await carregarTudo();
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

window.deletarSatelite = async function (id) {
    if (!confirm('Deletar este satélite?')) return;
    try {
        await SateliteAPI.deletar(id);
        await carregarTudo();
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

// ---- Missões ----
function renderMissoes() {
    const tbody = document.getElementById('tabela-missoes');
    if (!_missoes.length) {
        tbody.innerHTML = '<tr><td colspan="7" class="empty-state">Nenhuma missão cadastrada.</td></tr>';
        return;
    }
    tbody.innerHTML = _missoes.map(m => `
  <tr>
    <td>${m.nome}</td>
    <td>${m.objetivo}</td>
    <td><span class="badge badge-info">${m.orbita ?? '—'}</span></td>
    <td>${m.foguete?.nome ?? '—'}</td>
    <td>${m.satelite?.nome ?? '—'}</td>
    <td>${badgeStatus(m.status)}</td>
    <td><button class="btn btn-ghost btn-sm" onclick="window.abrirDetalhe(${m.id})">Ver</button></td>
  </tr>`).join('');
}

window.criarMissao = async function () {
    const nome = document.getElementById('mis-nome').value.trim();
    const objetivo = document.getElementById('mis-objetivo').value;
    const orbita = document.getElementById('mis-orbita').value;
    const fogueteId = parseInt(document.getElementById('mis-foguete').value);
    const sateliteId = parseInt(document.getElementById('mis-satelite').value);
    if (!nome || isNaN(fogueteId) || isNaN(sateliteId)) {
        mostrarErroModal('mis-erro', 'Preencha todos os campos.');
        return;
    }
    try {
        await MissaoAPI.criar({nome, objetivo, orbita, fogueteId, sateliteId});
        window.fecharModal('modal-missao');
        limparCampos(['mis-nome']);
        await carregarTudo();
    } catch (e) {
        mostrarErroModal('mis-erro', e.message);
    }
};

window.abrirDetalhe = async function (id) {
    await carregarTudo();
    const m = _missoes.find(x => x.id === id);
    if (!m) return;

    document.getElementById('detalhe-nome').textContent = m.nome;
    document.getElementById('detalhe-info').innerHTML =
        `<b>Objetivo:</b> ${m.objetivo} &nbsp;|&nbsp;
   <b>Órbita:</b> ${m.orbita ?? '—'} &nbsp;|&nbsp;
   <b>Foguete:</b> ${m.foguete?.nome ?? '—'} &nbsp;|&nbsp;
   <b>Satélite:</b> ${m.satelite?.nome ?? '—'} &nbsp;|&nbsp;
   <b>Status:</b> ${m.status}`;

    const acoes = document.getElementById('detalhe-acoes');
    acoes.innerHTML = '';
    if (m.status === 'PREPARANDO')
        acoes.innerHTML += `<button class="btn btn-primary btn-sm" onclick="window.iniciarMissao(${m.id})">🚀 Iniciar</button>`;
    if (m.status === 'EM_ANDAMENTO') {
        acoes.innerHTML += `<button class="btn btn-secondary btn-sm" onclick="window.usarRadar(${m.id})">📡 Radar</button>`;
        acoes.innerHTML += `<button class="btn btn-secondary btn-sm" onclick="window.ativarPaineisMissao(${m.id})">☀️ Painéis</button>`;
        acoes.innerHTML += `<button class="btn btn-danger btn-sm" onclick="window.encerrarMissao(${m.id})">⏹ Encerrar</button>`;
    }

    const itens = [...(m.dados || []), ...(m.mensagens || [])];
    document.getElementById('detalhe-log').innerHTML = itens.length
        ? itens.map(i => `<div class="log-item">▸ ${i}</div>`).join('')
        : '<p style="color:var(--text-3);font-size:0.82rem">Nenhum dado registrado ainda.</p>';

    window.abrirModal('modal-detalhe-missao');
};

window.iniciarMissao = async function (id) {
    try {
        await MissaoAPI.iniciar(id);
        window.fecharModal('modal-detalhe-missao');
        await carregarTudo();
        window.abrirDetalhe(id);
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

window.encerrarMissao = async function (id) {
    if (!confirm('Encerrar a missão?')) return;
    try {
        await MissaoAPI.encerrar(id);
        window.fecharModal('modal-detalhe-missao');
        await carregarTudo();
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

window.usarRadar = async function (id) {
    try {
        const res = await MissaoAPI.radar(id);
        const log = document.getElementById('detalhe-log');
        const novo = document.createElement('div');
        novo.className = 'log-item log-item--new';
        novo.textContent = '▸ ' + res.descricao + ' (Energia: ' + (res.energiaRestante?.toFixed(0) ?? '?') + '%)';
        log.prepend(novo);
        await carregarTudo();
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

window.ativarPaineisMissao = async function (id) {
    try {
        await MissaoAPI.paineis(id);
        await carregarTudo();
        window.abrirDetalhe(id);
    } catch (e) {
        alert('Erro: ' + e.message);
    }
};

// ---- Navegação de seções ----
window.showSection = function (id, el) {
    document.querySelectorAll('.dash-section').forEach(s => s.classList.add('hidden'));
    document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));
    document.getElementById('section-' + id)?.classList.remove('hidden');
    if (el) el.classList.add('active');
    if (id === 'missoes') popularSelectsMissao();
};

function popularSelectsMissao() {
    document.getElementById('mis-foguete').innerHTML = _foguetes.map(f => `<option value="${f.id}">${f.nome}</option>`).join('');
    document.getElementById('mis-satelite').innerHTML = _satelites.map(s => `<option value="${s.id}">${s.nome}</option>`).join('');
}

// ---- Modais ----
window.abrirModal = function (id) {
    if (id === 'modal-missao') popularSelectsMissao();
    document.getElementById(id)?.classList.remove('hidden');
};

window.fecharModal = function (id) {
    document.getElementById(id)?.classList.add('hidden');
    document.querySelectorAll('.modal-error').forEach(e => e.classList.add('hidden'));
};

// ---- Logout ----
window.logout = function () {
    sessionStorage.clear();
    navigate('/login');
};

// ---- Helpers ----
function mostrarErroModal(id, msg) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = msg;
    el.classList.remove('hidden');
}

function limparCampos(ids) {
    ids.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.value = '';
    });
}

function badgeStatus(s) {
    const m = {
        EM_SOLO: 'badge-neutral',
        EM_ORBITA: 'badge-info',
        EM_MISSAO: 'badge-info',
        FALHA: 'badge-danger',
        SEM_ENERGIA: 'badge-danger',
        INATIVO: 'badge-neutral',
        PREPARANDO: 'badge-warning',
        EM_ANDAMENTO: 'badge-success',
        CONCLUIDA: 'badge-success',
        FALHOU: 'badge-danger'
    };
    const l = {
        EM_SOLO: 'Em solo',
        EM_ORBITA: 'Em órbita',
        EM_MISSAO: 'Em missão',
        FALHA: 'Falha',
        SEM_ENERGIA: 'Sem energia',
        INATIVO: 'Inativo',
        PREPARANDO: 'Preparando',
        EM_ANDAMENTO: 'Em andamento',
        CONCLUIDA: 'Concluída',
        FALHOU: 'Falhou'
    };
    return `<span class="badge ${m[s] || 'badge-neutral'}">${l[s] || s}</span>`;
}

function energiaCor(e) {
    return e > 60 ? 'var(--success)' : e > 30 ? 'var(--warning)' : 'var(--danger)';
}

// ---- Inicia ----
carregarTudo();
// ---- Asteroides NASA ----
window.buscarAsteroides = async function () {
    const dataInicio = document.getElementById('neo-data-inicio').value;
    const dataFim    = document.getElementById('neo-data-fim').value;
    const erro       = document.getElementById('neo-erro');
    const tbody      = document.getElementById('tabela-asteroides');

    if (!dataInicio) {
        erro.textContent = 'Informe ao menos a data de início.';
        erro.classList.remove('hidden');
        return;
    }
    erro.classList.add('hidden');
    tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Buscando...</td></tr>';

    try {
        const asteroides = await NasaAPI.asteroides(dataInicio, dataFim || null);

        if (!asteroides.length) {
            tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Nenhum asteroide encontrado nesse período.</td></tr>';
            return;
        }

        tbody.innerHTML = asteroides.map(a => `
        <tr>
            <td>${a.nome}</td>
            <td>${a.diametroMinKm.toFixed(3)} – ${a.diametroMaxKm.toFixed(3)}</td>
            <td>${a.velocidadeKmH.toFixed(0)}</td>
            <td>${a.distanciaKm.toFixed(0)}</td>
            <td>${a.dataAproximacao}</td>
            <td>${a.potencialmentePerigoso
            ? '<span class="badge badge-danger">⚠ Perigoso</span>'
            : '<span class="badge badge-neutral">Seguro</span>'}</td>
        </tr>`).join('');
    } catch (e) {
        erro.textContent = 'Erro ao buscar asteroides: ' + e.message;
        erro.classList.remove('hidden');
        tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Falha na busca.</td></tr>';
    }
};