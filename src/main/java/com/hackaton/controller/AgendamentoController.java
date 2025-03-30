package com.hackaton.controller;

import com.hackaton.controller.json.AgendamentoDisponivelJson;
import com.hackaton.controller.json.ConcluirAgendamentoJson;
import com.hackaton.controller.json.NovoAgendamentoJson;
import com.hackaton.dto.AgendamentoConcluidoDTO;
import com.hackaton.dto.AgendamentoDTO;
import com.hackaton.dto.NovoAgendamentoDTO;
import com.hackaton.service.AgendamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<NovoAgendamentoDTO> realizarAgendamento(@RequestBody @Valid NovoAgendamentoJson novoAgendamentoJson) {
        var horarioAgendado = agendamentoService.realizarAgendamento(novoAgendamentoJson.toDTO());
        return ResponseEntity.ok(horarioAgendado);
    }

    @PostMapping("/marcar/retorno")
    public ResponseEntity<NovoAgendamentoDTO> agendarRetorno(
        @RequestParam Long idAgendamento,
        @Pattern(regexp = "[1-2]\\d{3}-[0-3]?\\d-[0-3]?\\d", message = "Formato data: yyyy-MM-dd") @RequestParam("data") String data
    ) {
        var localDateData = LocalDate.parse(data);

        var novoAgendamentoDTO = agendamentoService.agendarRetorno(idAgendamento, localDateData);
        return ResponseEntity.ok(novoAgendamentoDTO);
    }

    @PostMapping("/concluir/{idAgendamento}")
    public ResponseEntity<AgendamentoConcluidoDTO> concluirAgendamento(
            @PathVariable Long idAgendamento,
            @RequestBody ConcluirAgendamentoJson concluirAgendamentoJson
    ) {
        var conclusaoAgendamentoDTO = agendamentoService.concluirAgendamento(idAgendamento, concluirAgendamentoJson.toDTO());
        return ResponseEntity.ok(conclusaoAgendamentoDTO);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<AgendamentoDisponivelJson>> listarHorariosDisponiveisPorDiaEMedico(
            @RequestParam("medicoId") Long medicoId,
            @Pattern(regexp = "[1-2]\\d{3}-[0-3]?\\d-[0-3]?\\d", message = "Formato data: yyyy-MM-dd") @RequestParam("data") String data
    ) {
        var localDateData = LocalDate.parse(data);

        var disponiveisDTOs = agendamentoService.listarHorariosDisponiveisPorDiaEMedico(localDateData, medicoId);
        var jsons = disponiveisDTOs.stream().map(AgendamentoDisponivelJson::new).toList();

        return ResponseEntity.ok(jsons);
    }

    @GetMapping("/disponiveis/hoje")
    public ResponseEntity<List<AgendamentoDisponivelJson>> listarHorariosDisponiveisPorDia(@RequestParam Long medicoId) {
        var localDateData = LocalDate.now();

        var disponiveisDTOs = agendamentoService.listarHorariosDisponiveisPorDiaEMedico(localDateData, medicoId);
        var jsons = disponiveisDTOs.stream().map(AgendamentoDisponivelJson::new).toList();

        return ResponseEntity.ok(jsons);
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<AgendamentoDTO>> listarPorPaciente(@PathVariable Long idPaciente) {
        var porPaciente = agendamentoService.listarPorPaciente(idPaciente);
        return ResponseEntity.ok(porPaciente);
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<AgendamentoDTO>> listarPorMedico(@PathVariable Long idMedico) {
        var porPaciente = agendamentoService.listarPorMedico(idMedico);
        return ResponseEntity.ok(porPaciente);
    }

    @PostMapping("/email")
    public ResponseEntity<Void> enviarEmailConfirmacao() {
        agendamentoService.enviarEmailConfirmação(null);
        return ResponseEntity.ok().build();
    }
}
