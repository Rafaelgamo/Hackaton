package com.hackaton.controller;

import com.hackaton.dto.MarcacaoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    // TODO: CRUD de agendamento
    @PostMapping("/marcar")
    public ResponseEntity<Void> realizarAgendamento(MarcacaoDTO marcacaoDTO) {

        return ResponseEntity.ok().build();
    }

}
