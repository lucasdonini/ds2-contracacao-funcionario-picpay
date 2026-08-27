import { BriefcaseBusiness } from 'lucide-react';
import { Link, Outlet, useLocation } from 'react-router-dom';

function AppLayout() {
  const location = useLocation();

  return (
    <div className="app-shell">
      <header className="topbar">
        <Link className="brand" to="/" aria-label="PicPay Talentos — início">
          <span className="brand-mark" aria-hidden="true">P</span>
          <span>PicPay <b>Talentos</b></span>
        </Link>
        <nav className="topnav" aria-label="Navegação principal">
          <Link className={location.pathname === '/' ? 'active' : ''} to="/">Candidatos</Link>
        </nav>
        <div className="profile-chip" aria-label="Área da equipe de Recursos Humanos">
          <BriefcaseBusiness size={17} aria-hidden="true" /><span>Equipe de RH</span><b>RH</b>
        </div>
      </header>
      <Outlet />
    </div>
  );
}

export default AppLayout;

