package com.eversis.esa.geoss.userdata.application.configuration.oauth2;

import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * The type Claim accessor granted authorities converter.
 */
@Setter
public class ClaimAccessorGrantedAuthoritiesConverter implements
        Converter<ClaimAccessor, Collection<GrantedAuthority>> {

    private String authoritiesClaimName;

    private String authoritiesClaimAttributeName;

    private SimpleAuthorityMapper simpleAuthorityMapper;

    @Override
    public Collection<GrantedAuthority> convert(ClaimAccessor claimAccessor) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(claimAccessor)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        if (simpleAuthorityMapper != null) {
            return simpleAuthorityMapper.mapAuthorities(grantedAuthorities);
        }
        return grantedAuthorities;
    }

    private Collection<String> getAuthorities(ClaimAccessor claimAccessor) {
        Object authorities = claimAccessor.getClaim(authoritiesClaimName);
        if (authorities instanceof String) {
            if (StringUtils.hasText((String) authorities)) {
                return Arrays.asList(((String) authorities).split(" "));
            }
            return Collections.emptyList();
        }
        if (authorities instanceof Collection) {
            return castAuthoritiesToCollection(authorities);
        }
        if (authorities instanceof Map<?, ?> mapAuthorities) {
            return castAuthoritiesToCollection(mapAuthorities.get(authoritiesClaimAttributeName));
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> castAuthoritiesToCollection(Object authorities) {
        return (Collection<String>) authorities;
    }
}
