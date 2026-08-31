import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface Usuario {
  idUsuario?: number;
  nombre?: string;
  apellido?: string;
  email?: string;
  rol?: string;
  token?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = '/api/auth';
  usuarioActual = signal<Usuario | null>(null);

  constructor(private http: HttpClient, private router: Router) {
    const guardado = localStorage.getItem('usuario');
    if (guardado) {
      this.usuarioActual.set(JSON.parse(guardado));
    }
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap((res: any) => {
          const usuario: Usuario = {
            idUsuario: res.idUsuario,
            nombre: res.nombre,
            apellido: res.apellido,
            email: res.email,
            rol: res.rol,
            token: res.token
          };
          localStorage.setItem('usuario', JSON.stringify(usuario));
          localStorage.setItem('token', res.token);
          this.usuarioActual.set(usuario);
        })
      );
  }

  registroTutor(datos: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/registro-tutor`, datos);
  }

  logout() {
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
    this.usuarioActual.set(null);
    this.router.navigate(['/login']);
  }
}