import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CitaService, Cita } from '../../../core/services/cita.service';
import { PacienteService, Paciente } from '../../../core/services/paciente.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-cita-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cita-list.component.html',
  styleUrls: ['./cita-list.component.scss']
})
export class CitaListComponent implements OnInit {
  citas: Cita[] = [];
  error = '';

  constructor(
    private citaService: CitaService,
    private pacienteService: PacienteService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const tutorId = this.authService.usuarioActual()?.idUsuario;
    if (tutorId) {
      this.pacienteService.listarPorTutor(tutorId).subscribe({
        next: (pacientes) => {
          let allCitas: Cita[] = [];
          pacientes.forEach(p => {
            if (p.idPaciente) {
              this.citaService.listarPorPaciente(p.idPaciente).subscribe({
                next: (citas) => {
                  allCitas = allCitas.concat(citas);
                  this.citas = allCitas;
                },
                error: (err) => this.error = err.error?.message || 'Error al cargar citas'
              });
            }
          });
        },
        error: (err) => this.error = err.error?.message || 'Error al cargar pacientes'
      });
    }
  }
}