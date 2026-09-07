import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PacienteService } from '../../../core/services/paciente.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-paciente-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './paciente-form.component.html',
  styleUrls: ['./paciente-form.component.scss']
})
export class PacienteFormComponent {
  nombre = '';
  apellido = '';
  dni = '';
  fechaNacimiento = '';
  genero = 'M';
  grupoSanguineo = '';
  alergias = '';
  mensaje = '';
  error = '';
  imagenPreview: string | null = null;
  imagenBase64: string | null = null;

  constructor(
    private pacienteService: PacienteService,
    private authService: AuthService
  ) {}

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (!file.type.startsWith('image/')) {
        this.error = 'Solo se permiten imágenes';
        return;
      }
      const reader = new FileReader();
      reader.onload = () => {
        this.imagenPreview = reader.result as string;
        this.imagenBase64 = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  quitarImagen() {
    this.imagenPreview = null;
    this.imagenBase64 = null;
  }

  calcularEdad(fechaNacimiento: string): number | null {
    if (!fechaNacimiento) return null;
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
      edad--;
    }
    return edad;
  }

  registrar() {
    this.error = '';
    this.mensaje = '';
    const tutorId = this.authService.usuarioActual()?.idUsuario;
    if (!tutorId) {
      this.error = 'No se pudo obtener el tutor';
      return;
    }

    // Validación del DNI
    if (!/^\d{8}$/.test(this.dni)) {
      this.error = 'El DNI debe tener exactamente 8 dígitos';
      return;
    }

    // Validación de edad
    const edad = this.calcularEdad(this.fechaNacimiento);
    if (edad === null) {
      this.error = 'Ingrese una fecha de nacimiento válida';
      return;
    }
    if (edad < 0 || edad > 15) {
      this.error = 'La edad debe estar entre 0 y 15 años';
      return;
    }

    const paciente = {
      nombre: this.nombre,
      apellido: this.apellido,
      dni: this.dni,
      fechaNacimiento: this.fechaNacimiento,
      genero: this.genero,
      grupoSanguineo: this.grupoSanguineo,
      alergias: this.alergias,
      fotoBase64: this.imagenBase64
    };

    this.pacienteService.registrarPaciente(paciente, tutorId).subscribe({
      next: () => {
        this.mensaje = 'Paciente registrado exitosamente';
        // Limpiar campos
        this.nombre = '';
        this.apellido = '';
        this.dni = '';
        this.fechaNacimiento = '';
        this.genero = 'M';
        this.grupoSanguineo = '';
        this.alergias = '';
        this.imagenPreview = null;
        this.imagenBase64 = null;
      },
      error: (err) => {
        console.error('Error al guardar:', err);
        if (err.error && typeof err.error === 'object') {
          const mensajes = Object.values(err.error).join(', ');
          this.error = mensajes;
        } else if (err.error && typeof err.error === 'string') {
          this.error = err.error;
        } else {
          this.error = 'Error al registrar paciente';
        }
      }
    });
  }
}