package com.cunina.backend.controller;

import com.cunina.backend.dto.TriajeRequestDTO;
import com.cunina.backend.entity.Triaje;
import com.cunina.backend.service.TriajeService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> realizarTriaje(@Valid @RequestBody TriajeRequestDTO dto) {
        try {
            Triaje triaje = triajeService.realizarTriaje(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(triaje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Triaje>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(triajeService.listarPorPaciente(pacienteId));
    }
}