package com.hackaton.service;

import com.hackaton.dto.PacienteDTO;
import com.hackaton.entity.Paciente;
import com.hackaton.exception.ConflitoException;
import com.hackaton.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {


    private static final Logger log = LoggerFactory.getLogger(PacienteService.class);
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente buscarPorId(Long pacienteId) {
        var paciente = pacienteRepository.findById(pacienteId).orElse(null);
        return paciente;
    }

    public PacienteDTO buscarPacienteDTOPorId(Long pacienteId) {
        var paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) return null;

        return new PacienteDTO(paciente);
    }

    public Page<Paciente> listarPacientes(Pageable paginacao) {
        return pacienteRepository.findAllByAtivoTrue(paginacao);
    }

    public void cadastroPaciente(PacienteDTO pacienteDTO) {
        var pacienteComCPF = pacienteRepository.findByCpf(pacienteDTO.cpf());
        if (pacienteComCPF != null) {
            throw new ConflitoException("CPF já cadastrado");
        }

        var novoPaciente = new Paciente();
        novoPaciente.setNome(pacienteDTO.nome());
        novoPaciente.setCpf(pacienteDTO.cpf());
        novoPaciente.setEmail(pacienteDTO.email());
        novoPaciente.setEndereco(pacienteDTO.endereco());
        novoPaciente.setAtivo(true);

        pacienteRepository.save(novoPaciente);
    }

    public void removerPaciente(Long id) {

        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente não encontrado.");
        }
        try {
            pacienteRepository.deleteById(id);
            log.info("Paciente com id {} removido com sucesso",id);
        }catch (Exception e){
            log.error("Erro ao remover paciente com id {}: {}",id, e.getMessage());
            throw new RuntimeException("Erro ao remover paciente",e);
        }

    }

    public Paciente alteraPaciente(PacienteDTO pacienteDTO) {

        Paciente pacienteExistente = pacienteRepository.findByCpf(pacienteDTO.cpf());
        if (pacienteExistente == null) {
            throw new IllegalArgumentException("Paciente não encontrado com o CPF informado.");
        }

        pacienteExistente.setNome(pacienteDTO.nome());
        pacienteExistente.setCpf(pacienteDTO.cpf());
        pacienteExistente.setEmail(pacienteDTO.email());

        return pacienteRepository.save(pacienteExistente);
    }
}
