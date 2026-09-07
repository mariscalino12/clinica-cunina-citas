import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-tutor-dashboard',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class TutorDashboardComponent implements OnInit {

  nombreUsuario = '';
  fotoPerfil: string | null = null;
  mostrarPerfil = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.nombreUsuario =
      this.authService.usuarioActual()?.nombre || 'Tutor';

    this.fotoPerfil =
      localStorage.getItem('fotoPerfilTutor');
  }

  irInicio(): void {
    this.router.navigate(['/tutor']);
  }

  estaEnInicio(): boolean {
    const url = this.router.url.replace(/\/+$/, '');
    return url === '/tutor';
  }

  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  abrirPerfil(): void {
    this.mostrarPerfil = true;
  }

  cerrarPerfil(): void {
    this.mostrarPerfil = false;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    if (!file.type.startsWith('image/')) {
      alert('Solo se permiten imágenes.');
      input.value = '';
      return;
    }

    if (file.size > 2 * 1024 * 1024) {
      alert('La imagen no debe superar los 2 MB.');
      input.value = '';
      return;
    }

    const reader = new FileReader();

    reader.onload = () => {
      const resultado = reader.result;

      if (typeof resultado === 'string') {
        this.fotoPerfil = resultado;
        localStorage.setItem('fotoPerfilTutor', resultado);
      }
    };

    reader.readAsDataURL(file);
    input.value = '';
  }
}