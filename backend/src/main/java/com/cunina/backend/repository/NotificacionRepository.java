package com.cunina.backend.repository;

import com.cunina.backend.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuario_IdUsuarioAndLeidaFalse(Long usuarioId);
    List<Notificacion> findByUsuario_IdUsuario(Long usuarioId);
}