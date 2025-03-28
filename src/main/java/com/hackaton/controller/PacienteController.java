package com.hackaton.controller;

import com.hackaton.dto.PacienteDTO;
import com.hackaton.entity.Paciente;
import com.hackaton.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastroPaciente(@RequestBody @Valid PacienteDTO pacienteDTO) {
        pacienteService.cadastroPaciente(pacienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<Paciente>> listarPacientes(Pageable paginacao) {
        var pacientesAtivos = pacienteService.listarPacientes(paginacao);
        return ResponseEntity.ok(pacientesAtivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscaId(@PathVariable(name = "id") Long id) {
        var pacienteDTO = pacienteService.buscarPacienteDTOPorId(id);
        if (pacienteDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pacienteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPaciente(@PathVariable(name = "id") Long id) {
        pacienteService.removerPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> alterarPaciente(@PathVariable(name = "id") Long id
//                                                    @RequestBody AlterarPacienteDTO alterarPacienteDTO
    ) {
//      TODO: var paciente = pacienteService.alterarPaciente(id, alterarPacienteDTO);
        return ResponseEntity.ok().build();
    }
}
