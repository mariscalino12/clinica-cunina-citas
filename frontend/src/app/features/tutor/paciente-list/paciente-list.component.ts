import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacienteService, Paciente } from '../../../core/services/paciente.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-paciente-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './paciente-list.component.html',
  styleUrls: ['./paciente-list.component.scss']
})
export class PacienteListComponent implements OnInit {
  pacientes: Paciente[] = [];
  pacientesFiltrados: Paciente[] = [];
  busqueda = '';
  error = '';
  loading = true;

  constructor(
    private pacienteService: PacienteService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.cargarPacientes();
  }

  cargarPacientes() {
    this.loading = true;
    this.error = '';
    const tutorId = this.authService.usuarioActual()?.idUsuario;
    if (tutorId) {
      this.pacienteService.listarPorTutor(tutorId).subscribe({
        next: (data) => {
          this.pacientes = data;
          this.pacientesFiltrados = data;
          this.loading = false;
          this.cdr.detectChanges(); // Forzar actualización de la vista
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al cargar pacientes';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.loading = false;
      this.error = 'No se pudo obtener el tutor';
      this.cdr.detectChanges();
    }
  }

  filtrar() {
    const termino = this.busqueda.toLowerCase().trim();
    if (!termino) {
      this.pacientesFiltrados = [...this.pacientes];
    } else {
      this.pacientesFiltrados = this.pacientes.filter(p =>
        `${p.nombre} ${p.apellido}`.toLowerCase().includes(termino) ||
        (p.dni && p.dni.includes(termino))
      );
    }
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
    return edad >= 0 ? edad : null;
  }

  obtenerIniciales(paciente: Paciente): string {
    return (paciente.nombre.charAt(0) + paciente.apellido.charAt(0)).toUpperCase();
  }
}