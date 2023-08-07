package com.eversis.esa.geoss.curatedrelationships.search.configuration.oauth2;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The type Oidc granted authorities mapper.
 */
@Log4j2
@Setter
public class OidcGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {

    private OidcUserAuthorityGrantedAuthoritiesConverter oidcUserAuthorityGrantedAuthoritiesConverter;

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof OidcUserAuthority) {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                Collection<GrantedAuthority> grantedAuthorities2 = oidcUserAuthorityGrantedAuthoritiesConverter.convert(
                        oidcUserAuthority);
                grantedAuthorities.addAll(grantedAuthorities2);
            }
            grantedAuthorities.add(authority);
        }
        return grantedAuthorities;
    }
}
