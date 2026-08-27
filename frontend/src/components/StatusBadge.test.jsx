import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import StatusBadge from './StatusBadge';

describe('StatusBadge', () => {
  it('exibe o rótulo amigável e a classe correspondente', () => {
    render(<StatusBadge status="EM_ANALISE" />);
    expect(screen.getByText('Em análise')).toHaveClass('status--em_analise');
  });
});

