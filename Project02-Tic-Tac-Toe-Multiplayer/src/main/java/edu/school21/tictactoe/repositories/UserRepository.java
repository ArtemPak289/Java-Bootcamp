package edu.school21.tictactoe.repositories;

import edu.school21.tictactoe.models.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByLogin(String login);
}
