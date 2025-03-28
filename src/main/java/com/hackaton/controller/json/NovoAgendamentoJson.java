package com.hackaton.controller.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hackaton.dto.NovoAgendamentoDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record NovoAgendamentoJson(
        @NotNull @Positive Long idMedico,
        @NotNull @Positive Long idPaciente,
        String status,
        @DateTimeFormat(pattern = "YYYY-MM-DD")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD")
        LocalDate data,
        @DateTimeFormat(pattern = "HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime hora
) {

        public NovoAgendamentoJson(NovoAgendamentoDTO novoAgendamentoDTO) {
                this(
                        novoAgendamentoDTO.idMedico(),
                        novoAgendamentoDTO.idPaciente(),
                        novoAgendamentoDTO.status(),
                        novoAgendamentoDTO.data(),
                        novoAgendamentoDTO.hora()
                );
        }
}
