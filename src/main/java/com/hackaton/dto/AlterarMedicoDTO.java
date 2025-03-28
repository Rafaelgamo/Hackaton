package com.hackaton.dto;

import com.hackaton.entity.Especialidade;

public record AlterarMedicoDTO(
        Long id,
        String nome,
        String crm,
        Especialidade especialidade,
        String hora_entrada,
        String hora_saida
) {

}


