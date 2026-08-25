import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Especialidad {
  idEspecialidad: number;
  nombre: string;
  descripcion: string;
  imagenUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class EspecialidadService {
  private apiUrl = '/api/especialidades';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Especialidad> {
    return this.http.get<Especialidad>(`${this.apiUrl}/${id}`);
  }
}