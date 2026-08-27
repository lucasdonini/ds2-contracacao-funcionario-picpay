import { CheckCircle2, X, XCircle } from 'lucide-react';

function Toast({ toast, onClose }) {
  if (!toast) return null;
  const Icon = toast.type === 'error' ? XCircle : CheckCircle2;
  return (
    <div className={`toast toast--${toast.type || 'success'}`} role="status">
      <Icon size={21} /><span>{toast.message}</span>
      <button type="button" onClick={onClose} aria-label="Fechar mensagem"><X size={17} /></button>
    </div>
  );
}

export default Toast;

