package com.cunina.backend.repository;

import com.cunina.backend.entity.TriajeSintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TriajeSintomaRepository extends JpaRepository<TriajeSintoma, Long> {
    List<TriajeSintoma> findByTriaje_IdTriaje(Long triajeId);
}