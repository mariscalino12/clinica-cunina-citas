import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  mensaje = '';
  erroresBackend: { [campo: string]: string } = {};

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  ngOnInit() {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
      ]],
      telefono: [''],
      direccion: ['']
    });
  }

  // Getters para acceder fácilmente a los controles
  get nombre() { return this.registerForm.get('nombre'); }
  get apellido() { return this.registerForm.get('apellido'); }
  get email() { return this.registerForm.get('email'); }
  get dni() { return this.registerForm.get('dni'); }
  get password() { return this.registerForm.get('password'); }
  get telefono() { return this.registerForm.get('telefono'); }
  get direccion() { return this.registerForm.get('direccion'); }

  onRegister() {
    this.mensaje = '';
    this.erroresBackend = {};
    if (this.registerForm.invalid) {
      // Marcar todos los campos como tocados para mostrar errores
      this.registerForm.markAllAsTouched();
      return;
    }

    const datos = this.registerForm.value;
    datos.rol = 'TUTOR';

    this.authService.registroTutor(datos).subscribe({
      next: () => {
        this.mensaje = 'Registro exitoso. Ahora inicia sesión.';
        this.registerForm.reset();
      },
      error: (err) => {
        if (err.error && typeof err.error === 'object') {
          // El backend devuelve un mapa de errores por campo
          this.erroresBackend = err.error;
        } else if (err.error) {
          this.mensaje = err.error;
        } else {
          this.mensaje = 'Error al registrar';
        }
      }
    });
  }
}