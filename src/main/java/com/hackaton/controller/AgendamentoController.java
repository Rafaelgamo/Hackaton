package com.hackaton.controller;

import com.hackaton.dto.MarcacaoDTO;
import com.hackaton.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/marcar")
    public ResponseEntity<Void> realizarAgendamento(MarcacaoDTO marcacaoDTO) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email")
    public ResponseEntity<Void> enviarEmailConfirmacao() {
        agendamentoService.enviarEmailConfirmação(null);
        return ResponseEntity.ok().build();
    }
}
