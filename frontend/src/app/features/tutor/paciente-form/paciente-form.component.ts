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
  fechaNacimiento = '';
  genero = 'M';
  grupoSanguineo = '';
  alergias = '';
  mensaje = '';

  constructor(
    private pacienteService: PacienteService,
    private authService: AuthService
  ) {}

  registrar() {
    const tutorId = this.authService.usuarioActual()?.idUsuario;
    if (!tutorId) {
      this.mensaje = 'No se pudo obtener el tutor';
      return;
    }

    const paciente = {
      nombre: this.nombre,
      apellido: this.apellido,
      fechaNacimiento: this.fechaNacimiento,
      genero: this.genero,
      grupoSanguineo: this.grupoSanguineo,
      alergias: this.alergias
    };

    this.pacienteService.registrarPaciente(paciente, tutorId).subscribe({
      next: () => {
        this.mensaje = 'Paciente registrado exitosamente';
        // limpiar formulario
        this.nombre = ''; this.apellido = ''; this.fechaNacimiento = ''; 
        this.grupoSanguineo = ''; this.alergias = '';
      },
      error: (err) => this.mensaje = err.error?.message || 'Error al registrar paciente'
    });
  }
}