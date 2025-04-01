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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (!isValidEmail(pacienteDTO.email())) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (!isValidCPF(pacienteDTO.cpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        var novoPaciente = new Paciente();
        novoPaciente.setNome(pacienteDTO.nome());
        novoPaciente.setCpf(pacienteDTO.cpf());
        novoPaciente.setEmail(pacienteDTO.email());
        novoPaciente.setEndereco(pacienteDTO.endereco());
        novoPaciente.setAtivo(true);

        pacienteRepository.save(novoPaciente);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public boolean isValidCPF(String cpf) {
        // Remover caracteres não numéricos (pontos, traços)
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verificar se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se o CPF é composto por números repetidos
        if (cpf.equals(cpf.substring(0, 1) + cpf.substring(0, 1) + cpf.substring(0, 1) + cpf.substring(0, 1))) {
            return false;
        }

        // Calcular o primeiro dígito verificador
        int soma1 = 0;
        for (int i = 0; i < 9; i++) {
            soma1 += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digito1 = (soma1 * 10) % 11;
        if (digito1 == 10) {
            digito1 = 0;
        }

        // Calcular o segundo dígito verificador
        int soma2 = 0;
        for (int i = 0; i < 10; i++) {
            soma2 += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digito2 = (soma2 * 10) % 11;
        if (digito2 == 10) {
            digito2 = 0;
        }

        // Verificar se os dois dígitos verificadores calculados são iguais aos fornecidos
        return cpf.charAt(9) == (digito1 + '0') && cpf.charAt(10) == (digito2 + '0');
    }
        public void removerPaciente (Long id){

            if (!pacienteRepository.existsById(id)) {
                throw new IllegalArgumentException("Paciente não encontrado.");
            }
            try {
                pacienteRepository.deleteById(id);
                log.info("Paciente com id {} removido com sucesso", id);
            } catch (Exception e) {
                log.error("Erro ao remover paciente com id {}: {}", id, e.getMessage());
                throw new RuntimeException("Erro ao remover paciente", e);
            }

        }

        public Paciente alteraPaciente (PacienteDTO pacienteDTO){

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
