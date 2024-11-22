package com.eversis.esa.geoss.personaldata.feedback.properties;

import com.eversis.esa.geoss.common.properties.EmailProperties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * The type Feedback properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.personaldata.feedback")
public class FeedbackProperties {

    @NestedConfigurationProperty
    private final EmailProperties.Receiver receiver = new EmailProperties.Receiver();

    /**
     * The type Receiver.
     */
    @Getter
    @Setter
    public static class Receiver {

        private String address;

        private String personal;
    }
}
