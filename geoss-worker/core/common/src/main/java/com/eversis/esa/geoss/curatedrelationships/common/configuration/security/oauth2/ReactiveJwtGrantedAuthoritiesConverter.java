package com.eversis.esa.geoss.curatedrelationships.common.configuration.security.oauth2;

import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

/**
 * The type Reactive jwt granted authorities converter.
 */
@Setter
public class ReactiveJwtGrantedAuthoritiesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    private ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter;

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        return Flux.fromIterable(claimAccessorGrantedAuthoritiesConverter.convert(jwt));
    }
}
