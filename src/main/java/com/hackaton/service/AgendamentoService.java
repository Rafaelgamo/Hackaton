package com.hackaton.service;

import com.hackaton.dto.AgendamentoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AgendamentoService {

    private final EmailService emailService;

    @Value("${application.baseurl}")
    private String baseUrl;

    public AgendamentoService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void enviarEmailConfirmação(AgendamentoDTO agendamentoDTO) {
        var template = EmailTemplates.TEMPLATE_CONFIRMACAO;

        var properties = new HashMap<String, Object>();
        properties.put("nomePaciente", "Eu mesmo");
        properties.put("dataAgendamento", "25/03/2025");
        properties.put("horaAgendamento", "14:30");
        properties.put("especialidade", "Clinico Geral");

        properties.put("urlConfirmar", baseUrl + "/confirmar/" + 5);
        properties.put("urlCancelar", baseUrl + "/cancelar/" + 5);

        emailService.sendEmail(
                template.getTemplate(),
                "caioalves_diogo@hotmail.com",
                template.getTitulo(),
                properties
        );
    }

}
