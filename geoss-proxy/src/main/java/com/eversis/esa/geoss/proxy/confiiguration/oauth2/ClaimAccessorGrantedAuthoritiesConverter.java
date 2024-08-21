package com.eversis.esa.geoss.proxy.confiiguration.oauth2;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Setter
public class ClaimAccessorGrantedAuthoritiesConverter implements
        Converter<ClaimAccessor, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";

    private static final String RESOURCE_ACCESS = "resource_access";

    private static final String ROLES = "roles";

    private String clientId;

    private SimpleAuthorityMapper simpleAuthorityMapper;

    @Override
    public Collection<GrantedAuthority> convert(ClaimAccessor claimAccessor) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : getRealmRoles(claimAccessor)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        for (String role : getResourceAccessRoles(claimAccessor)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        if (simpleAuthorityMapper != null) {
            return simpleAuthorityMapper.mapAuthorities(grantedAuthorities);
        }
        return grantedAuthorities;
    }

    private Collection<String> getRealmRoles(ClaimAccessor claimAccessor) {
        Object claim = claimAccessor.getClaim(REALM_ACCESS);
        return getRolesFromClaim(claim, null);
    }

    private Collection<String> getResourceAccessRoles(ClaimAccessor claimAccessor) {
        Object claim = claimAccessor.getClaim(RESOURCE_ACCESS);
        return getRolesFromClaim(claim, clientId);
    }

    private Collection<String> getRolesFromClaim(Object claim, String nestedClaimName) {
        if (claim instanceof String) {
            if (StringUtils.hasText((String) claim)) {
                return Arrays.asList(((String) claim).split(" "));
            }
            return Collections.emptyList();
        }
        if (claim instanceof Collection) {
            return castRolesToCollection(claim);
        }
        if (claim instanceof Map<?, ?> map) {
            if (nestedClaimName != null) {
                Object nestedClaim = map.get(nestedClaimName);
                return getRolesFromClaim(nestedClaim, null);
            }
            return castRolesToCollection(map.get(ROLES));
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> castRolesToCollection(Object roles) {
        return (Collection<String>) roles;
    }
}
