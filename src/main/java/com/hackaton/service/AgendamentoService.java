package com.hackaton.service;

import com.hackaton.dto.AgendamentoDTO;
import com.hackaton.dto.AgendamentoDisponivelDTO;
import com.hackaton.entity.Agendamento;
import com.hackaton.entity.Medico;
import com.hackaton.entity.Paciente;
import com.hackaton.entity.StatusConfirmacao;
import com.hackaton.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
  
    private final EmailService emailService;
    private final PacienteService pacienteService;

    @Value("${application.baseurl}")
    private String baseUrl;

    private static final Set<LocalTime> horariosDisponiveisPorPadrao = new LinkedHashSet<>(List.of(
                LocalTime.parse("09:00"),
                LocalTime.parse("09:30"),
                LocalTime.parse("10:00"),
                LocalTime.parse("10:30"),
                LocalTime.parse("11:00"),
                LocalTime.parse("11:30"),
                // Almoço 12-13h
                LocalTime.parse("13:00"),
                LocalTime.parse("13:30"),
                LocalTime.parse("14:00"),
                LocalTime.parse("14:30"),
                LocalTime.parse("15:00"),
                LocalTime.parse("15:30"),
                LocalTime.parse("16:00"),
                LocalTime.parse("16:30"),
                LocalTime.parse("17:00"),
                LocalTime.parse("17:30")
            ));

    public AgendamentoService(AgendamentoRepository agendamentoRepository, EmailService emailService) {
        this.agendamentoRepository = agendamentoRepository;
        this.emailService = emailService;
    }

    public void enviarEmailConfirmação(AgendamentoDTO agendamentoDTO) {
        var template = EmailTemplates.TEMPLATE_CONFIRMACAO;

        var properties = new HashMap<String, Object>();
        properties.put("nomePaciente", "Eu mesmo");
        properties.put("dataAgendamento", "25/03/2025");
        properties.put("horaAgendamento", "14:30");
        properties.put("especialidade", "Clinico Geral");

        properties.put("urlConfirmar", baseUrl + "/confirmar/" + 5);
        properties.put("urlCancelar", baseUrl + "/cancelar/" + 5);

        emailService.sendEmail(
                template.getTemplate(),
                "caioalves_diogo@hotmail.com",
                template.getTitulo(),
                properties
        );
    }

    public List<Long> listarPassiveisDeEnvioDeEmailDeConfirmacao() {
        return null;
    }

    public AgendamentoDTO realizarAgendamento(AgendamentoDTO agendamentoDTO) {
        var novoAgendamento = new Agendamento();
        novoAgendamento.setMedico(new Medico(agendamentoDTO.idMedico()));
        novoAgendamento.setPaciente(new Paciente(agendamentoDTO.idPaciente()));

        novoAgendamento.setStatus();

        agendamentoRepository.save(novoAgendamento);

        return agendamentoDTO;
    }

    public void atualizarStatusAgendamento(Long medicoId, LocalDate data, LocalTime hora, StatusConfirmacao novoStatus) {
        Agendamento agendamento = agendamentoRepository.buscarAgendamento(medicoId, data, hora);
        if (agendamento == null) {
            // TODO Exception
            //throw new AgendamentoNaoEncontradoException
            return;
        }

        agendamento.setStatus(novoStatus);
    }

    public List<AgendamentoDisponivelDTO> listarHorariosDisponiveisPorDiaEMedico(LocalDate data, Long medicoId) {
        var horariosDisponiveis = new ArrayList<AgendamentoDisponivelDTO>();

        if (data.isBefore(LocalDate.now())) {
            throw new RuntimeException("Data inválida, insira a data de hoje ou uma futura");
        }

        var agendamentosOcupados = agendamentoRepository.listarMarcadosPorDataEMedico(data, medicoId);
        var horariosOcupados = agendamentosOcupados.stream().map(Agendamento::getHora).collect(Collectors.toSet());

        for (var horarioDisponivelPorPadrao : horariosDisponiveisPorPadrao) {
            if (!horariosOcupados.contains(horarioDisponivelPorPadrao)) {
                var horarioDisponivelDTO = new AgendamentoDisponivelDTO(
                        medicoId,
                        StatusConfirmacao.DISPONIVEL.getName(),
                        data,
                        horarioDisponivelPorPadrao
                );

                horariosDisponiveis.add(horarioDisponivelDTO);
            }
        }

        return horariosDisponiveis;
    }

}
