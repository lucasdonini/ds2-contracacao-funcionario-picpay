import { AlertTriangle, X } from 'lucide-react';

function ConfirmDialog({ employee, loading, onCancel, onConfirm }) {
  if (!employee) return null;
  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onCancel}>
      <section className="modal-card modal-card--small" role="dialog" aria-modal="true" aria-labelledby="delete-title" onMouseDown={(event) => event.stopPropagation()}>
        <button className="modal-close" type="button" onClick={onCancel} aria-label="Fechar"><X size={20} /></button>
        <span className="modal-icon modal-icon--danger"><AlertTriangle size={25} /></span>
        <p className="eyebrow">Confirmar exclusão</p>
        <h2 id="delete-title">Excluir {employee.nome}?</h2>
        <p>Essa ação remove a pessoa candidata da lista atual e não pode ser desfeita.</p>
        <div className="modal-actions">
          <button className="secondary-button" type="button" onClick={onCancel} disabled={loading}>Cancelar</button>
          <button className="danger-button" type="button" onClick={onConfirm} disabled={loading}>{loading ? 'Excluindo…' : 'Sim, excluir'}</button>
        </div>
      </section>
    </div>
  );
}

export default ConfirmDialog;

