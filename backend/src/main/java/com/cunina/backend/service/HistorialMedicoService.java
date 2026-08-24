package com.cunina.backend.service;

import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final CitaRepository citaRepository;

    public HistorialMedicoService(HistorialMedicoRepository historialMedicoRepository,
                                  CitaRepository citaRepository) {
        this.historialMedicoRepository = historialMedicoRepository;
        this.citaRepository = citaRepository;
    }

    public HistorialMedico registrarAtencion(Long citaId, String diagnostico,
                                             String tratamiento, String notas) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        HistorialMedico historial = new HistorialMedico();
        historial.setCita(cita);
        historial.setPaciente(cita.getPaciente());
        historial.setDiagnostico(diagnostico);
        historial.setTratamiento(tratamiento);
        historial.setNotas(notas);
        historial.setFechaAtencion(LocalDateTime.now());

        // Actualizar estado de la cita a ATENDIDA
        cita.setEstado("ATENDIDA");
        citaRepository.save(cita);

        return historialMedicoRepository.save(historial);
    }

    public List<HistorialMedico> listarPorPaciente(Long pacienteId) {
        return historialMedicoRepository.findByPaciente_IdPaciente(pacienteId);
    }
}