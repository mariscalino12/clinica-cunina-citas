package com.cunina.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triajes")
public class Triaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_triaje")
    private Long idTriaje;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;

    @ManyToOne
    @JoinColumn(name = "especialidad_recomendada_id", nullable = false)
    private Especialidad especialidadRecomendada;

    @Column(columnDefinition = "TEXT")
    private String notas;

    public Triaje() {
    }

    public Triaje(Long idTriaje, Paciente paciente, LocalDateTime fechaEvaluacion,
                  Especialidad especialidadRecomendada, String notas) {
        this.idTriaje = idTriaje;
        this.paciente = paciente;
        this.fechaEvaluacion = fechaEvaluacion;
        this.especialidadRecomendada = especialidadRecomendada;
        this.notas = notas;
    }

    // Getters y Setters
    public Long getIdTriaje() { return idTriaje; }
    public void setIdTriaje(Long idTriaje) { this.idTriaje = idTriaje; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }

    public Especialidad getEspecialidadRecomendada() { return especialidadRecomendada; }
    public void setEspecialidadRecomendada(Especialidad especialidadRecomendada) { this.especialidadRecomendada = especialidadRecomendada; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}