package com.cunina.backend.config;

import com.cunina.backend.entity.Usuario;
import com.cunina.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByEmail("admin@cunina.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("CUNINA");
            admin.setEmail("admin@cunina.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRol("ADMIN");
            admin.setTelefono("999999999");
            admin.setFechaRegistro(LocalDateTime.now());
            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado: admin@cunina.com / admin123");
        }
    }
}