// =====================================================
// api.js — comunicação com o backend Spring Boot
// =====================================================

const API = 'http://localhost:8080/api';

async function req(method, path, body = null) {
  const opts = {
    method,
    headers: { 'Content-Type': 'application/json' },
  };
  if (body) opts.body = JSON.stringify(body);

  const res = await fetch(API + path, opts);

  if (!res.ok) {
    const err = await res.text();
    throw new Error(err || `Erro ${res.status}`);
  }

  // 204 No Content (DELETE) não tem body
  if (res.status === 204) return null;
  return res.json();
}

// ---------- FOGUETES ----------
const FogueteAPI = {
  listar:     ()           => req('GET',    '/foguetes'),
  criar:      (data)       => req('POST',   '/foguetes', data),
  atualizar:  (id, data)   => req('PUT',    `/foguetes/${id}`, data),
  deletar:    (id)         => req('DELETE', `/foguetes/${id}`),
  abastecer:  (id, qtd)    => req('POST',   `/foguetes/${id}/abastecer?quantidade=${qtd}`),
};

// ---------- SATÉLITES ----------
const SateliteAPI = {
  listar:     ()         => req('GET',    '/satelites'),
  criar:      (data)     => req('POST',   '/satelites', data),
  atualizar:  (id, data) => req('PUT',    `/satelites/${id}`, data),
  deletar:    (id)       => req('DELETE', `/satelites/${id}`),
  paineis:    (id)       => req('POST',   `/satelites/${id}/paineis`),
};

// ---------- MISSÕES ----------
const MissaoAPI = {
  listar:     ()           => req('GET',  '/missoes'),
  criar:      (data)       => req('POST', '/missoes', data),
  iniciar:    (id)         => req('POST', `/missoes/${id}/iniciar`),
  encerrar:   (id)         => req('POST', `/missoes/${id}/encerrar`),
  radar:      (id)         => req('POST', `/missoes/${id}/radar`),
  mensagem:   (id, texto)  => req('POST', `/missoes/${id}/mensagem`, { mensagem: texto }),
  paineis:    (id)         => req('POST', `/missoes/${id}/paineis`),
};
// ---------- NASA ----------
const NasaAPI = {
  asteroides: (dataInicio, dataFim) => {
    const params = dataFim
        ? `?dataInicio=${dataInicio}&dataFim=${dataFim}`
        : `?dataInicio=${dataInicio}`;
    return req('GET', `/nasa/asteroides${params}`);
  },
};