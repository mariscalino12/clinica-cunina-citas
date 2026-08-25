import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacienteService, Paciente } from '../../../core/services/paciente.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-paciente-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './paciente-list.component.html',
  styleUrls: ['./paciente-list.component.scss']
})
export class PacienteListComponent implements OnInit {
  pacientes: Paciente[] = [];
  error = '';

  constructor(
    private pacienteService: PacienteService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const stored = localStorage.getItem('usuario');
    let tutorId: number | null = null;
    if (stored) {
      tutorId = JSON.parse(stored).idUsuario;
    }
    if (tutorId) {
      this.pacienteService.listarPorTutor(tutorId).subscribe({
        next: (data) => {
          this.pacientes = data;
          this.cdr.detectChanges(); // Forzar detección de cambios
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al cargar pacientes';
          this.cdr.detectChanges();
        }
      });
    }
  }
}