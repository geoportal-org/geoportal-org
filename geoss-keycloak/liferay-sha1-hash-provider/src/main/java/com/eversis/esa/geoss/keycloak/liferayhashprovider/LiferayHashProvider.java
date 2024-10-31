package com.eversis.esa.geoss.keycloak.liferayhashprovider;

import lombok.AllArgsConstructor;
import org.keycloak.common.util.Base64;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Implementation SHA1 password hash algorithm based on Liferay's (6.1.2) implementation.
 */
@AllArgsConstructor
public class LiferayHashProvider implements PasswordHashProvider {

    private final String providerId;

    @Override
    public boolean policyCheck(PasswordPolicy policy, PasswordCredentialModel credential) {
        return providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        byte[] salt = getSalt();
        String encodedPassword = encodedCredential(rawPassword, salt);

        return PasswordCredentialModel.createFromValues(providerId, salt, iterations, encodedPassword);
    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel credential) {
        byte[] salt = Objects.requireNonNullElseGet(credential.getPasswordSecretData().getSalt(), () -> new byte[0]);
        return encodedCredential(rawPassword, salt).equals(credential.getPasswordSecretData().getValue());
    }

    private String encodedCredential(String rawPassword, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] plainTextPasswordBytes = rawPassword.getBytes(UTF_8);
            byte[] data = Arrays.copyOf(plainTextPasswordBytes, plainTextPasswordBytes.length + salt.length);
            System.arraycopy(salt, 0, data, plainTextPasswordBytes.length, salt.length);
            return Base64.encodeBytes(messageDigest.digest(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getSalt() {
        byte[] buffer = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(buffer);
        return buffer;
    }

    @Override
    public void close() {
        // Not needed.
    }
}
