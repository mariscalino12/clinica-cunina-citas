import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { TutorDashboardComponent } from './features/tutor/dashboard/dashboard.component';
import { MedicoDashboardComponent } from './features/medico/dashboard/dashboard.component';
import { AdminDashboardComponent } from './features/admin/dashboard/dashboard.component';
import { PacienteListComponent } from './features/tutor/paciente-list/paciente-list.component';
import { PacienteFormComponent } from './features/tutor/paciente-form/paciente-form.component';
import { TriajeFormComponent } from './features/tutor/triaje-form/triaje-form.component';
import { CitaListComponent } from './features/tutor/cita-list/cita-list.component';
import { CitaFormComponent } from './features/tutor/cita-form/cita-form.component';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegisterComponent },
  {
    path: 'tutor',
    component: TutorDashboardComponent,
    canActivate: [authGuard, roleGuard],
    data: { rol: 'TUTOR' },
    children: [
      { path: '', redirectTo: 'pacientes', pathMatch: 'full' },
      { path: 'pacientes', component: PacienteListComponent },
      { path: 'pacientes/nuevo', component: PacienteFormComponent },
      { path: 'triaje', component: TriajeFormComponent },
      { path: 'citas', component: CitaListComponent },
      { path: 'citas/nueva', component: CitaFormComponent },
    ]
  },
  { path: 'medico', component: MedicoDashboardComponent, canActivate: [authGuard, roleGuard], data: { rol: 'MEDICO' } },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard, roleGuard], data: { rol: 'ADMIN' } },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];