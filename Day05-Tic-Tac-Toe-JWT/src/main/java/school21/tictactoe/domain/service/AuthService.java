package school21.tictactoe.domain.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school21.tictactoe.domain.model.Role;
import school21.tictactoe.domain.model.User;
import school21.tictactoe.web.model.JwtRequest;
import school21.tictactoe.web.model.JwtResponse;
import school21.tictactoe.web.security.JwtAuthentication;
import school21.tictactoe.web.security.JwtProvider;

import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        
        // Ensure at least one user exists for testing
        if (userService.findByLogin("testuser").isEmpty()) {
            userService.save(new User("testuser", passwordEncoder.encode("password"), Set.of(Role.USER)));
        }
    }

    public JwtResponse login(JwtRequest authRequest) {
        User user = userService.findByLogin(authRequest.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getClaims(refreshToken);
            String uuid = claims.getSubject();
            User user = userService.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new RuntimeException("User not found"));
            String accessToken = jwtProvider.generateAccessToken(user);
            return new JwtResponse(accessToken, refreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public JwtResponse refreshRefreshToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getClaims(refreshToken);
            String uuid = claims.getSubject();
            User user = userService.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new RuntimeException("User not found"));
            String accessToken = jwtProvider.generateAccessToken(user);
            String newRefreshToken = jwtProvider.generateRefreshToken(user);
            return new JwtResponse(accessToken, newRefreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
