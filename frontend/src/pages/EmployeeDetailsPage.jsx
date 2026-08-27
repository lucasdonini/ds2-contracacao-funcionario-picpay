import { ArrowLeft, Mail, MapPin, PencilLine, Phone, RefreshCcw, Trash2, WalletCards } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { funcionariosApi } from '../api/funcionarios';
import ConfirmDialog from '../components/ConfirmDialog';
import QuickUpdateModal from '../components/QuickUpdateModal';
import StatusBadge from '../components/StatusBadge';
import { formatCurrency, initials } from '../utils/funcionarios';

function EmployeeDetailsPage() {
  const { id } = useParams(); const navigate = useNavigate();
  const [employee, setEmployee] = useState(null); const [loading, setLoading] = useState(true); const [error, setError] = useState('');
  const [showPatch, setShowPatch] = useState(false); const [showDelete, setShowDelete] = useState(false); const [actionLoading, setActionLoading] = useState(false); const [actionError, setActionError] = useState('');

  async function loadEmployee() { setLoading(true); setError(''); try { setEmployee(await funcionariosApi.buscarPorId(id)); } catch (requestError) { setError(requestError.message); } finally { setLoading(false); } }
  useEffect(() => { loadEmployee(); }, [id]);
  async function handlePatch(payload) { setActionLoading(true); setActionError(''); try { setEmployee(await funcionariosApi.atualizarParcialmente(id, payload)); setShowPatch(false); } catch (requestError) { setActionError(requestError.message); } finally { setActionLoading(false); } }
  async function handleDelete() { setActionLoading(true); try { await funcionariosApi.excluir(id); navigate('/', { replace: true }); } catch (requestError) { setShowDelete(false); setError(requestError.message); } finally { setActionLoading(false); } }

  if (loading) return <main className="page-container"><div className="detail-skeleton" /></main>;
  if (error || !employee) return <main className="page-container"><Link className="back-link" to="/"><ArrowLeft size={18} /> Voltar</Link><div className="empty-state empty-state--page"><h1>Não encontramos esse candidato</h1><p>{error}</p><button className="secondary-button" type="button" onClick={loadEmployee}>Tentar novamente</button></div></main>;

  const details = [{ label: 'E-mail', value: employee.email, icon: Mail }, { label: 'Telefone', value: employee.telefone || 'Não informado', icon: Phone }, { label: 'Cidade', value: employee.cidade || 'Não informada', icon: MapPin }, { label: 'Salário', value: formatCurrency(employee.salario), icon: WalletCards }];
  return (
    <main className="page-container page-container--narrow">
      <Link className="back-link" to="/"><ArrowLeft size={18} /> Voltar para candidatos</Link>
      <section className="profile-hero"><div className="profile-avatar">{initials(employee.nome)}</div><div className="profile-title"><p className="eyebrow">Candidato #{employee.id}</p><h1>{employee.nome}</h1><div><span>{employee.cargo}</span><StatusBadge status={employee.status} /></div></div><div className="profile-actions"><button className="secondary-button" type="button" onClick={() => setShowPatch(true)}><RefreshCcw size={18} /> Atualização rápida</button><Link className="primary-button" to={`/funcionarios/${id}/editar`}><PencilLine size={18} /> Editar</Link></div></section>
      <section className="details-grid">{details.map(({ label, value, icon: Icon }) => <article className="detail-card" key={label}><span><Icon size={20} /></span><small>{label}</small><strong>{value}</strong></article>)}</section>
      <section className="professional-card"><div><p className="eyebrow">Informações profissionais</p><h2>Posição no processo</h2></div><dl><div><dt>Cargo</dt><dd>{employee.cargo}</dd></div><div><dt>Departamento</dt><dd>{employee.departamento || 'Não informado'}</dd></div><div><dt>Status atual</dt><dd><StatusBadge status={employee.status} /></dd></div></dl></section>
      <button className="delete-link" type="button" onClick={() => setShowDelete(true)}><Trash2 size={18} /> Excluir candidato</button>
      <QuickUpdateModal key={showPatch ? employee.id : 'closed'} employee={showPatch ? employee : null} loading={actionLoading} error={actionError} onCancel={() => setShowPatch(false)} onSubmit={handlePatch} />
      <ConfirmDialog employee={showDelete ? employee : null} loading={actionLoading} onCancel={() => setShowDelete(false)} onConfirm={handleDelete} />
    </main>
  );
}

export default EmployeeDetailsPage;
