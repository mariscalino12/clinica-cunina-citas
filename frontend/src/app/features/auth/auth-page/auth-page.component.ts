import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-auth-page',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './auth-page.component.html',
  styleUrls: ['./auth-page.component.scss']
})
export class AuthPageComponent implements OnInit {
  view: 'landing' | 'login' | 'register' = 'landing';

  loginForm!: FormGroup;
  registerForm!: FormGroup;

  mensaje = '';
  erroresBackend: { [campo: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Si la ruta es /login o /registro, mostrar directamente esa vista
    const path = this.route.snapshot.url[0]?.path;
    if (path === 'login') {
      this.view = 'login';
    } else if (path === 'registro') {
      this.view = 'register';
    } else {
      this.view = 'landing';
    }

    this.initForms();
  }

  initForms() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

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

  showLanding() {
    this.view = 'landing';
    this.mensaje = '';
    this.erroresBackend = {};
  }

  showLogin() {
    this.view = 'login';
    this.mensaje = '';
    this.erroresBackend = {};
  }

  showRegister() {
    this.view = 'register';
    this.mensaje = '';
    this.erroresBackend = {};
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    const { email, password } = this.loginForm.value;
    this.authService.login(email, password).subscribe({
      next: () => {
        const rol = this.authService.usuarioActual()?.rol;
        if (rol === 'ADMIN') this.router.navigate(['/admin']);
        else if (rol === 'MEDICO') this.router.navigate(['/medico']);
        else if (rol === 'TUTOR') this.router.navigate(['/tutor']);
      },
      error: (err) => {
        this.mensaje = err.error?.message || 'Error al iniciar sesión';
      }
    });
  }

  onRegister() {
    this.erroresBackend = {};
    this.mensaje = '';
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    const datos = { ...this.registerForm.value, rol: 'TUTOR' };
    this.authService.registroTutor(datos).subscribe({
      next: () => {
        this.mensaje = 'Registro exitoso. Ahora inicia sesión.';
        this.registerForm.reset();
        this.showLogin();
      },
      error: (err) => {
        if (err.error && typeof err.error === 'object') {
          this.erroresBackend = err.error;
        } else {
          this.mensaje = err.error || 'Error al registrar';
        }
      }
    });
  }

  // Helpers para templates
  get loginEmail() { return this.loginForm.get('email'); }
  get loginPassword() { return this.loginForm.get('password'); }
  get regNombre() { return this.registerForm.get('nombre'); }
  get regApellido() { return this.registerForm.get('apellido'); }
  get regEmail() { return this.registerForm.get('email'); }
  get regDni() { return this.registerForm.get('dni'); }
  get regPassword() { return this.registerForm.get('password'); }
  get regTelefono() { return this.registerForm.get('telefono'); }
  get regDireccion() { return this.registerForm.get('direccion'); }
}