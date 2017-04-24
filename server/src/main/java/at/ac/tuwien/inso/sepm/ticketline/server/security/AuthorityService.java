package at.ac.tuwien.inso.sepm.ticketline.server.security;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorityService {

    // authorities for each role
    private static final String[] admin = {
        "ADMIN",
        "USER"
    };

    private static final String[] user = {
        "USER"
    };

    // helper methods to use that class
    public static Set<? extends GrantedAuthority> getAuthoritiesForRole(Principal.Role role) {
        return Arrays
            .asList(getAuthorityStrings(role))
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }

    private static String[] getAuthorityStrings(Principal.Role role) {
        switch (role) {
            case ADMIN:
                return admin;
            case SELLER:
                return user;
        }
        return new String[] {};
    }
}
