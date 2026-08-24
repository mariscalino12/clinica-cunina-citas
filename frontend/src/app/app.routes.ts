import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { TutorDashboardComponent } from './features/tutor/dashboard/dashboard.component';
import { MedicoDashboardComponent } from './features/medico/dashboard/dashboard.component';
import { AdminDashboardComponent } from './features/admin/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegisterComponent },
  { path: 'tutor', component: TutorDashboardComponent, canActivate: [authGuard, roleGuard], data: { rol: 'TUTOR' } },
  { path: 'medico', component: MedicoDashboardComponent, canActivate: [authGuard, roleGuard], data: { rol: 'MEDICO' } },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard, roleGuard], data: { rol: 'ADMIN' } },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];