package com.hackaton.service;

import com.hackaton.dto.MedicoDTO;
import com.hackaton.entity.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hackaton.repository.MedicoRepository;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public Long cadastroMedico(MedicoDTO medicoDTO) {

        var medico = new Medico();

        medico.setNome(medicoDTO.nome());
        medico.setCrm(medicoDTO.crm());
        medico.setEspecialidade(medicoDTO.especialidade());
        medico.setHora_entrada(medicoDTO.data_entrada());
        medico.setHora_saida(medicoDTO.data_saida());
        medico.setAtivo(true);

        var medicoSalvo = medicoRepository.save(medico);
        return medicoSalvo.getId();
    }

    @Transactional
    public Page<MedicoDTO> listarMedicos(Pageable pageable) {
        return medicoRepository.findAllByAtivoTrue(pageable).map(MedicoDTO::new);
    }

    @Transactional
    public Medico buscaPorId(Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        return medico;
    }

    @Transactional
    public void removerMedico(Long id) {
        Optional<Medico> medicoDeletar = medicoRepository.findById(id);
        medicoDeletar.ifPresent(value -> value.setAtivo(false));
    }

}