package com.cunina.backend.controller;

import com.cunina.backend.entity.Triaje;
import com.cunina.backend.service.TriajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/triajes")
public class TriajeController {

    private final TriajeService triajeService;

    public TriajeController(TriajeService triajeService) {
        this.triajeService = triajeService;
    }

    @PostMapping
    public ResponseEntity<?> realizarTriaje(@RequestBody TriajeRequest request) {
        try {
            Triaje triaje = triajeService.realizarTriaje(
                    request.getPacienteId(),
                    request.getSintomaIds(),
                    request.getNotas()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(triaje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Triaje>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(triajeService.listarPorPaciente(pacienteId));
    }

    // DTO interno
    static class TriajeRequest {
        private Long pacienteId;
        private List<Long> sintomaIds;
        private String notas;
        // Getters y setters
        public Long getPacienteId() { return pacienteId; }
        public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
        public List<Long> getSintomaIds() { return sintomaIds; }
        public void setSintomaIds(List<Long> sintomaIds) { this.sintomaIds = sintomaIds; }
        public String getNotas() { return notas; }
        public void setNotas(String notas) { this.notas = notas; }
    }
}