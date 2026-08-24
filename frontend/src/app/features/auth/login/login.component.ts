import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        const rol = this.authService.usuarioActual()?.rol;
        if (rol === 'ADMIN') this.router.navigate(['/admin']);
        else if (rol === 'MEDICO') this.router.navigate(['/medico']);
        else if (rol === 'TUTOR') this.router.navigate(['/tutor']);
      },
      error: (err) => this.error = err.error?.message || 'Error al iniciar sesión'
    });
  }
}