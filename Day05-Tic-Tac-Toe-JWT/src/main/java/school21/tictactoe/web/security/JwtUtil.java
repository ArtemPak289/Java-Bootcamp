package school21.tictactoe.web.security;

import io.jsonwebtoken.Claims;
import school21.tictactoe.domain.model.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtUtil {
    public static JwtAuthentication generate(Claims claims) {
        String uuidString = claims.getSubject();
        List<String> rolesList = claims.get("roles", List.class);
        Set<Role> roles = rolesList != null ? rolesList.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet()) : Set.of();
        
        JwtAuthentication auth = new JwtAuthentication(UUID.fromString(uuidString), roles);
        auth.setAuthenticated(true);
        return auth;
    }
}
