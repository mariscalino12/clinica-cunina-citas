import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PacienteService, Paciente } from '../../../core/services/paciente.service';
import { MedicoService, Medico } from '../../../core/services/medico.service';
import { EspecialidadService, Especialidad } from '../../../core/services/especialidad.service';
import { CitaService } from '../../../core/services/cita.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-cita-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cita-form.component.html',
  styleUrls: ['./cita-form.component.scss']
})
export class CitaFormComponent implements OnInit {
  pacientes: Paciente[] = [];
  especialidades: Especialidad[] = [];
  medicos: Medico[] = [];
  pacienteId: number | null = null;
  especialidadId: number | null = null;
  medicoId: number | null = null;
  fechaHora: string = '';
  mensaje = '';
  error = '';

  constructor(
    private pacienteService: PacienteService,
    private medicoService: MedicoService,
    private especialidadService: EspecialidadService,
    private citaService: CitaService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const tutorId = this.authService.usuarioActual()?.idUsuario;
    if (tutorId) {
      this.pacienteService.listarPorTutor(tutorId).subscribe({
        next: (data) => this.pacientes = data,
        error: (err) => this.error = err.error?.message || 'Error al cargar pacientes'
      });
    }
    this.especialidadService.listarTodas().subscribe({
      next: (data) => this.especialidades = data,
      error: (err) => this.error = err.error?.message || 'Error al cargar especialidades'
    });
  }

  cargarMedicos() {
    if (this.especialidadId) {
      this.medicoService.listarPorEspecialidad(this.especialidadId).subscribe({
        next: (data) => this.medicos = data,
        error: (err) => this.error = err.error?.message || 'Error al cargar médicos'
      });
    }
  }

  reservar() {
    if (!this.pacienteId || !this.medicoId || !this.especialidadId || !this.fechaHora) {
      this.error = 'Complete todos los campos';
      return;
    }
    this.citaService.reservarCita({
      pacienteId: this.pacienteId,
      medicoId: this.medicoId,
      especialidadId: this.especialidadId,
      triajeId: null,
      fechaHora: this.fechaHora
    }).subscribe({
      next: () => {
        this.mensaje = 'Cita reservada con éxito';
      },
      error: (err) => this.error = err.error?.message || 'Error al reservar cita'
    });
  }
}