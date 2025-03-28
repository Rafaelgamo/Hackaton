package com.hackaton.controller;

import com.hackaton.dto.AlterarMedicoDTO;
import com.hackaton.dto.MedicoDTO;
import com.hackaton.entity.Medico;
import com.hackaton.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Void> cadastroMedico(@RequestBody MedicoDTO medicoDTO) {
        medicoService.cadastroMedico(medicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<MedicoDTO>> listarMedicos(Pageable paginacao) {
        var medicosAtivos = medicoService.listarMedicos(paginacao);
        return ResponseEntity.ok(medicosAtivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscaId(@PathVariable(name = "id") Long id) {
        var medico = medicoService.buscaPorId(id);
        return ResponseEntity.ok(new MedicoDTO(medico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerMedico(@PathVariable(name = "id") Long id) {
        medicoService.removerMedico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> alterarPaciente(@PathVariable(name = "id") Long id,
                                                            @RequestBody AlterarMedicoDTO alterarMedicoDTO) {
        var medico = medicoService.alterarMedico(id, alterarMedicoDTO);
        return ResponseEntity.ok(medico.getBody());
    }

}



