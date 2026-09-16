package school21.tictactoe.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import school21.tictactoe.domain.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpiration);
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtAccessSecret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    private boolean validateToken(String token, SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtAccessSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
