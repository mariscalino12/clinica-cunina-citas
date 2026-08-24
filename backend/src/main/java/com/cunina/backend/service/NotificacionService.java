package com.cunina.backend.service;

import com.cunina.backend.entity.Notificacion;
import com.cunina.backend.entity.Usuario;
import com.cunina.backend.repository.NotificacionRepository;
import com.cunina.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Notificacion crearNotificacion(Long usuarioId, String mensaje) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now());
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listarNoLeidas(Long usuarioId) {
        return notificacionRepository.findByUsuario_IdUsuarioAndLeidaFalse(usuarioId);
    }

    public List<Notificacion> listarTodas(Long usuarioId) {
        return notificacionRepository.findByUsuario_IdUsuario(usuarioId);
    }

    public void marcarComoLeida(Long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }
}