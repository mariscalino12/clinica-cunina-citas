package com.cunina.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "especialidad_sintoma")
public class EspecialidadSintoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relacion")
    private Long idRelacion;

    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "sintoma_id", nullable = false)
    private Sintoma sintoma;

    @Column(nullable = false)
    private Integer peso = 1;

    public EspecialidadSintoma() {
    }

    public EspecialidadSintoma(Long idRelacion, Especialidad especialidad, Sintoma sintoma, Integer peso) {
        this.idRelacion = idRelacion;
        this.especialidad = especialidad;
        this.sintoma = sintoma;
        this.peso = peso;
    }

    // Getters y Setters
    public Long getIdRelacion() { return idRelacion; }
    public void setIdRelacion(Long idRelacion) { this.idRelacion = idRelacion; }

    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    public Sintoma getSintoma() { return sintoma; }
    public void setSintoma(Sintoma sintoma) { this.sintoma = sintoma; }

    public Integer getPeso() { return peso; }
    public void setPeso(Integer peso) { this.peso = peso; }
}