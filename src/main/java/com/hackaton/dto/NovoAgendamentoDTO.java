package com.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hackaton.entity.StatusConfirmacao;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record NovoAgendamentoDTO(
        long idMedico,
        long idPaciente,
        String status,
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD")
        LocalDate data,
        @DateTimeFormat(pattern = "HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime hora
) {
        public NovoAgendamentoDTO(NovoAgendamentoDTO source, StatusConfirmacao status) {
                this(
                        source.idMedico(),
                        source.idPaciente(),
                        status.getName(),
                        source.data(),
                        source.hora()
                );
        }
}
