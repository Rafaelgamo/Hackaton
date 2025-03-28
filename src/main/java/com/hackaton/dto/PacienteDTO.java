package com.hackaton.dto;

import com.hackaton.entity.Paciente;

public record PacienteDTO(
        String nome,
        String cpf,
        String endereco,
        String email,
        Boolean ativo
) {
    public PacienteDTO(Paciente paciente) {
        this(
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getEndereco(),
                paciente.getEmail(),
                paciente.getAtivo()
        );
    }
}
