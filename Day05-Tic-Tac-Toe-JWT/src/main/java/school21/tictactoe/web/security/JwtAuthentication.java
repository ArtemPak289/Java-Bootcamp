package school21.tictactoe.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import school21.tictactoe.domain.model.Role;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class JwtAuthentication implements Authentication {
    private boolean authenticated;
    private UUID uuid;
    private Set<Role> roles;

    public JwtAuthentication(UUID uuid, Set<Role> roles) {
        this.uuid = uuid;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return uuid;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return uuid.toString();
    }
}
