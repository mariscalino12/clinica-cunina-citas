package com.cunina.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "triaje_sintomas")
public class TriajeSintoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_triaje_sintoma")
    private Long idTriajeSintoma;

    @ManyToOne
    @JoinColumn(name = "triaje_id", nullable = false)
    private Triaje triaje;

    @ManyToOne
    @JoinColumn(name = "sintoma_id", nullable = false)
    private Sintoma sintoma;

    public TriajeSintoma() {
    }

    public TriajeSintoma(Long idTriajeSintoma, Triaje triaje, Sintoma sintoma) {
        this.idTriajeSintoma = idTriajeSintoma;
        this.triaje = triaje;
        this.sintoma = sintoma;
    }

    // Getters y Setters
    public Long getIdTriajeSintoma() { return idTriajeSintoma; }
    public void setIdTriajeSintoma(Long idTriajeSintoma) { this.idTriajeSintoma = idTriajeSintoma; }

    public Triaje getTriaje() { return triaje; }
    public void setTriaje(Triaje triaje) { this.triaje = triaje; }

    public Sintoma getSintoma() { return sintoma; }
    public void setSintoma(Sintoma sintoma) { this.sintoma = sintoma; }
}