package com.eversis.esa.geoss.personaldata.feedback.event;

import com.eversis.esa.geoss.common.domain.NonTransactionalSimpleMailMessage;
import com.eversis.esa.geoss.personaldata.feedback.domain.Feedback;
import com.eversis.esa.geoss.personaldata.feedback.properties.FeedbackProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * The type Feedback repository event handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
@RepositoryEventHandler
public class FeedbackRepositoryEventHandler {

    private final FeedbackProperties feedbackProperties;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * After.
     *
     * @param feedback the feedback
     */
    @HandleAfterCreate
    void after(Feedback feedback) {
        log.debug("feedback:{}", feedback);
        SimpleMailMessage simpleMailMessage = new NonTransactionalSimpleMailMessage();
        simpleMailMessage.setFrom(address(feedback.getFromName(), feedback.getFromMail()));
        simpleMailMessage.setTo(
                address(feedbackProperties.getReceiver().getPersonal(), feedbackProperties.getReceiver().getAddress()));
        simpleMailMessage.setSubject(feedback.getSubject());
        simpleMailMessage.setText(feedback.getBody());
        applicationEventPublisher.publishEvent(simpleMailMessage);
    }

    private String address(String name, String mail) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" ");
        sb.append("<");
        sb.append(mail);
        sb.append(">");
        return sb.toString();
    }
}
