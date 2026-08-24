package com.cunina.backend.repository;

import com.cunina.backend.entity.Triaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TriajeRepository extends JpaRepository<Triaje, Long> {
    List<Triaje> findByPaciente_IdPaciente(Long pacienteId);
}