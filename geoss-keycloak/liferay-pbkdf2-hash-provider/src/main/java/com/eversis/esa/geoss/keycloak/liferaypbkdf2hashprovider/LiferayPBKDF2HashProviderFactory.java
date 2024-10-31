package com.eversis.esa.geoss.keycloak.liferaypbkdf2hashprovider;

import org.keycloak.Config;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * Provider factory for PBKDF2 Liferay password hash algorithm.
 */
public class LiferayPBKDF2HashProviderFactory implements PasswordHashProviderFactory {

    /**
     * The constant ID.
     */
    public static final String ID = "liferay-pbkdf2";

    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return new LiferayPBKDF2HashProvider(ID);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void init(Config.Scope config) {
        // Not needed.
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // Not needed.
    }

    @Override
    public void close() {
        // Not needed.
    }
}
