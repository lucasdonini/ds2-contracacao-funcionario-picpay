const configuredUrl = import.meta.env.VITE_API_URL?.trim();
const API_URL = (configuredUrl || 'http://localhost:8080/funcionarios').replace(/\/$/, '');

export class ApiError extends Error {
  constructor(message, status, details = {}) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.details = details;
  }
}

async function request(path = '', options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      Accept: 'application/json',
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
  });

  // DELETE retorna 204 e, portanto, não possui corpo para converter em JSON.
  const payload = response.status === 204 ? null : await response.json().catch(() => null);

  if (!response.ok) {
    throw new ApiError(
      payload?.mensagem || 'Não foi possível concluir a solicitação.',
      response.status,
      payload || {},
    );
  }

  return payload;
}

function toQueryString(filters) {
  const params = new URLSearchParams();

  // Campos vazios não podem ser enviados: a API exige ao menos um filtro válido.
  Object.entries(filters).forEach(([key, value]) => {
    if (value !== undefined && value !== null && String(value).trim() !== '') {
      params.set(key, String(value).trim());
    }
  });

  return params.toString();
}

export const funcionariosApi = {
  listar: () => request(),
  buscarPorId: (id) => request(`/${id}`),
  buscar: (filters) => request(`/busca?${toQueryString(filters)}`),
  indicadores: () => request('/indicadores'),
  cadastrar: (data) => request('', { method: 'POST', body: JSON.stringify(data) }),
  atualizar: (id, data) => request(`/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  atualizarParcialmente: (id, data) => request(`/${id}`, { method: 'PATCH', body: JSON.stringify(data) }),
  excluir: (id) => request(`/${id}`, { method: 'DELETE' }),
};
