import { ArrowUpRight, BriefcaseBusiness, CheckCircle2, Clock3, PencilLine, RefreshCcw, Search, Trash2, UserPlus, UsersRound, XCircle } from 'lucide-react';
import { useCallback, useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { funcionariosApi } from '../api/funcionarios';
import ConfirmDialog from '../components/ConfirmDialog';
import QuickUpdateModal from '../components/QuickUpdateModal';
import StatusBadge from '../components/StatusBadge';
import Toast from '../components/Toast';
import { STATUS_OPTIONS, initials } from '../utils/funcionarios';

const EMPTY_INDICATORS = { totalCandidatos: 0, emAnalise: 0, aprovados: 0, reprovados: 0, contratados: 0 };
const EMPTY_FILTERS = { nome: '', cargo: '', status: '' };

function DashboardPage() {
  const navigate = useNavigate();
  const [employees, setEmployees] = useState([]);
  const [indicators, setIndicators] = useState(EMPTY_INDICATORS);
  const [filters, setFilters] = useState(EMPTY_FILTERS);
  const [loading, setLoading] = useState(true);
  const [pageError, setPageError] = useState('');
  const [selectedForPatch, setSelectedForPatch] = useState(null);
  const [selectedForDelete, setSelectedForDelete] = useState(null);
  const [actionLoading, setActionLoading] = useState(false);
  const [actionError, setActionError] = useState('');
  const [toast, setToast] = useState(null);

  const loadDashboard = useCallback(async () => {
    setLoading(true); setPageError('');
    try {
      const [employeeData, indicatorData] = await Promise.all([funcionariosApi.listar(), funcionariosApi.indicadores()]);
      setEmployees(employeeData); setIndicators(indicatorData);
    } catch (error) { setPageError(error.message || 'Não foi possível conectar ao servidor.'); }
    finally { setLoading(false); }
  }, []);

  useEffect(() => { loadDashboard(); }, [loadDashboard]);

  async function handleSearch(event) {
    event.preventDefault();
    const hasFilters = Object.values(filters).some((value) => value.trim());
    if (!hasFilters) return loadDashboard();
    setLoading(true); setPageError('');
    try { setEmployees(await funcionariosApi.buscar(filters)); }
    catch (error) { setPageError(error.message); }
    finally { setLoading(false); }
  }

  function clearFilters() { setFilters(EMPTY_FILTERS); loadDashboard(); }

  async function handlePatch(payload) {
    setActionLoading(true); setActionError('');
    try {
      await funcionariosApi.atualizarParcialmente(selectedForPatch.id, payload);
      setSelectedForPatch(null); setToast({ type: 'success', message: 'Dados atualizados com sucesso.' });
      await loadDashboard();
    } catch (error) { setActionError(error.message); }
    finally { setActionLoading(false); }
  }

  async function handleDelete() {
    setActionLoading(true);
    try {
      await funcionariosApi.excluir(selectedForDelete.id);
      setSelectedForDelete(null); setToast({ type: 'success', message: 'Candidato excluído com sucesso.' });
      await loadDashboard();
    } catch (error) { setSelectedForDelete(null); setToast({ type: 'error', message: error.message }); }
    finally { setActionLoading(false); }
  }

  const cards = [
    { key: 'totalCandidatos', label: 'Total de candidatos', help: 'Visão geral do processo', icon: UsersRound, theme: 'dark' },
    { key: 'emAnalise', label: 'Em análise', help: 'Aguardando avaliação', icon: Clock3 },
    { key: 'aprovados', label: 'Aprovados', help: 'Prontos para avançar', icon: CheckCircle2 },
    { key: 'reprovados', label: 'Reprovados', help: 'Processos encerrados', icon: XCircle },
    { key: 'contratados', label: 'Contratados', help: 'Novos talentos', icon: BriefcaseBusiness },
  ];

  return (
    <main className="page-container">
      <section className="hero"><div><p className="eyebrow">Gestão de candidatos</p><h1>Talentos certos.<br />Um processo mais simples.</h1><p className="hero-copy">Acompanhe cada pessoa candidata, atualize etapas e mantenha o processo seletivo organizado em um só lugar.</p></div><Link className="primary-button" to="/funcionarios/novo"><UserPlus size={20} /> Novo candidato</Link></section>
      <section className="stats-grid" aria-label="Indicadores de candidatos">
        {cards.map(({ key, label, help, icon: Icon, theme }) => <article className={`stat-card ${theme === 'dark' ? 'stat-card--dark' : ''}`} key={key}><Icon aria-hidden="true" /><span>{label}</span><strong>{loading ? '—' : indicators[key]}</strong><small>{help}</small></article>)}
      </section>
      <section className="content-card">
        <div className="section-heading"><div><p className="eyebrow">Candidatos</p><h2>Acompanhe o processo</h2></div><span className="count-badge">{employees.length} {employees.length === 1 ? 'pessoa' : 'pessoas'}</span></div>
        <form className="filters" onSubmit={handleSearch}>
          <label className="search-field"><Search size={19} /><input aria-label="Buscar por nome" value={filters.nome} onChange={(event) => setFilters({ ...filters, nome: event.target.value })} placeholder="Buscar por nome" /></label>
          <input className="filter-control" aria-label="Buscar por cargo" value={filters.cargo} onChange={(event) => setFilters({ ...filters, cargo: event.target.value })} placeholder="Cargo" />
          <select className="filter-control" aria-label="Filtrar por status" value={filters.status} onChange={(event) => setFilters({ ...filters, status: event.target.value })}><option value="">Todos os status</option>{STATUS_OPTIONS.map((option) => <option value={option.value} key={option.value}>{option.label}</option>)}</select>
          <button className="secondary-button" type="submit">Buscar</button>
          {Object.values(filters).some(Boolean) && <button className="text-button" type="button" onClick={clearFilters}>Limpar</button>}
        </form>
        {pageError && <div className="error-banner"><div><strong>Não foi possível carregar os dados</strong><span>{pageError}</span></div><button type="button" onClick={loadDashboard}>Tentar novamente</button></div>}
        {loading ? <div className="loading-list" aria-label="Carregando candidatos">{[1, 2, 3].map((item) => <div className="loading-row" key={item} />)}</div> : employees.length === 0 ? <div className="empty-state"><span><UsersRound size={30} /></span><h3>Nenhum candidato por aqui</h3><p>Cadastre uma nova pessoa ou ajuste os filtros da pesquisa.</p><Link className="primary-button" to="/funcionarios/novo">Cadastrar candidato</Link></div> : (
          <div className="employee-list"><div className="employee-table-header"><span>Candidato</span><span>Departamento</span><span>Status</span><span>Ações</span></div>{employees.map((employee) => <article className="employee-row" key={employee.id}><div className="avatar">{initials(employee.nome)}</div><div className="employee-main"><strong>{employee.nome}</strong><span>{employee.cargo}</span></div><span className="department">{employee.departamento || 'Não informado'}</span><StatusBadge status={employee.status} /><div className="row-actions"><button type="button" onClick={() => { setActionError(''); setSelectedForPatch(employee); }} aria-label={`Atualizar rapidamente ${employee.nome}`} title="Atualização rápida"><RefreshCcw size={17} /></button><button type="button" onClick={() => navigate(`/funcionarios/${employee.id}/editar`)} aria-label={`Editar ${employee.nome}`} title="Editar"><PencilLine size={17} /></button><button className="row-action--danger" type="button" onClick={() => setSelectedForDelete(employee)} aria-label={`Excluir ${employee.nome}`} title="Excluir"><Trash2 size={17} /></button><Link to={`/funcionarios/${employee.id}`} aria-label={`Ver detalhes de ${employee.nome}`} title="Ver detalhes"><ArrowUpRight size={18} /></Link></div></article>)}</div>
        )}
      </section>
      <QuickUpdateModal key={selectedForPatch?.id || 'patch'} employee={selectedForPatch} loading={actionLoading} error={actionError} onCancel={() => setSelectedForPatch(null)} onSubmit={handlePatch} />
      <ConfirmDialog employee={selectedForDelete} loading={actionLoading} onCancel={() => setSelectedForDelete(null)} onConfirm={handleDelete} />
      <Toast toast={toast} onClose={() => setToast(null)} />
    </main>
  );
}

export default DashboardPage;

