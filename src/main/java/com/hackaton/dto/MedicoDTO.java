package com.hackaton.dto;

import com.hackaton.entity.Especialidade;
import com.hackaton.entity.Medico;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.sql.Time;
import java.time.LocalTime;

@EnableSpringDataWebSupport
public record MedicoDTO(

        Long id,

        String nome,

        @Pattern(regexp = "\\d{4,6}, message = \"Formato do CRM é inválido\"")
        String crm,

        Especialidade especialidade,

        String hora_entrada,

        String hora_saida,

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


