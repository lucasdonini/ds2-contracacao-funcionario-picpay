import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { funcionariosApi } from '../api/funcionarios';
import DashboardPage from './DashboardPage';

vi.mock('../api/funcionarios', () => ({
  funcionariosApi: {
    listar: vi.fn(),
    indicadores: vi.fn(),
    buscar: vi.fn(),
    atualizarParcialmente: vi.fn(),
    excluir: vi.fn(),
  },
}));

const employee = {
  id: 1,
  nome: 'Ana Silva',
  email: 'ana@email.com',
  cargo: 'Desenvolvedora',
  departamento: 'Tecnologia',
  status: 'EM_ANALISE',
};

describe('DashboardPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    funcionariosApi.listar.mockResolvedValue([employee]);
    funcionariosApi.indicadores.mockResolvedValue({
      totalCandidatos: 1,
      emAnalise: 1,
      aprovados: 0,
      reprovados: 0,
      contratados: 0,
    });
    funcionariosApi.buscar.mockResolvedValue([employee]);
  });

  it('carrega lista e indicadores ao abrir o painel', async () => {
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);

    expect(await screen.findByText('Ana Silva')).toBeInTheDocument();
    expect(funcionariosApi.listar).toHaveBeenCalledOnce();
    expect(funcionariosApi.indicadores).toHaveBeenCalledOnce();
    expect(screen.getByText('1 pessoa')).toBeInTheDocument();
  });

  it('pesquisa usando os filtros preenchidos', async () => {
    const user = userEvent.setup();
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);
    await screen.findByText('Ana Silva');

    await user.type(screen.getByLabelText('Buscar por nome'), 'Ana');
    await user.selectOptions(screen.getByLabelText('Filtrar por status'), 'EM_ANALISE');
    await user.click(screen.getByRole('button', { name: 'Buscar' }));

    await waitFor(() => expect(funcionariosApi.buscar).toHaveBeenCalledWith({
      nome: 'Ana', cargo: '', status: 'EM_ANALISE',
    }));
  });

  it('mostra estado vazio quando não há candidatos', async () => {
    funcionariosApi.listar.mockResolvedValue([]);
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);

    expect(await screen.findByText('Nenhum candidato por aqui')).toBeInTheDocument();
  });
});

