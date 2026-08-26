package com.cunina.backend.service;

import com.cunina.backend.dto.ReservaCitaRequestDTO;
import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final TarifaRepository tarifaRepository;
    private final TriajeRepository triajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final HorarioMedicoRepository horarioMedicoRepository; // Nuevo

    public CitaService(CitaRepository citaRepository,
                       PacienteRepository pacienteRepository,
                       MedicoRepository medicoRepository,
                       EspecialidadRepository especialidadRepository,
                       TarifaRepository tarifaRepository,
                       TriajeRepository triajeRepository,
                       UsuarioRepository usuarioRepository,
                       HorarioMedicoRepository horarioMedicoRepository) { // Nuevo parámetro
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
        this.tarifaRepository = tarifaRepository;
        this.triajeRepository = triajeRepository;
        this.usuarioRepository = usuarioRepository;
        this.horarioMedicoRepository = horarioMedicoRepository;
    }

    public Cita reservarCita(ReservaCitaRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        // Validación: paciente pertenece al tutor autenticado
        if (!paciente.getTutor().getIdUsuario().equals(obtenerTutorIdAutenticado())) {
            throw new RuntimeException("No tiene permiso para reservar cita para este paciente");
        }

        // Validación: médico pertenece a la especialidad seleccionada
        if (!medico.getEspecialidad().getIdEspecialidad().equals(especialidad.getIdEspecialidad())) {
            throw new RuntimeException("El médico no pertenece a la especialidad seleccionada");
        }

        if (!medico.getActivo()) {
            throw new RuntimeException("El médico no está activo");
        }

        if (dto.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha de la cita debe ser futura");
        }

        // Validación de horario laboral del médico
        Integer diaSemana = dto.getFechaHora().getDayOfWeek().getValue(); // 1=Lunes..7=Domingo
        List<HorarioMedico> horarios = horarioMedicoRepository
                .findByMedico_IdMedicoAndDiaSemana(medico.getIdMedico(), diaSemana);

        boolean dentroHorario = horarios.stream().anyMatch(h -> {
            LocalTime horaCita = dto.getFechaHora().toLocalTime();
            return !horaCita.isBefore(h.getHoraInicio()) && !horaCita.isAfter(h.getHoraFin());
        });

        if (!dentroHorario) {
            throw new RuntimeException("El médico no atiende en ese horario");
        }

        // Verificar solapamiento con citas existentes para el médico
        LocalDateTime inicio = dto.getFechaHora().minusMinutes(30);
        LocalDateTime fin = dto.getFechaHora().plusMinutes(30);
        List<Cita> citasMedico = citaRepository.findByMedico_IdMedicoAndFechaHoraBetween(
                medico.getIdMedico(), inicio, fin);
        for (Cita c : citasMedico) {
            if (!c.getEstado().equals("CANCELADA")) {
                throw new RuntimeException("El médico no está disponible en ese horario");
            }
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);
        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado("PENDIENTE");
        cita.setTipoConsulta("PRESENCIAL");
        cita.setEstadoPago("PENDIENTE");
        cita.setFechaCreacion(LocalDateTime.now());

        tarifaRepository.findByEspecialidad_IdEspecialidad(especialidad.getIdEspecialidad())
                .ifPresent(cita::setTarifa);

        if (dto.getTriajeId() != null) {
            Triaje triaje = triajeRepository.findById(dto.getTriajeId())
                    .orElseThrow(() -> new RuntimeException("Triaje no encontrado"));
            cita.setTriaje(triaje);
        }

        return citaRepository.save(cita);
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPaciente_IdPaciente(pacienteId);
    }

    public List<Cita> listarPorMedico(Long medicoId, LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByMedico_IdMedicoAndFechaHoraBetween(medicoId, inicio, fin);
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