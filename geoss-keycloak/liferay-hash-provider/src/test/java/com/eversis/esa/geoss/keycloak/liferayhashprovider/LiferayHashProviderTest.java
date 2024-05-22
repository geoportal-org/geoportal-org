package com.eversis.esa.geoss.keycloak.liferayhashprovider;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.keycloak.common.util.Base64;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Liferay hash provider test.
 */
class LiferayHashProviderTest {

    /**
     * Test.
     */
    @Test
    void test() {
        LiferayHashProvider hasher = new LiferayHashProvider(LiferayHashProviderFactory.ID);

        // "qaz123" password taken from database
        String liferayPassword = "XttentAd47a/XZbzhlBnNBLgvvE=";
        PasswordCredentialModel credentialModel = parsePasswordFromLiferay(liferayPassword);

        assertTrue(hasher.verify("qaz123", credentialModel));
    }

    @SneakyThrows
    private PasswordCredentialModel parsePasswordFromLiferay(String raw) {
        // Sha1 has an output length of 20 bytes. SSHA (salted sha) has an additional 8 byte suffix with salt.
        byte[] decodedPass = Base64.decode(raw);
        byte[] pass = Arrays.copyOfRange(decodedPass, 0, Math.max(decodedPass.length - 8, 20));
        byte[] salt = Arrays.copyOfRange(decodedPass, Math.max(decodedPass.length - 8, 20), decodedPass.length);
        return PasswordCredentialModel.createFromValues(null, salt, 0, Base64.encodeBytes(pass));
    }
}
