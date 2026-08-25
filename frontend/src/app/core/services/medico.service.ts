import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Medico {
  idMedico: number;
  usuario?: any;
  especialidad: { idEspecialidad: number; nombre: string };
  numeroColegiatura: string;
  activo: boolean;
}

@Injectable({ providedIn: 'root' })
export class MedicoService {
  private apiUrl = '/api/medicos';

  constructor(private http: HttpClient) {}

  listarActivos(): Observable<Medico[]> {
    return this.http.get<Medico[]>(this.apiUrl);
  }

  listarPorEspecialidad(especialidadId: number): Observable<Medico[]> {
    return this.http.get<Medico[]>(`${this.apiUrl}/especialidad/${especialidadId}`);
  }
}