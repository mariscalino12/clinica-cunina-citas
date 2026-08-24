package com.cunina.backend.service;

import com.cunina.backend.entity.Medico;
import com.cunina.backend.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> listarActivos() {
        return medicoRepository.findByActivoTrue();
    }

    public List<Medico> listarPorEspecialidad(Long especialidadId) {
        return medicoRepository.findByEspecialidad_IdEspecialidad(especialidadId);
    }

    public Medico obtenerPorUsuarioId(Long usuarioId) {
        return medicoRepository.findByUsuario_IdUsuario(usuarioId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    }
}