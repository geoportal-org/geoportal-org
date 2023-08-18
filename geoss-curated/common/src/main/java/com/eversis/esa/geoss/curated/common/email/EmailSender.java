package com.eversis.esa.geoss.curated.common.email;

import java.util.Locale;
import java.util.Map;

import com.eversis.esa.geoss.common.domain.NonTransactionalSimpleMailMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * The type Email sender.
 */
@Log4j2
@Component
public class EmailSender {

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Instantiates a new Email sender.
     *
     * @param messageSource the message source
     * @param templateEngine the template engine
     * @param applicationEventPublisher the application event publisher
     */
    public EmailSender(MessageSource messageSource, SpringTemplateEngine templateEngine,
            ApplicationEventPublisher applicationEventPublisher) {
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Send.
     *
     * @param email the email
     * @param locale the locale
     * @param titleCode the title code
     * @param bodyTemplate the body template
     * @param bodyVariables the body variables
     */
    public void send(String email, Locale locale, String titleCode, String bodyTemplate,
            Map<String, Object> bodyVariables) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String subject = subject(locale, titleCode);
        String body = body(locale, bodyTemplate, bodyVariables);
        sendMail(email, subject, body);
    }

    private String subject(Locale locale, String code) {
        return messageSource.getMessage(code, null, locale);
    }

    private String body(Locale locale, String templateName, Map<String, Object> variables) {
        final Context context = new Context();
        context.setLocale(locale);
        context.setVariables(variables);
        return this.templateEngine.process(templateName, context);
    }

    private void sendMail(String to, String subject, String body) {
        NonTransactionalSimpleMailMessage msg = new NonTransactionalSimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        applicationEventPublisher.publishEvent(msg);
    }

}
