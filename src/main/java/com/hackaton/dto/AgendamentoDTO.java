package com.hackaton.dto;

import java.time.Instant;

public record AgendamentoDTO(
        long idMedico,
        long idPaciente,
        Instant dataHora
) {
}
