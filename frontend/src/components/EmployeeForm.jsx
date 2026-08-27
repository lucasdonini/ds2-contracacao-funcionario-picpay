import { Save } from 'lucide-react';
import { useMemo, useState } from 'react';
import { STATUS_OPTIONS } from '../utils/funcionarios';

const EMPTY_FORM = { id: '', nome: '', email: '', telefone: '', cargo: '', departamento: '', salario: '', cidade: '', status: 'EM_ANALISE' };

function getInitialValues(employee) {
  if (!employee) return EMPTY_FORM;
  return { ...EMPTY_FORM, ...employee, id: String(employee.id ?? ''), salario: employee.salario == null ? '' : String(employee.salario), status: employee.status || 'EM_ANALISE' };
}

function validate(values, mode) {
  const errors = {};
  if (!values.nome.trim()) errors.nome = 'Informe o nome.';
  if (!values.email.trim()) errors.email = 'Informe o e-mail.';
  else if (!/^\S+@\S+\.\S+$/.test(values.email)) errors.email = 'Informe um e-mail válido.';
  if (!values.cargo.trim()) errors.cargo = 'Informe o cargo.';
  if (mode === 'edit' && !values.status) errors.status = 'Selecione um status.';
  if (values.id && Number(values.id) <= 0) errors.id = 'O ID deve ser positivo.';
  if (values.salario !== '' && Number(String(values.salario).replace(',', '.')) < 0) errors.salario = 'O salário não pode ser negativo.';
  return errors;
}

function EmployeeForm({ employee, mode, loading, serverErrors = {}, onSubmit }) {
  const initialValues = useMemo(() => getInitialValues(employee), [employee]);
  const [values, setValues] = useState(initialValues);
  const [clientErrors, setClientErrors] = useState({});
  const [editedFields, setEditedFields] = useState({});
  const visibleServerErrors = Object.fromEntries(
    Object.entries(serverErrors).filter(([field]) => !editedFields[field]),
  );
  const errors = { ...visibleServerErrors, ...clientErrors };

  function updateField(event) {
    const { name, value } = event.target;
    setValues((current) => ({ ...current, [name]: value }));
    setEditedFields((current) => ({ ...current, [name]: true }));
    setClientErrors((current) => ({ ...current, [name]: undefined }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    const nextErrors = validate(values, mode);
    setClientErrors(nextErrors);
    if (Object.keys(nextErrors).length) return;

    const data = {
      nome: values.nome.trim(), email: values.email.trim(), telefone: values.telefone.trim() || null,
      cargo: values.cargo.trim(), departamento: values.departamento.trim() || null,
      salario: values.salario === '' ? null : Number(String(values.salario).replace(',', '.')),
      cidade: values.cidade.trim() || null, status: values.status,
    };
    if (mode === 'create' && values.id !== '') data.id = Number(values.id);
    onSubmit(data);
  }

  return (
    <form className="employee-form" onSubmit={handleSubmit} noValidate>
      <div className="form-section-heading"><span>01</span><div><h2>Informações pessoais</h2><p>Dados para identificar e contatar a pessoa candidata.</p></div></div>
      <div className="form-grid">
        {mode === 'create' && <Field label="ID (opcional)" name="id" type="number" value={values.id} error={errors.id} onChange={updateField} placeholder="Gerado automaticamente" />}
        <Field label="Nome completo" name="nome" value={values.nome} error={errors.nome} onChange={updateField} maxLength={120} required placeholder="Ex.: Ana Silva" wide={mode === 'edit'} />
        <Field label="E-mail" name="email" type="email" value={values.email} error={errors.email} onChange={updateField} maxLength={160} required placeholder="ana@email.com" />
        <Field label="Telefone" name="telefone" value={values.telefone} error={errors.telefone} onChange={updateField} maxLength={30} placeholder="(11) 99999-9999" />
        <Field label="Cidade" name="cidade" value={values.cidade} error={errors.cidade} onChange={updateField} maxLength={100} placeholder="São Paulo" />
      </div>
      <div className="form-divider" />
      <div className="form-section-heading"><span>02</span><div><h2>Informações profissionais</h2><p>Posição, área e etapa atual do processo.</p></div></div>
      <div className="form-grid">
        <Field label="Cargo" name="cargo" value={values.cargo} error={errors.cargo} onChange={updateField} maxLength={100} required placeholder="Ex.: Desenvolvedora" />
        <Field label="Departamento" name="departamento" value={values.departamento} error={errors.departamento} onChange={updateField} maxLength={100} placeholder="Ex.: Tecnologia" />
        <Field label="Salário" name="salario" type="number" min="0" step="0.01" value={values.salario} error={errors.salario} onChange={updateField} placeholder="0,00" />
        <label className={`field ${errors.status ? 'field--error' : ''}`}><span>Status {mode === 'edit' && <b>*</b>}</span><select name="status" value={values.status} onChange={updateField}>{STATUS_OPTIONS.map((option) => <option value={option.value} key={option.value}>{option.label}</option>)}</select>{errors.status && <small>{errors.status}</small>}</label>
      </div>
      <div className="form-footer"><p><b>*</b> Campos obrigatórios</p><button className="primary-button" type="submit" disabled={loading}><Save size={19} /> {loading ? 'Salvando…' : mode === 'create' ? 'Cadastrar candidato' : 'Salvar alterações'}</button></div>
    </form>
  );
}

function Field({ label, error, required, wide, ...props }) {
  return <label className={`field ${wide ? 'field--wide' : ''} ${error ? 'field--error' : ''}`}><span>{label} {required && <b>*</b>}</span><input {...props} />{error && <small>{error}</small>}</label>;
}

export default EmployeeForm;
