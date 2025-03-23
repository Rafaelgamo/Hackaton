package controller;

import dto.CadastroMedicoDTO;
import dto.MedicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MedicoService;


@RestController
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Void> cadastroMedico(@RequestBody CadastroMedicoDTO cadastroMedicoDTO) {
        medicoService.cadastroMedico(cadastroMedicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @GetMapping
    public ResponseEntity<Page<MedicoDTO>> listarMedicos(Pageable paginacao) {
        var medicosAtivos = medicoService.listrMedicos(paginacao);
        return ResponseEntity.ok(medicosAtivos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerMedcio(@PathVariable(name = "id") Long atendenteId) {
        medicoService.removerMedico(atendenteId);
        return ResponseEntity.noContent().build();
    }

}



