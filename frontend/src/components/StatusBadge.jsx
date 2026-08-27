import { STATUS_LABELS } from '../utils/funcionarios';

function StatusBadge({ status }) {
  return <span className={`status status--${status?.toLowerCase()}`}>{STATUS_LABELS[status] || status}</span>;
}

export default StatusBadge;

