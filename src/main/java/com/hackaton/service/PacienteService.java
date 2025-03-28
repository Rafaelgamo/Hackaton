package com.hackaton.service;

import com.hackaton.dto.PacienteDTO;
import com.hackaton.entity.Paciente;
import com.hackaton.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {


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
            throw new RuntimeException("CPF já cadastrado");
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
        pacienteRepository.deleteById(id);
    }
}
