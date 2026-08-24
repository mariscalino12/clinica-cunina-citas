package com.cunina.backend.service;

import com.cunina.backend.dto.DetalleRecetaDTO;
import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final RecetaMedicamentoRepository recetaMedicamentoRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public RecetaService(RecetaRepository recetaRepository,
                         RecetaMedicamentoRepository recetaMedicamentoRepository,
                         MedicamentoRepository medicamentoRepository,
                         CitaRepository citaRepository,
                         PacienteRepository pacienteRepository,
                         MedicoRepository medicoRepository) {
        this.recetaRepository = recetaRepository;
        this.recetaMedicamentoRepository = recetaMedicamentoRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public Receta crearReceta(Long citaId, String indicacionesGenerales,
                              List<DetalleRecetaDTO> detalles) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Receta receta = new Receta();
        receta.setCita(cita);
        receta.setPaciente(cita.getPaciente());
        receta.setMedico(cita.getMedico());
        receta.setFechaEmision(LocalDateTime.now());
        receta.setIndicacionesGenerales(indicacionesGenerales);
        receta.setEstado("EMITIDA");
        receta = recetaRepository.save(receta);

        for (DetalleRecetaDTO detalle : detalles) {
            Medicamento medicamento = medicamentoRepository.findById(detalle.getMedicamentoId())
                    .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));

            RecetaMedicamento rm = new RecetaMedicamento();
            rm.setReceta(receta);
            rm.setMedicamento(medicamento);
            rm.setDosis(detalle.getDosis());
            rm.setFrecuencia(detalle.getFrecuencia());
            rm.setDuracion(detalle.getDuracion());
            rm.setInstrucciones(detalle.getInstrucciones());
            recetaMedicamentoRepository.save(rm);
        }

        return receta;
    }

    public List<Receta> listarPorPaciente(Long pacienteId) {
        return recetaRepository.findByPaciente_IdPaciente(pacienteId);
    }

    public Receta obtenerPorCita(Long citaId) {
        return recetaRepository.findByCita_IdCita(citaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }
}