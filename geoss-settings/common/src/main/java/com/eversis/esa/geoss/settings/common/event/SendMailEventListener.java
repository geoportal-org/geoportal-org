package com.eversis.esa.geoss.settings.common.event;

import com.eversis.esa.geoss.settings.common.configuration.EmailProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * The type Send mail event listener.
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class SendMailEventListener {

    private final Optional<JavaMailSender> javaMailSender;

    private final EmailProperties emailProperties;

    /**
     * Simple mail message event listener.
     *
     * @param simpleMailMessage the simple mail message
     */
    @EventListener
    public void simpleMailMessageEventListener(SimpleMailMessage simpleMailMessage) {
        handleEvent(simpleMailMessage);
    }

    /**
     * Simple mail message transactional event listener.
     *
     * @param simpleMailMessage the simple mail message
     */
    @TransactionalEventListener
    public void simpleMailMessageTransactionalEventListener(SimpleMailMessage simpleMailMessage) {
        handleEvent(simpleMailMessage);
    }

    private void handleEvent(SimpleMailMessage simpleMailMessage) {
        javaMailSender.ifPresentOrElse(sender -> {
            try {
                log.debug("Sending message:{}", simpleMailMessage);
                setDefaultFrom(simpleMailMessage);
                setDefaultTo(simpleMailMessage);
                MimeMessage mimeMessage = sender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                copyTo(simpleMailMessage, mimeMessageHelper);
                sender.send(mimeMessage);
            } catch (Exception e) {
                log.error("Error sending message:" + simpleMailMessage + " Reason:" + e.getMessage(), e);
            }
        }, () -> log.warn("No sending message:{}", simpleMailMessage));
    }

    private void setDefaultFrom(SimpleMailMessage simpleMailMessage) throws UnsupportedEncodingException {
        String from = simpleMailMessage.getFrom();
        if (from == null || from.isEmpty()) {
            InternetAddress internetAddress = new InternetAddress(emailProperties.getSender().getAddress(),
                    emailProperties.getSender().getPersonal(), emailProperties.getDefaultEncoding().name());
            simpleMailMessage.setFrom(internetAddress.toString());
        }
    }

    private void setDefaultTo(SimpleMailMessage simpleMailMessage) throws UnsupportedEncodingException {
        String to = simpleMailMessage.getReplyTo();
        if (to == null || to.isEmpty()) {
            InternetAddress internetAddress = new InternetAddress(emailProperties.getReceiver().getAddress(),
                    emailProperties.getReceiver().getPersonal(), emailProperties.getDefaultEncoding().name());
            simpleMailMessage.setFrom(internetAddress.toString());
        }
    }

    private void copyTo(SimpleMailMessage source, MimeMessageHelper target) throws MessagingException {
        if (source.getFrom() != null) {
            target.setFrom(source.getFrom());
        }
        if (source.getReplyTo() != null) {
            target.setReplyTo(source.getReplyTo());
        }
        if (source.getTo() != null) {
            target.setTo(copy(source.getTo()));
        }
        if (source.getCc() != null) {
            target.setCc(copy(source.getCc()));
        }
        if (source.getBcc() != null) {
            target.setBcc(copy(source.getBcc()));
        }
        if (source.getSentDate() != null) {
            target.setSentDate(source.getSentDate());
        }
        if (source.getSubject() != null) {
            target.setSubject(source.getSubject());
        }
        if (source.getText() != null) {
            target.setText(source.getText(), true);
        }
    }

    private static String[] copy(String[] state) {
        return state.clone();
    }
}
