package com.hackaton.controller.json;

import com.hackaton.dto.ConcluirAgendamentoDTO;

public record ConcluirAgendamentoJson(
        Boolean agendarRetorno
) {
    public ConcluirAgendamentoDTO toDTO() {
        return new ConcluirAgendamentoDTO(agendarRetorno == null ? false : agendarRetorno);
    }
}
