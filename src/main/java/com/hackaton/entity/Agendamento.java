package com.hackaton.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_paciente")
    @ManyToOne(targetEntity = Paciente.class, fetch = FetchType.LAZY)
    private Paciente paciente;

    @JoinColumn(name = "id_medico")
    @ManyToOne(targetEntity = Medico.class, fetch = FetchType.LAZY)
    private Medico medico;

    private Boolean ehUltimoDoDia;

    @Enumerated(EnumType.STRING)
    private StatusConfirmacao status;

    @Column(name = "data", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate data;

    @Column(name = "hora", nullable = false)
    @Temporal(TemporalType.TIME)
    private LocalTime hora;

    public Agendamento(Long id, Paciente paciente, Medico medico, Boolean ehUltimoDoDia, StatusConfirmacao status, LocalDate data, LocalTime hora) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.ehUltimoDoDia = ehUltimoDoDia;
        this.status = status;
        this.data = data;
        this.hora = hora;
    }

    public Agendamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Boolean getEhUltimoDoDia() {
        return ehUltimoDoDia;
    }

    public void setEhUltimoDoDia(Boolean ehUltimoDoDia) {
        this.ehUltimoDoDia = ehUltimoDoDia;
    }

    public StatusConfirmacao getStatus() {
        return status;
    }

    public void setStatus(StatusConfirmacao statusConfirmacao) {
        this.status = statusConfirmacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}
