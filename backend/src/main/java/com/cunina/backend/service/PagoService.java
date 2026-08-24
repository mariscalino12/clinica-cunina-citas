package com.cunina.backend.service;

import com.cunina.backend.entity.*;
import com.cunina.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;
    private final MetodoPagoRepository metodoPagoRepository;

    public PagoService(PagoRepository pagoRepository,
                       CitaRepository citaRepository,
                       MetodoPagoRepository metodoPagoRepository) {
        this.pagoRepository = pagoRepository;
        this.citaRepository = citaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
    }

    @Transactional
    public Pago registrarPago(Long citaId, Long metodoPagoId, BigDecimal monto, String comprobante) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(monto);
        pago.setFechaPago(LocalDateTime.now());
        pago.setComprobante(comprobante);
        pago.setEstado("COMPLETADO");

        // Actualizar estado de pago de la cita
        cita.setEstadoPago("PAGADO");
        citaRepository.save(cita);

        return pagoRepository.save(pago);
    }

    public List<Pago> listarPorCita(Long citaId) {
        return pagoRepository.findByCita_IdCita(citaId);
    }
}