import { describe, expect, it } from 'vitest';
import { formatCurrency, initials, STATUS_LABELS, STATUS_OPTIONS } from './funcionarios';

describe('utilitários de funcionários', () => {
  it('gera iniciais usando no máximo dois nomes', () => {
    expect(initials('Ana Beatriz Silva')).toBe('AB');
    expect(initials('  Bruno  ')).toBe('B');
    expect(initials()).toBe('');
  });

  it('formata salário em reais e trata valor ausente', () => {
    expect(formatCurrency(8500)).toMatch(/R\$\s*8\.500,00/);
    expect(formatCurrency(null)).toBe('Não informado');
  });

  it('mantém todos os status aceitos pela API', () => {
    expect(STATUS_LABELS).toEqual({
      EM_ANALISE: 'Em análise',
      APROVADO: 'Aprovado',
      REPROVADO: 'Reprovado',
      CONTRATADO: 'Contratado',
    });
    expect(STATUS_OPTIONS).toHaveLength(4);
  });
});

