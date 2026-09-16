package school21.tictactoe.domain.service;

import org.springframework.stereotype.Service;
import school21.tictactoe.datasource.repository.UserRepository;
import school21.tictactoe.domain.model.User;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findById(java.util.UUID id) {
        return userRepository.findById(id);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
}
