package com.hackaton.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Retorno - SUS",
        version = "0.0.1-dev",
        description = "POC de melhoria ao processo de agendamento de retorno do SUS",
        contact = @Contact(
                name = "Grupo 14 - 5ADJT",
                email = "rm354256@fiap.com.br"
        )
    )
)
public interface DocumentedAPI { }
