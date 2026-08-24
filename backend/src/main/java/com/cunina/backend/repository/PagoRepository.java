package com.cunina.backend.repository;

import com.cunina.backend.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByCita_IdCita(Long citaId);
}