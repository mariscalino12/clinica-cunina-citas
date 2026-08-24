package com.cunina.backend.service;

import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final TarifaRepository tarifaRepository;
    private final TriajeRepository triajeRepository;

    public CitaService(CitaRepository citaRepository,
                       PacienteRepository pacienteRepository,
                       MedicoRepository medicoRepository,
                       EspecialidadRepository especialidadRepository,
                       TarifaRepository tarifaRepository,
                       TriajeRepository triajeRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
        this.tarifaRepository = tarifaRepository;
        this.triajeRepository = triajeRepository;
    }

    public Cita reservarCita(Long pacienteId, Long medicoId, Long especialidadId,
                             Long triajeId, LocalDateTime fechaHora) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        // Verificar que la fecha sea futura
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha de la cita debe ser futura");
        }

        // Verificar disponibilidad: que no exista otra cita para el médico en ese horario
        List<Cita> citasMedico = citaRepository.findByMedico_IdMedicoAndFechaHoraBetween(
                medicoId, fechaHora.minusMinutes(30), fechaHora.plusMinutes(30));
        if (!citasMedico.isEmpty()) {
            throw new RuntimeException("El médico no está disponible en ese horario");
        }

        // Crear la cita
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);
        cita.setFechaHora(fechaHora);
        cita.setEstado("PENDIENTE");
        cita.setTipoConsulta("PRESENCIAL");
        cita.setEstadoPago("PENDIENTE");
        cita.setFechaCreacion(LocalDateTime.now());

        // Asociar tarifa según especialidad
        tarifaRepository.findByEspecialidad_IdEspecialidad(especialidadId)
                .ifPresent(cita::setTarifa);

        // Asociar triaje si se proporciona
        if (triajeId != null) {
            Triaje triaje = triajeRepository.findById(triajeId)
                    .orElseThrow(() -> new RuntimeException("Triaje no encontrado"));
            cita.setTriaje(triaje);
            cita.setSintomasDescripcion("Triaje realizado");
        }

        return citaRepository.save(cita);
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPaciente_IdPaciente(pacienteId);
    }

    public List<Cita> listarPorMedico(Long medicoId, LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByMedico_IdMedicoAndFechaHoraBetween(medicoId, inicio, fin);
    }
}