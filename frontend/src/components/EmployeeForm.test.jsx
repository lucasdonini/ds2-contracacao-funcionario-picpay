import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import EmployeeForm from './EmployeeForm';

describe('EmployeeForm', () => {
  it('valida os campos obrigatórios antes de enviar', async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();
    render(<EmployeeForm mode="create" loading={false} onSubmit={onSubmit} />);

    await user.click(screen.getByRole('button', { name: /cadastrar candidato/i }));

    expect(screen.getByText('Informe o nome.')).toBeInTheDocument();
    expect(screen.getByText('Informe o e-mail.')).toBeInTheDocument();
    expect(screen.getByText('Informe o cargo.')).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('normaliza e envia o cadastro no formato esperado pela API', async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();
    render(<EmployeeForm mode="create" loading={false} onSubmit={onSubmit} />);

    await user.type(screen.getByLabelText(/ID \(opcional\)/i), '8');
    await user.type(screen.getByLabelText(/Nome completo/i), '  Ana Silva  ');
    await user.type(screen.getByLabelText(/E-mail/i), 'ana@email.com');
    await user.type(screen.getByLabelText(/^Cargo/i), 'Desenvolvedora');
    await user.type(screen.getByLabelText(/Salário/i), '8500.50');
    await user.click(screen.getByRole('button', { name: /cadastrar candidato/i }));

    expect(onSubmit).toHaveBeenCalledWith({
      id: 8,
      nome: 'Ana Silva',
      email: 'ana@email.com',
      telefone: null,
      cargo: 'Desenvolvedora',
      departamento: null,
      salario: 8500.5,
      cidade: null,
      status: 'EM_ANALISE',
    });
  });

  it('remove a mensagem do servidor quando o campo é corrigido', async () => {
    const user = userEvent.setup();
    render(
      <EmployeeForm
        mode="create"
        loading={false}
        serverErrors={{ email: 'E-mail já utilizado.' }}
        onSubmit={vi.fn()}
      />,
    );

    expect(screen.getByText('E-mail já utilizado.')).toBeInTheDocument();
    await user.type(screen.getByLabelText(/E-mail/i), 'novo@email.com');
    expect(screen.queryByText('E-mail já utilizado.')).not.toBeInTheDocument();
  });
});

