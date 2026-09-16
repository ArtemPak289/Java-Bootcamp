package school21.tictactoe.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school21.tictactoe.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLogin(String login);
}
