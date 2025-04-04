package com.hackaton.service;

import com.hackaton.dto.AlterarMedicoDTO;
import com.hackaton.dto.MedicoDTO;
import com.hackaton.entity.Medico;
import com.hackaton.exception.NaoEncontradoException;
import com.hackaton.exception.ValidacaoException;
import com.hackaton.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public Long cadastroMedico(MedicoDTO medicoDTO) {

        if (medicoRepository.existsByCrm(medicoDTO.crm())) {
            throw new ValidacaoException("CRM " + medicoDTO.crm() + " já está cadastrado.");
        }

        var medico = new Medico();

        medico.setNome(medicoDTO.nome());
        medico.setCrm(medicoDTO.crm());
        medico.setEspecialidade(medicoDTO.especialidade());
        medico.setHora_entrada(medicoDTO.hora_entrada());
        medico.setHora_saida(medicoDTO.hora_saida());
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
        var medico = medicoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException(id));
        return medico;
    }

    @Transactional
    public ResponseEntity<Medico> alterarMedico(Long id, AlterarMedicoDTO alterarMedicoDTO) {
        var alteracao = medicoRepository.findByIdAndAtivoTrue(id);
        if (alteracao.isPresent()) {

            var medico = alteracao.get();
            medico.setNome(alterarMedicoDTO.nome());
            medico.setEspecialidade(alterarMedicoDTO.especialidade());
            medico.setHora_entrada(alterarMedicoDTO.hora_entrada());
            medico.setHora_saida(alterarMedicoDTO.hora_saida());

            var alter = medicoRepository.save(medico);
            return ResponseEntity.ok(alter);
        }
        return null;
    }

    @Transactional
    public void removerMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException(id));
        medico.setAtivo(false);
    }

}

