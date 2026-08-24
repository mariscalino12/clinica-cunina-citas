package com.cunina.backend.controller;

import com.cunina.backend.entity.Usuario;
import com.cunina.backend.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro-tutor")
    public ResponseEntity<?> registrarTutor(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarTutor(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login temporal (solo devuelve el usuario sin validar contraseña)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var usuarioOpt = usuarioService.buscarPorEmail(loginRequest.getEmail());
        if (usuarioOpt.isPresent()) {
            Usuario u = usuarioOpt.get();
            u.setPasswordHash(null);
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    // DTO interno
    static class LoginRequest {
        private String email;
        private String password;
        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}