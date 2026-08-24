import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  nombre = '';
  apellido = '';
  email = '';
  password = '';
  telefono = '';
  direccion = '';
  mensaje = '';

  constructor(private authService: AuthService) {}

  onRegister() {
    const datos = {
      nombre: this.nombre,
      apellido: this.apellido,
      email: this.email,
      passwordHash: this.password,
      rol: 'TUTOR',
      telefono: this.telefono,
      direccion: this.direccion
    };
    this.authService.registroTutor(datos).subscribe({
      next: () => {
        this.mensaje = 'Registro exitoso. Ahora inicia sesión.';
      },
      error: (err) => this.mensaje = err.error?.message || 'Error al registrar'
    });
  }
}