package com.hackaton.controller;

import com.hackaton.controller.json.AgendamentoDisponivelJson;
import com.hackaton.dto.NovoAgendamentoDTO;
import com.hackaton.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/marcar")
    public ResponseEntity<NovoAgendamentoDTO> realizarAgendamento(@RequestBody @Valid NovoAgendamentoDTO novoAgendamentoDTO) {
        var horarioAgendado = agendamentoService.realizarAgendamento(novoAgendamentoDTO);
        return ResponseEntity.ok(horarioAgendado);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<AgendamentoDisponivelJson>> listarHorariosDisponiveisPorDiaEMedico(
        @RequestParam("medicoId") Long medicoId,

        @RequestParam(value = "data")
//        @Pattern(regexp = "[1-2]\\d{3}-[0-1]?\\d-[0-3]?\\d", message = "A 'data' deve estar no padrão YYYY-MM-DD.")
        String data

    ) {
        var localDateData = LocalDate.parse(data);

        var disponiveisDTOs = agendamentoService.listarHorariosDisponiveisPorDiaEMedico(localDateData, medicoId);
        var jsons = disponiveisDTOs.stream().map(AgendamentoDisponivelJson::new).toList();

        return ResponseEntity.ok(jsons);
    }

    @GetMapping("/disponiveis/hoje")
    public ResponseEntity listarHorariosDisponiveisPorDia(
            @RequestParam Long medicoId
    ) {
        var localDateData = LocalDate.now();

        var disponiveisDTOs = agendamentoService.listarHorariosDisponiveisPorDiaEMedico(localDateData, medicoId);
        var jsons = disponiveisDTOs.stream().map(AgendamentoDisponivelJson::new).toList();

        return ResponseEntity.ok(jsons);
    }

    @PostMapping("/email")
    public ResponseEntity<Void> enviarEmailConfirmacao() {
        agendamentoService.enviarEmailConfirmação(null);
        return ResponseEntity.ok().build();
    }
}
