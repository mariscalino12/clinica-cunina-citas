package com.cunina.backend.repository;

import com.cunina.backend.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByTutor_IdUsuario(Long tutorId);
    boolean existsByTutor_IdUsuarioAndNombreIgnoreCaseAndApellidoIgnoreCaseAndFechaNacimiento(
            Long tutorId, String nombre, String apellido, LocalDate fechaNacimiento);
    boolean existsByDni(String dni);
}