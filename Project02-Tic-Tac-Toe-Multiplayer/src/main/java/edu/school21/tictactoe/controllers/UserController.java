package edu.school21.tictactoe.controllers;

import edu.school21.tictactoe.models.User;
import edu.school21.tictactoe.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(Map.of("id", user.get().getId(), "login", user.get().getLogin()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
