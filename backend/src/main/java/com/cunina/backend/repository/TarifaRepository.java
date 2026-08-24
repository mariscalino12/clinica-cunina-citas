package com.cunina.backend.repository;

import com.cunina.backend.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findByEspecialidad_IdEspecialidad(Long especialidadId);
}