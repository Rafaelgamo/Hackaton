package com.hackaton.jobs;

import com.hackaton.service.AgendamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmacaoJob {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfirmacaoJob.class);
    private final AgendamentoService agendamentoService;

    public EmailConfirmacaoJob(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    // Job rodando todos os dias às 10:00
    @Scheduled(cron = "0 0 10 * * *", zone = "America/Recife")
    public void enviarEmailConfirmacao() {
        logger.info("JOB - Envio de email de confirmação - Iniciado");
//        var agendamentosAplicaveis = agendamentoService.listarAgendamentosPorProximidadeDeData(5, TimeUnit.DAYS);
//        agendamentoService.enviarEmailConfirmação(null);

        logger.info("JOB - Envio de email de confirmação - Encerrado");
    }

}
