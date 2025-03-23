package com.hackaton.dto;

import com.hackaton.entity.Especialidade;
import com.hackaton.entity.Medico;

public record MedicoDTO(

        Long id,
        String nome,
        String crm,
        Especialidade especialidade,
        String data_entrada,
        String data_saida,
        Boolean ativo
) {
    public MedicoDTO(Medico medico) {
        this(
                medico.getId(),
                medico.getNome(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getHora_entrada(),
                medico.getHora_saida(),
                medico.getAtivo()
        );
    }


}


