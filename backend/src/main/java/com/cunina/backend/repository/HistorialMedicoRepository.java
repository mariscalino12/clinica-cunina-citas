package com.cunina.backend.repository;

import com.cunina.backend.entity.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long> {
    List<HistorialMedico> findByPaciente_IdPaciente(Long pacienteId);
}