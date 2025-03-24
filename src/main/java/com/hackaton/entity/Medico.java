package com.hackaton.entity;

import com.hackaton.dto.CadastroMedicoDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    private String hora_entrada;
    private String hora_saida;
    private Boolean ativo;


    public Medico(CadastroMedicoDTO dados) {
        this.hora_saida = hora_saida;
        this.hora_entrada = hora_entrada;
        this.especialidade = especialidade;
        this.crm = crm;
        this.nome = nome;
        this.ativo = true;
    }

    public Medico(String nome, String crm, Especialidade especialidade, String hora_entrada, String hora_saida, Boolean ativo) {
        this.hora_saida = hora_saida;
        this.hora_entrada = hora_entrada;
        this.especialidade = especialidade;
        this.crm = crm;
        this.nome = nome;
        this.ativo = true;
    }

    public Medico() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public String getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getHora_saida() {
        return hora_saida;
    }

    public void setHora_saida(String hora_saida) {
        this.hora_saida = hora_saida;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = true;
    }
}