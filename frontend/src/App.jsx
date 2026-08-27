import { Navigate, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import DashboardPage from './pages/DashboardPage';
import EmployeeDetailsPage from './pages/EmployeeDetailsPage';
import EmployeeFormPage from './pages/EmployeeFormPage';

function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="funcionarios/novo" element={<EmployeeFormPage mode="create" />} />
        <Route path="funcionarios/:id" element={<EmployeeDetailsPage />} />
        <Route path="funcionarios/:id/editar" element={<EmployeeFormPage mode="edit" />} />
        <Route path="*" element={<Navigate replace to="/" />} />
      </Route>
    </Routes>
  );
}

export default App;

