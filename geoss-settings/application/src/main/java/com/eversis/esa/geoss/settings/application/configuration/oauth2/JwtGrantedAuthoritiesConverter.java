package com.eversis.esa.geoss.settings.application.configuration.oauth2;

import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

/**
 * The type Jwt granted authorities converter.
 */
@Setter
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return claimAccessorGrantedAuthoritiesConverter.convert(jwt);
    }
}
