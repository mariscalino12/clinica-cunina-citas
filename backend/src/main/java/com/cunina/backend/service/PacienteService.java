package com.cunina.backend.service;

import com.cunina.backend.entity.Paciente;
import com.cunina.backend.entity.Usuario;
import com.cunina.backend.repository.PacienteRepository;
import com.cunina.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PacienteService(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Paciente registrarPaciente(Paciente paciente, Long tutorId) {
        Usuario tutor = usuarioRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
        paciente.setTutor(tutor);
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarPorTutor(Long tutorId) {
        return pacienteRepository.findByTutor_IdUsuario(tutorId);
    }

    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }
}