package com.cunina.backend.service;

import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
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

    public TriajeService(TriajeRepository triajeRepository,
                         TriajeSintomaRepository triajeSintomaRepository,
                         EspecialidadSintomaRepository especialidadSintomaRepository,
                         SintomaRepository sintomaRepository,
                         PacienteRepository pacienteRepository,
                         EspecialidadRepository especialidadRepository) {
        this.triajeRepository = triajeRepository;
        this.triajeSintomaRepository = triajeSintomaRepository;
        this.especialidadSintomaRepository = especialidadSintomaRepository;
        this.sintomaRepository = sintomaRepository;
        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    public Triaje realizarTriaje(Long pacienteId, List<Long> sintomaIds, String notas) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Calcular especialidad recomendada
        Especialidad especialidadRecomendada = recomendarEspecialidad(sintomaIds);

        // Crear triaje
        Triaje triaje = new Triaje();
        triaje.setPaciente(paciente);
        triaje.setFechaEvaluacion(LocalDateTime.now());
        triaje.setEspecialidadRecomendada(especialidadRecomendada);
        triaje.setNotas(notas);
        triaje = triajeRepository.save(triaje);

        // Guardar síntomas seleccionados
        for (Long sintomaId : sintomaIds) {
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
            // Si no hay coincidencias, recomendar Pediatría General (id=1)
            return especialidadRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Especialidad por defecto no encontrada"));
        }

        // Sumar pesos por especialidad
        Map<Long, Integer> puntajes = new HashMap<>();
        for (EspecialidadSintoma rel : relaciones) {
            Long espId = rel.getEspecialidad().getIdEspecialidad();
            puntajes.merge(espId, rel.getPeso(), Integer::sum);
        }

        // Obtener la especialidad con mayor puntaje
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
}