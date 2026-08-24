package com.cunina.backend.controller;

import com.cunina.backend.dto.DetalleRecetaDTO;
import com.cunina.backend.entity.Receta;
import com.cunina.backend.service.RecetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RecetaRequest request) {
        try {
            Receta receta = recetaService.crearReceta(
                    request.getCitaId(),
                    request.getIndicacionesGenerales(),
                    request.getDetalles()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(receta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Receta>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(recetaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<Receta> obtenerPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(recetaService.obtenerPorCita(citaId));
    }

    // DTO interno
    static class RecetaRequest {
        private Long citaId;
        private String indicacionesGenerales;
        private List<DetalleRecetaDTO> detalles;
        // Getters y setters
        public Long getCitaId() { return citaId; }
        public void setCitaId(Long citaId) { this.citaId = citaId; }
        public String getIndicacionesGenerales() { return indicacionesGenerales; }
        public void setIndicacionesGenerales(String indicacionesGenerales) { this.indicacionesGenerales = indicacionesGenerales; }
        public List<DetalleRecetaDTO> getDetalles() { return detalles; }
        public void setDetalles(List<DetalleRecetaDTO> detalles) { this.detalles = detalles; }
    }
}