import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PacienteService, Paciente } from '../../../core/services/paciente.service';
import { TriajeService, TriajeResult } from '../../../core/services/triaje.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-triaje-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './triaje-form.component.html',
  styleUrls: ['./triaje-form.component.scss']
})
export class TriajeFormComponent implements OnInit {
  pacientes: Paciente[] = [];
  pacienteSeleccionado: number | null = null;
  sintomasSeleccionados: number[] = [];
  sintomasDisponibles = [
    { id: 1, nombre: 'Fiebre alta persistente' },
    { id: 2, nombre: 'Tos seca o con flema' },
    { id: 3, nombre: 'Dolor de oído' },
    { id: 4, nombre: 'Erupción cutánea' },
    { id: 5, nombre: 'Dificultad para respirar' },
    { id: 6, nombre: 'Dolor abdominal' },
    { id: 7, nombre: 'Convulsiones' },
    { id: 8, nombre: 'Vómitos persistentes' }
  ];
  resultado: TriajeResult | null = null;
  error = '';

  constructor(
    private pacienteService: PacienteService,
    private triajeService: TriajeService,
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
  }

  toggleSintoma(id: number) {
    const index = this.sintomasSeleccionados.indexOf(id);
    if (index > -1) {
      this.sintomasSeleccionados.splice(index, 1);
    } else {
      this.sintomasSeleccionados.push(id);
    }
  }

  realizarTriaje() {
    if (!this.pacienteSeleccionado || this.sintomasSeleccionados.length === 0) {
      this.error = 'Debe seleccionar paciente y al menos un síntoma';
      return;
    }
    this.error = '';
    this.triajeService.realizarTriaje(this.pacienteSeleccionado, this.sintomasSeleccionados)
      .subscribe({
        next: (res) => this.resultado = res,
        error: (err) => this.error = err.error?.message || 'Error al procesar triaje'
      });
  }
}