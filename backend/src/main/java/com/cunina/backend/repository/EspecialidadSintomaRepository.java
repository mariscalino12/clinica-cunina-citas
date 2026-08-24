package com.cunina.backend.repository;

import com.cunina.backend.entity.EspecialidadSintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EspecialidadSintomaRepository extends JpaRepository<EspecialidadSintoma, Long> {
    List<EspecialidadSintoma> findBySintoma_IdSintomaIn(List<Long> sintomaIds);
    List<EspecialidadSintoma> findByEspecialidad_IdEspecialidad(Long especialidadId);
}