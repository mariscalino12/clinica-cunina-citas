package com.cunina.backend.repository;

import com.cunina.backend.entity.RecetaMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecetaMedicamentoRepository extends JpaRepository<RecetaMedicamento, Long> {
    List<RecetaMedicamento> findByReceta_IdReceta(Long recetaId);
}