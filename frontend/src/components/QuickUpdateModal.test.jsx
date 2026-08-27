import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import QuickUpdateModal from './QuickUpdateModal';

const employee = { id: 1, nome: 'Ana Silva', cargo: 'Dev', status: 'EM_ANALISE', salario: 5000 };

describe('QuickUpdateModal', () => {
  it('recusa um PATCH sem alterações', async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();
    render(<QuickUpdateModal employee={employee} loading={false} onCancel={vi.fn()} onSubmit={onSubmit} />);

    await user.click(screen.getByRole('button', { name: /atualizar dados/i }));

    expect(screen.getByText('Altere ao menos um dos campos disponíveis.')).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('envia somente os campos alterados', async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();
    render(<QuickUpdateModal employee={employee} loading={false} onCancel={vi.fn()} onSubmit={onSubmit} />);

    await user.selectOptions(screen.getByLabelText(/Novo status/i), 'CONTRATADO');
    await user.click(screen.getByRole('button', { name: /atualizar dados/i }));

    expect(onSubmit).toHaveBeenCalledWith({ status: 'CONTRATADO' });
  });
});

