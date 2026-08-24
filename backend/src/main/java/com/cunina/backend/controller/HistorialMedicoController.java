package com.cunina.backend.controller;

import com.cunina.backend.entity.HistorialMedico;
import com.cunina.backend.service.HistorialMedicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialMedicoController {

    private final HistorialMedicoService historialMedicoService;

    public HistorialMedicoController(HistorialMedicoService historialMedicoService) {
        this.historialMedicoService = historialMedicoService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody HistorialRequest request) {
        try {
            HistorialMedico historial = historialMedicoService.registrarAtencion(
                    request.getCitaId(),
                    request.getDiagnostico(),
                    request.getTratamiento(),
                    request.getNotas()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(historial);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HistorialMedico>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historialMedicoService.listarPorPaciente(pacienteId));
    }

    // DTO interno
    static class HistorialRequest {
        private Long citaId;
        private String diagnostico;
        private String tratamiento;
        private String notas;
        // Getters y setters
        public Long getCitaId() { return citaId; }
        public void setCitaId(Long citaId) { this.citaId = citaId; }
        public String getDiagnostico() { return diagnostico; }
        public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
        public String getTratamiento() { return tratamiento; }
        public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
        public String getNotas() { return notas; }
        public void setNotas(String notas) { this.notas = notas; }
    }
}