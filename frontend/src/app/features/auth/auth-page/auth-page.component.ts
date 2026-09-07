import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import {
  MedicoService,
  Medico
} from '../../../core/services/medico.service';
import {
  EspecialidadService,
  Especialidad
} from '../../../core/services/especialidad.service';

@Component({
  selector: 'app-auth-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './auth-page.component.html',
  styleUrls: ['./auth-page.component.scss']
})
export class AuthPageComponent implements OnInit {
  view: 'landing' | 'login' | 'register' = 'landing';

  loginForm!: FormGroup;
  registerForm!: FormGroup;

  mensaje = '';
  erroresBackend: { [campo: string]: string } = {};

  doctorAnimation = 'doctor-idle';
  activeField = '';
  showPassword = false;

  bgImage = 'https://images.unsplash.com/photo-1629909613654-28e377c37b09?auto=format&fit=crop&q=80&w=1920';

  medicos: Medico[] = [];
  especialidades: Especialidad[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private medicoService: MedicoService,
    private especialidadService: EspecialidadService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const path = this.route.snapshot.url[0]?.path;

    if (path === 'login') {
      this.view = 'login';
    } else if (path === 'registro') {
      this.view = 'register';
    } else {
      this.view = 'landing';
    }

    this.initForms();
    this.cargarDatosLanding();
  }

  initForms() {
    this.loginForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      password: [
        '',
        Validators.required
      ]
    });

    this.registerForm = this.fb.group({
      nombre: [
        '',
        [
          Validators.required,
          Validators.minLength(2)
        ]
      ],

      apellido: [
        '',
        [
          Validators.required,
          Validators.minLength(2)
        ]
      ],

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      dni: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d{8}$/)
        ]
      ],

      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(
            /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
          )
        ]
      ],

      telefono: [''],
      direccion: ['']
    });
  }

  cargarDatosLanding() {
    this.medicoService.listarActivos().subscribe({
      next: (data) => {
        this.medicos = data;
      },
      error: (err) => {
        console.error('Error cargando médicos', err);
      }
    });

    this.especialidadService.listarTodas().subscribe({
      next: (data) => {
        this.especialidades = data;
      },
      error: (err) => {
        console.error('Error cargando especialidades', err);
      }
    });
  }

  showLanding() {
    this.view = 'landing';
    this.mensaje = '';
    this.erroresBackend = {};
    this.activeField = '';
    this.doctorAnimation = 'doctor-idle';
    this.showPassword = false;
  }

  showLogin() {
    this.view = 'login';
    this.bgImage = 'https://images.unsplash.com/photo-1581056771107-24ca5f033842?auto=format&fit=crop&q=80&w=1920';
    this.mensaje = '';
    this.erroresBackend = {};
    this.activeField = '';
    this.doctorAnimation = 'doctor-idle';
    this.showPassword = false;
  }

  showRegister() {
    this.view = 'register';
    this.bgImage = 'https://images.unsplash.com/photo-1602052577122-f73b9710adba?auto=format&fit=crop&q=80&w=1920';
    this.mensaje = '';
    this.erroresBackend = {};
    this.activeField = '';
    this.doctorAnimation = 'doctor-idle';
    this.showPassword = false;
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.doctorAnimation = 'doctor-password';
      return;
    }

    const { email, password } = this.loginForm.value;

    this.doctorAnimation = 'doctor-success';

    this.authService.login(email, password).subscribe({
      next: () => {
        const rol = this.authService.usuarioActual()?.rol;

        if (rol === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else if (rol === 'MEDICO') {
          this.router.navigate(['/medico']);
        } else if (rol === 'TUTOR') {
          this.router.navigate(['/tutor']);
        }
      },

      error: (err) => {
        this.mensaje =
          err.error?.message ||
          'Error al iniciar sesión';

        this.doctorAnimation = 'doctor-password';
      }
    });
  }

  onRegister() {
    this.erroresBackend = {};
    this.mensaje = '';

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.doctorAnimation = 'doctor-password';
      return;
    }

    const datos = {
      ...this.registerForm.value,
      rol: 'TUTOR'
    };

    this.doctorAnimation = 'doctor-success';

    this.authService.registroTutor(datos).subscribe({
      next: () => {
        this.mensaje =
          'Registro exitoso. Ahora inicia sesión.';

        this.registerForm.reset();

        this.showLogin();
      },

      error: (err) => {
        this.doctorAnimation = 'doctor-password';

        if (
          err.error &&
          typeof err.error === 'object'
        ) {
          this.erroresBackend = err.error;
        } else {
          this.mensaje =
            err.error ||
            'Error al registrar';
        }
      }
    });
  }

  setActiveField(field: string) {
    this.activeField = field;

    if (field === 'password') {
      this.doctorAnimation = 'doctor-password';
    } else {
      this.doctorAnimation = 'doctor-typing';
    }
  }

  onFieldTyping(field: string) {
    if (field === 'password') {
      this.doctorAnimation = 'doctor-password';
    } else {
      this.doctorAnimation = 'doctor-typing';
    }
  }

  clearActiveField() {
    this.activeField = '';

    setTimeout(() => {
      if (!this.activeField) {
        this.doctorAnimation = 'doctor-idle';
      }
    }, 150);
  }

  togglePassword() {
    this.showPassword = !this.showPassword;

    if (this.showPassword) {
      this.doctorAnimation = 'doctor-typing';
    } else {
      this.doctorAnimation = 'doctor-password';
    }
  }

  get loginEmail() {
    return this.loginForm.get('email');
  }

  get loginPassword() {
    return this.loginForm.get('password');
  }

  get regNombre() {
    return this.registerForm.get('nombre');
  }

  get regApellido() {
    return this.registerForm.get('apellido');
  }

  get regEmail() {
    return this.registerForm.get('email');
  }

  get regDni() {
    return this.registerForm.get('dni');
  }

  get regPassword() {
    return this.registerForm.get('password');
  }

  get regTelefono() {
    return this.registerForm.get('telefono');
  }

  get regDireccion() {
    return this.registerForm.get('direccion');
  }
}