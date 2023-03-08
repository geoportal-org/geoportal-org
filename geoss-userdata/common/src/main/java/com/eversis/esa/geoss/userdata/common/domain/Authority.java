package com.eversis.esa.geoss.userdata.common.domain;

/**
 * The type Authority.
 */
public record Authority(Role role) {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public String toString() {
        return ROLE_PREFIX + role.name();
    }
}
