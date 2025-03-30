package com.hackaton.entity;

public enum Especialidade {

    ORTOPEDIA("Ortopedia"),
    CLINICO_GERAL("Clínico Geral"),
    CARDIOLOGIA("Cardiologia"),
    GINECOLOGIA("Ginecologia"),
    DERMATOLOGIA("Dermatologia");

    private final String nome;

    Especialidade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
