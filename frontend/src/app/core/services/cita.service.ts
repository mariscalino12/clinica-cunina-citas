import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Cita {
  idCita?: number;
  paciente: { idPaciente: number; nombre: string; apellido: string };
  medico: { idMedico: number; especialidad: { nombre: string } };
  especialidad: { idEspecialidad: number; nombre: string };
  fechaHora: string;
  estado: string;
  tipoConsulta: string;
  estadoPago: string;
}

@Injectable({ providedIn: 'root' })
export class CitaService {
  private apiUrl = '/api/citas';

  constructor(private http: HttpClient) {}

  reservarCita(data: any): Observable<Cita> {
    return this.http.post<Cita>(this.apiUrl, data);
  }

  listarPorPaciente(pacienteId: number): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/paciente/${pacienteId}`);
  }

  listarPorMedico(medicoId: number, inicio: string, fin: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/medico/${medicoId}`, {
      params: { inicio, fin }
    });
  }
}