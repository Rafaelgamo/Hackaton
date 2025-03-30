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
import com.hackaton.exception.ConflitoException;
import com.hackaton.exception.NaoEncontradoException;
import com.hackaton.exception.ValidacaoException;
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

    // TODO: MVP - Esse método permite que agendamentos com data maior do que a atual seja concluído para facilitar os testes desta POC
    public AgendamentoConcluidoDTO concluirAgendamento(Long agendamentoId, ConcluirAgendamentoDTO concluirAgendamentoDTO) {
        var optById = agendamentoRepository.findById(agendamentoId);
        if (optById.isEmpty()) {
            throw new NaoEncontradoException("Agendamento não existente");
        }

        var agendamentoConcluido = optById.get();
        agendamentoConcluido.setStatus(StatusConfirmacao.CONCLUIDO);

        NovoAgendamentoDTO retorno = null;
        if (concluirAgendamentoDTO.agendarRetorno()) {
            retorno = agendarRetorno(agendamentoConcluido, DIAS_PARA_RETORNO);
        }

        agendamentoRepository.save(agendamentoConcluido);

        return new AgendamentoConcluidoDTO(
            new AgendamentoDTO(agendamentoConcluido),
            retorno == null ? null : new AgendamentoDTO(retorno)
        );
    }

    public AgendamentoDTO atualizarStatusAgendamento(Long idAgendamento, StatusConfirmacao status) {
        var optById = agendamentoRepository.findById(idAgendamento);
        if (optById.isEmpty()) {
            throw new NaoEncontradoException("Agendamento não existente");
        }

        var agendamento = optById.get();
        agendamento.setStatus(status);

        agendamentoRepository.save(agendamento);

        return new AgendamentoDTO(agendamento);
    }

    public NovoAgendamentoDTO agendarRetorno(Long agendamentoId, LocalDate novaData) {
        var optById = agendamentoRepository.findById(agendamentoId);
        if (optById.isEmpty()) {
            throw new NaoEncontradoException("Agendamento não existente");
        }

        if (!ehDataValida(novaData)) {
            throw new ValidacaoException("Data inválida, verifique os horarios disponíveis para este dia");
        }

        var agendamento = optById.get();
        var medicoId = agendamento.getMedico().getId();
        var pacienteId = agendamento.getPaciente().getId();

        var novaHora = agendamento.getHora();
        return agendarRetorno(medicoId, pacienteId, novaData, novaHora);
    }

    public NovoAgendamentoDTO agendarRetorno(Agendamento agendamentoConcluido, Long diasParaRetorno) {
        var medicoId = agendamentoConcluido.getMedico().getId();
        var pacienteId = agendamentoConcluido.getPaciente().getId();

        var novaData = LocalDate.now().plusDays(diasParaRetorno);
        var novaHora = agendamentoConcluido.getHora();
        return agendarRetorno(medicoId, pacienteId, novaData, novaHora);
    }

    public NovoAgendamentoDTO realizarAgendamento(NovoAgendamentoDTO agendamentoDTO) {
        var medicoId = agendamentoDTO.idMedico();
        var pacienteId = agendamentoDTO.idPaciente();
        var novoAgendamento = new Agendamento();

        var data = agendamentoDTO.data();
        var hora = agendamentoDTO.hora();
        if (!ehDataHoraValida(data, hora)) {
            throw new ValidacaoException("Data ou hora inválidos, verifique os horarios disponíveis para este dia");
        }

        var agendamentoConflitante = agendamentoRepository.buscarAgendamento(medicoId, data, hora);
        if (agendamentoConflitante != null) {
            throw new ValidacaoException("Data ou hora inválidos, verifique os horarios disponíveis para este dia");
        }

        var medico = medicoService.buscaPorId(medicoId);
        if (medico == null) {
            throw new NaoEncontradoException("Medico inexistente");
        }

        var paciente = pacienteService.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new NaoEncontradoException("Paciente inexistente");
        }

        novoAgendamento.setMedico(medico);
        novoAgendamento.setPaciente(new Paciente(pacienteId));
        novoAgendamento.setStatus(StatusConfirmacao.AGENDADO);
        novoAgendamento.setData(data);
        novoAgendamento.setHora(hora);

        var agendamentoSalvo = agendamentoRepository.save(novoAgendamento);

        return new NovoAgendamentoDTO(agendamentoSalvo.getId(), agendamentoDTO, agendamentoSalvo.getStatus());
    }

    private NovoAgendamentoDTO agendarRetorno(Long medicoId, Long pacienteId, LocalDate novaData, LocalTime novaHora) {
        var horariosDisponiveisNaData = listarHorariosDisponiveisPorDiaEMedico(novaData, medicoId);
        if (horariosDisponiveisNaData.isEmpty()) {
            throw new ConflitoException("Médico indisponível na data do retorno - " + novaData);
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

        return realizarAgendamento(retornoNovoAgendamentoDTO);
    }

    public List<Agendamento> listarAgendamentosProximos(Integer rangeDias) {
        if (rangeDias <= 0) {
            throw new ValidacaoException("O rangeDias deve ser um numero inteiro positivo");
        }

        var hoje = LocalDate.now();
        var dataLimite = hoje.plusDays(rangeDias);
        return agendamentoRepository.listarAgendadosPorDataEntre(hoje, dataLimite);
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

    public void contatarPacientesParaConfirmacao(List<Agendamento> agendamentosProximos) {
        for (var agendamento : agendamentosProximos) {
            var paciente = agendamento.getPaciente();

            var emailPaciente = paciente.getEmail();
            if (emailPaciente == null) {
                agendamento.setStatus(StatusConfirmacao.SEM_CONTATO_ELETRONICO);
                continue;
            }

            var medico = agendamento.getMedico();

            agendamento.setStatus(StatusConfirmacao.AGUARDANDO_CONFIRMACAO);

            enviarEmailConfirmacao(
                agendamento.getId(),
                paciente.getNome(),
                emailPaciente,
                agendamento.getData(),
                agendamento.getHora(),
                medico.getEspecialidade().getNome()
            );
        }

        agendamentoRepository.saveAll(agendamentosProximos);
    }

    public void enviarEmailConfirmacao(Long agendamentoId, String nomePaciente, String emailPaciente, LocalDate data, LocalTime hora, String especialidade) {
        var template = EmailTemplates.TEMPLATE_CONFIRMACAO;

        var properties = new HashMap<String, Object>();
        properties.put("nomePaciente", nomePaciente);
        properties.put("dataAgendamento", data);
        properties.put("horaAgendamento", hora);
        properties.put("especialidade", especialidade);

        properties.put("urlConfirmar", baseUrl + "/agendamento/confirmar/" + agendamentoId);
        properties.put("urlCancelar", baseUrl + "/agendamento/cancelar/" + agendamentoId);

        emailService.sendEmail(
            template.getTemplate(),
            emailPaciente,
            template.getTitulo(),
            properties
        );
    }

    private boolean ehDataHoraValida(LocalDate data, LocalTime hora) {
        if (!ehDataValida(data)) {
            return false;
        }

        if (!ehHoraValida(hora)) {
            return false;
        }

        if (data.equals(LocalDate.now())) {
            if (hora.isBefore(LocalTime.now())) {
                return false;
            }
        }

        return true;
    }

    private boolean ehHoraValida(LocalTime hora) {
        return HORARIOS_DISPONIVEIS_POR_PADRAO.contains(hora);
    }

    private boolean ehDataValida(LocalDate data) {
        return LocalDate.now().isBefore(data);
    }
}
