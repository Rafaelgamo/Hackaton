package com.hackaton.controller.json;

import com.hackaton.dto.AgendamentoDisponivelDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoDisponivelJson (
        long idMedico,
        String status,

        @DateTimeFormat(pattern = "YYYY-MM-DD")
        LocalDate data,

        @DateTimeFormat(pattern = "HH:mm")
        LocalTime hora
) {

    public AgendamentoDisponivelJson (AgendamentoDisponivelDTO agendamentoDisponivelDTO) {
        this(
                agendamentoDisponivelDTO.idMedico(),
                agendamentoDisponivelDTO.status(),
                agendamentoDisponivelDTO.data(),
                agendamentoDisponivelDTO.hora()
        );
    }

}
