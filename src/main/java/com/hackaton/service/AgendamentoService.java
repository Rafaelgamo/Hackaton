package com.hackaton.service;

import com.hackaton.dto.AgendamentoConcluidoDTO;
import com.hackaton.dto.AgendamentoDTO;
import com.hackaton.dto.AgendamentoDisponivelDTO;
import com.hackaton.dto.ConcluirAgendamentoDTO;
import com.hackaton.dto.NovoAgendamentoDTO;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    private final EmailService emailService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    @Value("${application.baseurl}")
    private String baseUrl;

    private static final long DIAS_PARA_RETORNO = 30L;

    private static final Set<LocalTime> HORARIOS_DISPONIVEIS_POR_PADRAO = new LinkedHashSet<>(List.of(
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

    public AgendamentoService(AgendamentoRepository agendamentoRepository, EmailService emailService, PacienteService pacienteService, MedicoService medicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.emailService = emailService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    public List<Long> listarPassiveisDeEnvioDeEmailDeConfirmacao() {
        return null;
    }

    public AgendamentoConcluidoDTO concluirAgendamento(Long agendamentoId, ConcluirAgendamentoDTO concluirAgendamentoDTO) {
        var optById = agendamentoRepository.findById(agendamentoId);
        if (optById.isEmpty()) {
            throw new RuntimeException("Agendamento não existente");
        }

        var agendamentoConcluido = optById.get();
        agendamentoConcluido.setStatus(StatusConfirmacao.CONCLUIDO);

        NovoAgendamentoDTO retorno = null;
        if (concluirAgendamentoDTO.agendarRetorno()) {
            var medicoId = agendamentoConcluido.getMedico().getId();
            var pacienteId = agendamentoConcluido.getPaciente().getId();

            var novaData = LocalDate.now().plusDays(DIAS_PARA_RETORNO);
            var novaHora = agendamentoConcluido.getHora();

            var horariosDisponiveisNaData = listarHorariosDisponiveisPorDiaEMedico(novaData, medicoId);
            if (horariosDisponiveisNaData.isEmpty()) {
                throw new RuntimeException("Médico indisponível na data do retorno - " + novaData);
            }

            boolean horaDaConsultaLivre = false;
            for (AgendamentoDisponivelDTO agendamentoDisponivelDTO : horariosDisponiveisNaData) {
                if (agendamentoDisponivelDTO.hora().equals(novaHora)) {
                    horaDaConsultaLivre = true;
                    break;
                }
            }

            if (!horaDaConsultaLivre) {
                novaHora = horariosDisponiveisNaData.get(0).hora();
            }

            var retornoNovoAgendamentoDTO = new NovoAgendamentoDTO(
                    null,
                    medicoId,
                    pacienteId,
                    StatusConfirmacao.AGENDADO.getName(),
                    novaData,
                    novaHora
            );

            retorno = realizarAgendamento(retornoNovoAgendamentoDTO);
        }

        return new AgendamentoConcluidoDTO(
            new AgendamentoDTO(agendamentoConcluido),
            retorno == null ? null : new AgendamentoDTO(retorno)
        );
    }

    public NovoAgendamentoDTO realizarAgendamento(NovoAgendamentoDTO agendamentoDTO) {
        var medicoId = agendamentoDTO.idMedico();
        var pacienteId = agendamentoDTO.idPaciente();
        var novoAgendamento = new Agendamento();

        var data = agendamentoDTO.data();
        if (data.isBefore(LocalDate.now())) {
            throw new RuntimeException("Data inválida, insira a data de hoje ou uma futura");
        }

        var hora = agendamentoDTO.hora();
        if (!HORARIOS_DISPONIVEIS_POR_PADRAO.contains(hora)) {
            throw new RuntimeException("'hora' inválida, cheque a lista de horarios disponiveis");
        }

        if (data.equals(LocalDate.now())) {
            if (hora.isBefore(LocalTime.now())) {
                throw new RuntimeException("Data inválida, insira data e hora futuras");
            }
        }

        var agendamentoConflitante = agendamentoRepository.buscarAgendamento(medicoId, data, hora);
        if (agendamentoConflitante != null) {
            throw new RuntimeException("Horário está indisponível para este médico");
        }

        var medico = medicoService.buscaPorId(medicoId);
        if (medico == null) {
            throw new RuntimeException("Medico inexistente");
        }

        var paciente = pacienteService.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new RuntimeException("Paciente inexistente");
        }

        novoAgendamento.setMedico(medico);
        novoAgendamento.setPaciente(new Paciente(pacienteId));
        novoAgendamento.setStatus(StatusConfirmacao.AGENDADO);
        novoAgendamento.setData(data);
        novoAgendamento.setHora(hora);

        var agendamentoSalvo = agendamentoRepository.save(novoAgendamento);

        return new NovoAgendamentoDTO(agendamentoSalvo.getId(), agendamentoDTO, agendamentoSalvo.getStatus());
    }

    public void atualizarStatusAgendamento(Long medicoId, Long pacienteId, LocalDate data, LocalTime hora, StatusConfirmacao novoStatus) {
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

        var hoje = LocalDate.now();
        var agora = LocalTime.now();
        for (var horarioDisponivelPorPadrao : HORARIOS_DISPONIVEIS_POR_PADRAO) {
            // Filtrando horários passados
            if (hoje.equals(data) && horarioDisponivelPorPadrao.isBefore(agora)) {
                continue;
            }

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

    public List<AgendamentoDTO> listarPorPaciente(Long idPaciente) {
        var paciente = new Paciente(idPaciente);
        return agendamentoRepository.findAllByPaciente(paciente).stream().map(AgendamentoDTO::new).toList();
    }

    public List<AgendamentoDTO> listarPorMedico(Long idMedico) {
        var medico = new Medico(idMedico);
        return agendamentoRepository.findAllByMedico(medico).stream().map(AgendamentoDTO::new).toList();
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

}
