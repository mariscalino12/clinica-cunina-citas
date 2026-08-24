package com.cunina.backend.service;

import com.cunina.backend.entity.Especialidad;
import com.cunina.backend.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    public Especialidad obtenerPorId(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
    }

    public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }
}