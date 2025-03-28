package com.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hackaton.entity.Agendamento;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoDTO(
        long id,
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

    public AgendamentoDTO(Agendamento agendamento) {
        this(
                agendamento.getId(),
                agendamento.getMedico().getId(),
                agendamento.getPaciente().getId(),
                agendamento.getStatus().getName(),
                agendamento.getData(),
                agendamento.getHora()
        );
    }

    public AgendamentoDTO(NovoAgendamentoDTO novoAgendamentoDTO) {
        this(
            novoAgendamentoDTO.id(),
            novoAgendamentoDTO.idMedico(),
            novoAgendamentoDTO.idPaciente(),
            novoAgendamentoDTO.status(),
            novoAgendamentoDTO.data(),
            novoAgendamentoDTO.hora()
        );
    }
}
