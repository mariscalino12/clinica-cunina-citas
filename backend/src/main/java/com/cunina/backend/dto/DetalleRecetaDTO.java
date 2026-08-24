package com.cunina.backend.dto;

public class DetalleRecetaDTO {
    private Long medicamentoId;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String instrucciones;

    // Getters y Setters
    public Long getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}