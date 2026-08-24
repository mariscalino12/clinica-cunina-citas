package com.cunina.backend.repository;

import com.cunina.backend.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByUsuario_IdUsuario(Long usuarioId);
    List<Medico> findByEspecialidad_IdEspecialidad(Long especialidadId);
    List<Medico> findByActivoTrue();
}