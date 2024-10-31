package com.eversis.esa.geoss.keycloak.liferaypbkdf2hashprovider;

import lombok.AllArgsConstructor;
import org.keycloak.common.util.Base64;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Implementation PBKDF2 password hash algorithm based on Liferay's (6.2.5) implementation.
 */
@AllArgsConstructor
public class LiferayPBKDF2HashProvider implements PasswordHashProvider {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    private static final int ITERATION_COUNT = 128000;

    private static final int KEY_LENGTH = 160;

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
        byte[] salt = credential.getPasswordSecretData().getSalt();
        return encodedCredential(rawPassword, salt).equals(credential.getPasswordSecretData().getValue());
    }

    private String encodedCredential(String rawPassword, byte[] salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(rawPassword.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
            byte[] secretKeyBytes = secretKey.getEncoded();
            return Base64.encodeBytes(secretKeyBytes);
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
