package com.cunina.backend.controller;

import com.cunina.backend.entity.Paciente;
import com.cunina.backend.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/tutor/{tutorId}")
    public ResponseEntity<?> registrar(@PathVariable Long tutorId, @RequestBody Paciente paciente) {
        try {
            Paciente nuevo = pacienteService.registrarPaciente(paciente, tutorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Paciente>> listarPorTutor(@PathVariable Long tutorId) {
        return ResponseEntity.ok(pacienteService.listarPorTutor(tutorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}