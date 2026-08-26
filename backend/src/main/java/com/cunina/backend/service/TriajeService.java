package com.cunina.backend.service;

import com.cunina.backend.dto.TriajeRequestDTO;
import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TriajeService {

    private final TriajeRepository triajeRepository;
    private final TriajeSintomaRepository triajeSintomaRepository;
    private final EspecialidadSintomaRepository especialidadSintomaRepository;
    private final SintomaRepository sintomaRepository;
    private final PacienteRepository pacienteRepository;
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository; // Añadido

    public TriajeService(TriajeRepository triajeRepository,
                         TriajeSintomaRepository triajeSintomaRepository,
                         EspecialidadSintomaRepository especialidadSintomaRepository,
                         SintomaRepository sintomaRepository,
                         PacienteRepository pacienteRepository,
                         EspecialidadRepository especialidadRepository,
                         UsuarioRepository usuarioRepository) {
        this.triajeRepository = triajeRepository;
        this.triajeSintomaRepository = triajeSintomaRepository;
        this.especialidadSintomaRepository = especialidadSintomaRepository;
        this.sintomaRepository = sintomaRepository;
        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Triaje realizarTriaje(TriajeRequestDTO dto) {
        if (dto.getSintomaIds() == null || dto.getSintomaIds().isEmpty()) {
            throw new RuntimeException("Debe seleccionar al menos un síntoma");
        }

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Validación de que el paciente pertenece al tutor autenticado
        if (!paciente.getTutor().getIdUsuario().equals(obtenerTutorIdAutenticado())) {
            throw new RuntimeException("No tiene permiso para realizar triaje sobre este paciente");
        }

        Especialidad especialidadRecomendada = recomendarEspecialidad(dto.getSintomaIds());

        Triaje triaje = new Triaje();
        triaje.setPaciente(paciente);
        triaje.setFechaEvaluacion(LocalDateTime.now());
        triaje.setEspecialidadRecomendada(especialidadRecomendada);
        triaje.setNotas(dto.getNotas());
        triaje = triajeRepository.save(triaje);

        for (Long sintomaId : dto.getSintomaIds()) {
            Sintoma sintoma = sintomaRepository.findById(sintomaId)
                    .orElseThrow(() -> new RuntimeException("Síntoma no encontrado"));
            TriajeSintoma ts = new TriajeSintoma();
            ts.setTriaje(triaje);
            ts.setSintoma(sintoma);
            triajeSintomaRepository.save(ts);
        }

        return triaje;
    }

    private Especialidad recomendarEspecialidad(List<Long> sintomaIds) {
        List<EspecialidadSintoma> relaciones = especialidadSintomaRepository
                .findBySintoma_IdSintomaIn(sintomaIds);

        if (relaciones.isEmpty()) {
            return especialidadRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Especialidad por defecto no encontrada"));
        }

        Map<Long, Integer> puntajes = new HashMap<>();
        for (EspecialidadSintoma rel : relaciones) {
            Long espId = rel.getEspecialidad().getIdEspecialidad();
            puntajes.merge(espId, rel.getPeso(), Integer::sum);
        }

        Long especialidadId = puntajes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        return especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
    }

    public List<Triaje> listarPorPaciente(Long pacienteId) {
        return triajeRepository.findByPaciente_IdPaciente(pacienteId);
    }

    private Long obtenerTutorIdAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("No autenticado"));
            return usuario.getIdUsuario();
        }
        throw new RuntimeException("No autenticado");
    }
}