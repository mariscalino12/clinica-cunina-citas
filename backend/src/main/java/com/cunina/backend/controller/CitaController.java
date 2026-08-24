package com.cunina.backend.controller;

import com.cunina.backend.entity.Cita;
import com.cunina.backend.service.CitaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<?> reservar(@RequestBody ReservaCitaRequest request) {
        try {
            Cita cita = citaService.reservarCita(
                    request.getPacienteId(),
                    request.getMedicoId(),
                    request.getEspecialidadId(),
                    request.getTriajeId(),
                    request.getFechaHora()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(cita);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Cita>> listarPorMedico(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(citaService.listarPorMedico(medicoId, inicio, fin));
    }

    // DTO interno
    static class ReservaCitaRequest {
        private Long pacienteId;
        private Long medicoId;
        private Long especialidadId;
        private Long triajeId;
        private LocalDateTime fechaHora;
        // Getters y setters
        public Long getPacienteId() { return pacienteId; }
        public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
        public Long getMedicoId() { return medicoId; }
        public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
        public Long getEspecialidadId() { return especialidadId; }
        public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; }
        public Long getTriajeId() { return triajeId; }
        public void setTriajeId(Long triajeId) { this.triajeId = triajeId; }
        public LocalDateTime getFechaHora() { return fechaHora; }
        public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    }
}