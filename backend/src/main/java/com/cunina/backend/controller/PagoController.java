package com.cunina.backend.controller;

import com.cunina.backend.entity.Pago;
import com.cunina.backend.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody PagoRequest request) {
        try {
            Pago pago = pagoService.registrarPago(
                    request.getCitaId(),
                    request.getMetodoPagoId(),
                    request.getMonto(),
                    request.getComprobante()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(pago);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<Pago>> listarPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(pagoService.listarPorCita(citaId));
    }

    // DTO interno
    static class PagoRequest {
        private Long citaId;
        private Long metodoPagoId;
        private BigDecimal monto;
        private String comprobante;
        // Getters y setters
        public Long getCitaId() { return citaId; }
        public void setCitaId(Long citaId) { this.citaId = citaId; }
        public Long getMetodoPagoId() { return metodoPagoId; }
        public void setMetodoPagoId(Long metodoPagoId) { this.metodoPagoId = metodoPagoId; }
        public BigDecimal getMonto() { return monto; }
        public void setMonto(BigDecimal monto) { this.monto = monto; }
        public String getComprobante() { return comprobante; }
        public void setComprobante(String comprobante) { this.comprobante = comprobante; }
    }
}