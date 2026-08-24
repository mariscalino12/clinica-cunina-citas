package com.cunina.backend.repository;

import com.cunina.backend.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPaciente_IdPaciente(Long pacienteId);
    List<Cita> findByMedico_IdMedicoAndFechaHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByEstado(String estado);
}