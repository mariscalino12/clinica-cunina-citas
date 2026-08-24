package com.cunina.backend.repository;

import com.cunina.backend.entity.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {
    List<HorarioMedico> findByMedico_IdMedicoAndDiaSemana(Long medicoId, Integer diaSemana);
    List<HorarioMedico> findByMedico_IdMedico(Long medicoId);
}