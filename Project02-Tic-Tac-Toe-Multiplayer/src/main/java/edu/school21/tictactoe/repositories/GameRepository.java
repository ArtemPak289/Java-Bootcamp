package edu.school21.tictactoe.repositories;

import edu.school21.tictactoe.models.Game;
import edu.school21.tictactoe.models.GameState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends CrudRepository<Game, UUID> {
    List<Game> findByState(GameState state);
}
