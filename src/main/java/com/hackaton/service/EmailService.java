package com.hackaton.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String senhaApp;


    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public EmailService(SpringTemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    public void sendEmail(String template, String toEmail, String title, Map<String, Object> properties) {
        try {
            var message = mailSender.createMimeMessage();
            var mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // "SUS - Confirmação de Agendamento"
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(toEmail);

            var htmlContent = getContentFromTemplate(template, properties);
            mimeMessageHelper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getContentFromTemplate(String templateName, Map<String, Object> properties) {
        var context = new Context(LocaleContextHolder.getLocale());
        context.setVariables(properties);
        return templateEngine.process(templateName, context);
    }
}
