import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Paciente {
  idPaciente?: number;
  nombre: string;
  apellido: string;
  fechaNacimiento: string; // formato yyyy-MM-dd
  genero: string; // 'M' o 'F'
  grupoSanguineo?: string;
  alergias?: string;
  dni?: string; // ← AÑADE ESTA LÍNEA
}

@Injectable({ providedIn: 'root' })
export class PacienteService {
  private apiUrl = '/api/pacientes';

  constructor(private http: HttpClient) {}

  registrarPaciente(paciente: Paciente, tutorId: number): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.apiUrl}/tutor/${tutorId}`, paciente);
  }

  listarPorTutor(tutorId: number): Observable<Paciente[]> {
  return this.http.get<Paciente[]>(`${this.apiUrl}/tutor/${tutorId}`);
}

  obtenerPorId(id: number): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.apiUrl}/${id}`);
  }
}