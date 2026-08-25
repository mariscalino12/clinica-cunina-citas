import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TriajeResult {
  idTriaje: number;
  especialidadRecomendada: { idEspecialidad: number; nombre: string };
  notas?: string;
}

@Injectable({ providedIn: 'root' })
export class TriajeService {
  private apiUrl = '/api/triajes';

  constructor(private http: HttpClient) {}

  realizarTriaje(pacienteId: number, sintomaIds: number[], notas?: string): Observable<TriajeResult> {
    return this.http.post<TriajeResult>(this.apiUrl, {
      pacienteId,
      sintomaIds,
      notas
    });
  }

  listarPorPaciente(pacienteId: number): Observable<TriajeResult[]> {
    return this.http.get<TriajeResult[]>(`${this.apiUrl}/paciente/${pacienteId}`);
  }
}