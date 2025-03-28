package com.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hackaton.entity.StatusConfirmacao;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record NovoAgendamentoDTO(
        Long id,
        long idMedico,
        long idPaciente,
        String status,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate data,
        @DateTimeFormat(pattern = "HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime hora
) {
    public NovoAgendamentoDTO(Long id, NovoAgendamentoDTO source, StatusConfirmacao status) {
        this(
                id,
                source.idMedico(),
                source.idPaciente(),
                status.getName(),
                source.data(),
                source.hora()
        );
    }
}
