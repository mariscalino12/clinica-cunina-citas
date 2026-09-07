package com.cunina.backend.service;

import com.cunina.backend.dto.RegistroPacienteDTO;
import com.cunina.backend.entity.Paciente;
import com.cunina.backend.entity.Usuario;
import com.cunina.backend.repository.PacienteRepository;
import com.cunina.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PacienteService(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Paciente registrarPaciente(RegistroPacienteDTO dto, Long tutorId) {
        Usuario tutor = usuarioRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));

        // Validación de fecha de nacimiento y edad
        LocalDate fechaNacimiento = dto.getFechaNacimiento();
        LocalDate hoy = LocalDate.now();

        if (fechaNacimiento == null) {
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        }
        if (fechaNacimiento.isAfter(hoy)) {
            throw new RuntimeException("La fecha de nacimiento no puede ser futura");
        }

        Period edad = Period.between(fechaNacimiento, hoy);
        int anios = edad.getYears();
        if (anios < 0 || anios > 15) {
            throw new RuntimeException("La edad debe estar entre 0 y 15 años");
        }

        // Validar duplicado por nombre, apellido y fecha de nacimiento para el mismo tutor
        boolean existe = pacienteRepository
                .existsByTutor_IdUsuarioAndNombreIgnoreCaseAndApellidoIgnoreCaseAndFechaNacimiento(
                        tutorId, dto.getNombre(), dto.getApellido(), dto.getFechaNacimiento());
        if (existe) {
            throw new RuntimeException("El paciente ya está registrado.");
        }

        // Validar DNI único si se proporciona
        if (dto.getDni() != null && !dto.getDni().isEmpty()) {
            if (!dto.getDni().matches("\\d{8}")) {
                throw new RuntimeException("El DNI debe tener exactamente 8 dígitos");
            }
            if (pacienteRepository.existsByDni(dto.getDni())) {
                throw new RuntimeException("El DNI ya está registrado");
            }
        }

        Paciente paciente = new Paciente();
        paciente.setTutor(tutor);
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setGenero(dto.getGenero());
        paciente.setGrupoSanguineo(dto.getGrupoSanguineo());
        paciente.setAlergias(dto.getAlergias());
        paciente.setDni(dto.getDni());

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