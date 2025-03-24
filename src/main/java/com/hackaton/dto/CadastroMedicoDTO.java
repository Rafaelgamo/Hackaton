package com.hackaton.dto;

import com.hackaton.entity.Especialidade;

public record CadastroMedicoDTO(

        Long id,
        String nome,
        String crm,
        Especialidade especialidade,
        String data_entrada,
        String data_saida
) {

}


