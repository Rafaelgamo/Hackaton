package com.hackaton.dto;

import com.hackaton.entity.Especialidade;
import com.hackaton.entity.Medico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
public record MedicoDTO(

        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Pattern(regexp = "\\d{4,6}", message = "CRM deve conter entre 4 e 6 dígitos numéricos")
        String crm,

        @NotNull
        Especialidade especialidade,

        @NotBlank(message = "Hora de entrada é obrigatória")
        String hora_entrada,

        @NotBlank(message = "Hora de saída é obrigatória")
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


