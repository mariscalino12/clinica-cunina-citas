package com.cunina.backend.repository;

import com.cunina.backend.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    boolean existsByNombre(String nombre);
}