import { ArrowLeft } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { funcionariosApi } from '../api/funcionarios';
import EmployeeForm from '../components/EmployeeForm';

function EmployeeFormPage({ mode }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const [employee, setEmployee] = useState(null);
  const [loadingEmployee, setLoadingEmployee] = useState(mode === 'edit');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});

  useEffect(() => {
    if (mode !== 'edit') return;
    funcionariosApi.buscarPorId(id).then(setEmployee).catch((requestError) => setError(requestError.message)).finally(() => setLoadingEmployee(false));
  }, [id, mode]);

  async function handleSubmit(data) {
    setSubmitting(true); setError(''); setFieldErrors({});
    try {
      const saved = mode === 'create' ? await funcionariosApi.cadastrar(data) : await funcionariosApi.atualizar(id, data);
      navigate(`/funcionarios/${saved.id}`, { replace: true });
    } catch (requestError) {
      setError(requestError.message);
      setFieldErrors(Object.fromEntries((requestError.details?.campos || []).map((item) => [item.campo.split('.').at(-1), item.mensagem])));
    } finally { setSubmitting(false); }
  }

  const title = mode === 'create' ? 'Novo candidato' : 'Editar candidato';
  const subtitle = mode === 'create' ? 'Adicione uma nova pessoa ao processo seletivo.' : 'Atualize todas as informações da pessoa candidata.';
  if (loadingEmployee) return <main className="page-container page-container--form"><div className="form-skeleton" /></main>;

  return (
    <main className="page-container page-container--form">
      <Link className="back-link" to={mode === 'edit' ? `/funcionarios/${id}` : '/'}><ArrowLeft size={18} /> Voltar</Link>
      <header className="form-page-header"><p className="eyebrow">{mode === 'create' ? 'Cadastro' : `Candidato #${id}`}</p><h1>{title}</h1><p>{subtitle}</p></header>
      {error && <div className="error-banner"><div><strong>Revise os dados informados</strong><span>{error}</span></div></div>}
      {mode === 'edit' && !employee ? <div className="empty-state"><h3>Candidato não encontrado</h3><p>{error}</p></div> : <EmployeeForm key={employee?.id || 'new'} employee={employee} mode={mode} loading={submitting} serverErrors={fieldErrors} onSubmit={handleSubmit} />}
    </main>
  );
}

export default EmployeeFormPage;
