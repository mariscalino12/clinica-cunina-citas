package com.cunina.backend.repository;

import com.cunina.backend.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
    Optional<Receta> findByCita_IdCita(Long citaId);
    List<Receta> findByPaciente_IdPaciente(Long pacienteId);
}