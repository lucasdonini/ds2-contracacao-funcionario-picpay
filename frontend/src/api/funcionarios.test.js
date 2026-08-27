import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { ApiError, funcionariosApi } from './funcionarios';

function response({ ok = true, status = 200, body = null } = {}) {
  return { ok, status, json: vi.fn().mockResolvedValue(body) };
}

describe('funcionariosApi', () => {
  beforeEach(() => vi.stubGlobal('fetch', vi.fn()));
  afterEach(() => vi.unstubAllGlobals());

  it('lista funcionários na URL configurada', async () => {
    const employees = [{ id: 1, nome: 'Ana' }];
    fetch.mockResolvedValue(response({ body: employees }));

    await expect(funcionariosApi.listar()).resolves.toEqual(employees);
    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8080/funcionarios',
      expect.objectContaining({ headers: expect.objectContaining({ Accept: 'application/json' }) }),
    );
  });

  it('envia somente filtros preenchidos na busca', async () => {
    fetch.mockResolvedValue(response({ body: [] }));

    await funcionariosApi.buscar({ nome: ' Ana ', cargo: '', status: 'APROVADO' });

    expect(fetch.mock.calls[0][0]).toBe(
      'http://localhost:8080/funcionarios/busca?nome=Ana&status=APROVADO',
    );
  });

  it('envia JSON no cadastro', async () => {
    const payload = { nome: 'Ana', email: 'ana@email.com', cargo: 'Dev' };
    fetch.mockResolvedValue(response({ status: 201, body: { id: 1, ...payload } }));

    await funcionariosApi.cadastrar(payload);

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8080/funcionarios',
      expect.objectContaining({
        method: 'POST',
        body: JSON.stringify(payload),
        headers: expect.objectContaining({ 'Content-Type': 'application/json' }),
      }),
    );
  });

  it('aceita resposta 204 sem tentar converter o corpo', async () => {
    const deleteResponse = response({ status: 204 });
    fetch.mockResolvedValue(deleteResponse);

    await expect(funcionariosApi.excluir(7)).resolves.toBeNull();
    expect(deleteResponse.json).not.toHaveBeenCalled();
  });

  it('transforma o erro padronizado do back em ApiError', async () => {
    const body = { mensagem: 'Funcionário não encontrado.', campos: [] };
    fetch.mockResolvedValue(response({ ok: false, status: 404, body }));

    await expect(funcionariosApi.buscarPorId(999)).rejects.toMatchObject({
      name: 'ApiError', status: 404, message: body.mensagem, details: body,
    });
    await funcionariosApi.buscarPorId(999).catch((error) => expect(error).toBeInstanceOf(ApiError));
  });
});

