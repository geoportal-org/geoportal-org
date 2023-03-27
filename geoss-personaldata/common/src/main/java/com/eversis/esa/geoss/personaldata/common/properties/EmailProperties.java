package com.eversis.esa.geoss.personaldata.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The type Email properties.
 */
@Getter
@Setter
@ConfigurationProperties("common.email")
public class EmailProperties {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Charset defaultEncoding = DEFAULT_CHARSET;

    @NestedConfigurationProperty
    private final Sender sender = new Sender();

    @NestedConfigurationProperty
    private final Receiver receiver = new Receiver();

    /**
     * The type Reset password.
     */
    @Getter
    @Setter
    public static class Sender {

        private String address;

        private String personal;
    }

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
