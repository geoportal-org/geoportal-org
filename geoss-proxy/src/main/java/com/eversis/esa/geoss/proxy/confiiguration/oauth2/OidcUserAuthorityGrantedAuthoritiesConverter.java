package com.eversis.esa.geoss.proxy.confiiguration.oauth2;

import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.Collection;

/**
 * The type Oidc user authority granted authorities converter.
 */
@Setter
public class OidcUserAuthorityGrantedAuthoritiesConverter implements
        Converter<OidcUserAuthority, Collection<GrantedAuthority>> {

    private ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter;

    @Override
    public Collection<GrantedAuthority> convert(OidcUserAuthority oidcUserAuthority) {
        OidcIdToken idToken = oidcUserAuthority.getIdToken();
        Collection<GrantedAuthority> convert = claimAccessorGrantedAuthoritiesConverter.convert(idToken);
        OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
        if (userInfo != null) {
            Collection<GrantedAuthority> convert1 = claimAccessorGrantedAuthoritiesConverter.convert(userInfo);
            convert.addAll(convert1);
        }
        return convert;
    }
}
