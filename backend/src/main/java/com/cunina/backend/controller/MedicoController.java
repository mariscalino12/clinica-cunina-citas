package com.cunina.backend.controller;

import com.cunina.backend.entity.Medico;
import com.cunina.backend.service.MedicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public ResponseEntity<List<Medico>> listarActivos() {
        return ResponseEntity.ok(medicoService.listarActivos());
    }

    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<Medico>> listarPorEspecialidad(@PathVariable Long especialidadId) {
        return ResponseEntity.ok(medicoService.listarPorEspecialidad(especialidadId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Medico> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(medicoService.obtenerPorUsuarioId(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}