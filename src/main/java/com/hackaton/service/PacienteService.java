package com.hackaton.service;

import com.hackaton.dto.PacienteDTO;
import com.hackaton.entity.Paciente;
import com.hackaton.repository.PacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {


    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente buscarPacientePorId(Long pacienteId) {
        var paciente = pacienteRepository.findById(pacienteId).orElse(null);
        return paciente;
    }

    public PacienteDTO buscarPacienteDTOPorId(Long pacienteId) {
        var paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) return null;

        return new PacienteDTO(paciente);
    }

}
