package com.hackaton.entity;

public enum StatusConfirmacao {

    DISPONIVEL,
    SEM_CONTATO_ELETRONICO,
    AGENDADO,
    AGUARDANDO_CONFIRMACAO,
    CONFIRMADO,
    CONCLUIDO,
    CANCELADO;

    public String getName() {
        return this.name();
    }

}
