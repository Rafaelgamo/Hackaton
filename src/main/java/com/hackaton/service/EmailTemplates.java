package com.hackaton.service;

public enum EmailTemplates {

    TEMPLATE_CONFIRMACAO("confirmacao", "SUS - Confirmação de Agendamento");

    private String template;
    private String titulo;

    EmailTemplates(String template, String titulo) {
        this.template = template;
        this.titulo = titulo;
    }

    public String getTemplate() {
        return template;
    }

    public String getTitulo() {
        return titulo;
    }
}
