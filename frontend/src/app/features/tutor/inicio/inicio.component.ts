import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-tutor-inicio',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  nombreUsuario = '';

  constructor(
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.nombreUsuario =
      this.authService.usuarioActual()?.nombre || 'Tutor';
  }
}