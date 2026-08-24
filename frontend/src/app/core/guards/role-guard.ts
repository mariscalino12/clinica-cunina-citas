import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const rolRequerido = route.data['rol'];
  const usuario = authService.usuarioActual();

  if (usuario && usuario.rol === rolRequerido) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};