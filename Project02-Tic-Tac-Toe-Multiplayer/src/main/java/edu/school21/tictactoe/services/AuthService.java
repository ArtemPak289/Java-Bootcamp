package edu.school21.tictactoe.services;

import edu.school21.tictactoe.models.SignUpRequest;
import edu.school21.tictactoe.models.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean register(SignUpRequest request) {
        if (userService.findByLogin(request.getLogin()).isPresent()) {
            return false;
        }
        User user = new User(request.getLogin(), request.getPassword());
        userService.save(user);
        return true;
    }

    public UUID authorize(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return null;
        }
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        
        if (values.length != 2) {
            return null;
        }
        String login = values[0];
        String password = values[1];
        
        Optional<User> userOptional = userService.findByLogin(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return user.getId();
            }
        }
        return null;
    }
}
