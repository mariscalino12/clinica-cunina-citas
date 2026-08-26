package com.cunina.backend.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class TriajeRequestDTO {

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotEmpty(message = "Debe seleccionar al menos un síntoma")
    private List<Long> sintomaIds;

    private String notas;

    // Getters y Setters
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public List<Long> getSintomaIds() { return sintomaIds; }
    public void setSintomaIds(List<Long> sintomaIds) { this.sintomaIds = sintomaIds; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}