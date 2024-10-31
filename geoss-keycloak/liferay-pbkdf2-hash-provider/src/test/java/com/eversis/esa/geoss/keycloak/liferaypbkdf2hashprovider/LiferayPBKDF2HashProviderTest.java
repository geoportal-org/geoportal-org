package com.eversis.esa.geoss.keycloak.liferaypbkdf2hashprovider;

import com.eversis.esa.geoss.keycloak.liferaypbkdf2hashprovider.LiferayPBKDF2HashProvider;
import com.eversis.esa.geoss.keycloak.liferaypbkdf2hashprovider.LiferayPBKDF2HashProviderFactory;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.keycloak.common.util.Base64;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Liferay PBKDF2 hash provider test.
 */
class LiferayPBKDF2HashProviderTest {

    /**
     * Test.
     */
    @Test
    void test() {
        LiferayPBKDF2HashProvider hasher = new LiferayPBKDF2HashProvider(LiferayPBKDF2HashProviderFactory.ID);

        String plainPassword = "QAZ123wsx";
        String liferayPassword = "AAAAoAAB9AD5GhsbxnOYHSAvA/RzOTOjTL8pD+1QNwHmEDqe";
        PasswordCredentialModel credentialModel = parsePasswordFromLiferay(liferayPassword);

        assertTrue(hasher.verify(plainPassword, credentialModel));
    }

    @SneakyThrows
    private PasswordCredentialModel parsePasswordFromLiferay(String raw) {
        byte[] decodedPass = Base64.decode(raw);
        byte[] keySize = Arrays.copyOfRange(decodedPass, 0, 4);
        byte[] rounds = Arrays.copyOfRange(decodedPass, 4, 8);
        byte[] salt = Arrays.copyOfRange(decodedPass, 8, 16);
        byte[] pass = Arrays.copyOfRange(decodedPass, 16, decodedPass.length);
        return PasswordCredentialModel.createFromValues(null, salt, ByteBuffer.wrap(rounds).getInt(),
                Base64.encodeBytes(pass));
    }
}
