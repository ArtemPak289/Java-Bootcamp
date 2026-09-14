package edu.school21.tictactoe.controllers;

import edu.school21.tictactoe.models.SignUpRequest;
import edu.school21.tictactoe.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
        boolean success = authService.register(signUpRequest);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Login already exists"));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authorize(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        UUID uuid = authService.authorize(authHeader);
        if (uuid != null) {
            return ResponseEntity.ok(Map.of("uuid", uuid));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    }
}
