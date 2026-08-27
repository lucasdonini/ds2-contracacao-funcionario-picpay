import { RefreshCcw, X } from 'lucide-react';
import { useState } from 'react';
import { STATUS_OPTIONS } from '../utils/funcionarios';

function QuickUpdateModal({ employee, loading, error, onCancel, onSubmit }) {
  const [values, setValues] = useState({ cargo: '', salario: '', status: '' });
  const [validationError, setValidationError] = useState('');
  if (!employee) return null;

  function handleSubmit(event) {
    event.preventDefault();
    const payload = {};
    if (values.cargo.trim()) payload.cargo = values.cargo.trim();
    if (values.status) payload.status = values.status;
    if (values.salario !== '') payload.salario = Number(String(values.salario).replace(',', '.'));
    if (!Object.keys(payload).length) return setValidationError('Altere ao menos um dos campos disponíveis.');
    if (payload.salario < 0) return setValidationError('O salário não pode ser negativo.');
    onSubmit(payload);
  }

  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onCancel}>
      <section className="modal-card" role="dialog" aria-modal="true" aria-labelledby="patch-title" onMouseDown={(event) => event.stopPropagation()}>
        <button className="modal-close" type="button" onClick={onCancel} aria-label="Fechar"><X size={20} /></button>
        <span className="modal-icon"><RefreshCcw size={24} /></span>
        <p className="eyebrow">Atualização rápida</p>
        <h2 id="patch-title">Atualize apenas o necessário</h2>
        <p>Os demais dados de <strong>{employee.nome}</strong> permanecerão inalterados.</p>
        <form onSubmit={handleSubmit}>
          <label className="field"><span>Novo cargo</span><input value={values.cargo} maxLength={100} onChange={(event) => setValues({ ...values, cargo: event.target.value })} placeholder={employee.cargo} /></label>
          <label className="field"><span>Novo status</span><select value={values.status} onChange={(event) => setValues({ ...values, status: event.target.value })}><option value="">Manter status atual</option>{STATUS_OPTIONS.map((option) => <option value={option.value} key={option.value}>{option.label}</option>)}</select></label>
          <label className="field"><span>Novo salário</span><input type="number" min="0" step="0.01" value={values.salario} onChange={(event) => setValues({ ...values, salario: event.target.value })} placeholder={employee.salario ?? 'Não informado'} /></label>
          {(validationError || error) && <div className="inline-error">{validationError || error}</div>}
          <div className="modal-actions"><button className="secondary-button" type="button" onClick={onCancel} disabled={loading}>Cancelar</button><button className="primary-button" type="submit" disabled={loading}>{loading ? 'Atualizando…' : 'Atualizar dados'}</button></div>
        </form>
      </section>
    </div>
  );
}

export default QuickUpdateModal;
