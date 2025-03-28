package com.hackaton.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoDisponivelDTO (
        long idMedico,
        String status,

        @DateTimeFormat(pattern = "YYYY-MM-DD")
        LocalDate data,

        @DateTimeFormat(pattern = "HH:mm")
        LocalTime hora
) {
}
