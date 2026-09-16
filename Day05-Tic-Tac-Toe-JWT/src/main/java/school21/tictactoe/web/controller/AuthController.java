package school21.tictactoe.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school21.tictactoe.domain.service.AuthService;
import school21.tictactoe.web.model.JwtRequest;
import school21.tictactoe.web.model.JwtResponse;
import school21.tictactoe.web.model.RefreshJwtRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        return ResponseEntity.ok(authService.getAccessToken(request.getRefreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        return ResponseEntity.ok(authService.refreshRefreshToken(request.getRefreshToken()));
    }
}
