package school21.tictactoe.datasource.repository;

import school21.tictactoe.datasource.model.Game;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameStorage {
    private final ConcurrentHashMap<UUID, Game> storage = new ConcurrentHashMap<>();

    public void save(Game game) {
        if (game != null && game.getId() != null) {
            storage.put(game.getId(), game);
        }
    }

    public Game get(UUID id) {
        if (id == null) return null;
        return storage.get(id);
    }

    public Optional<Game> findById(UUID id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    public boolean exists(UUID id) {
        return id != null && storage.containsKey(id);
    }

    public void delete(UUID id) {
        if (id != null) {
            storage.remove(id);
        }
    }

    public void clear() {
        storage.clear();
    }

    public int size() {
        return storage.size();
    }

    public Map<UUID, Game> getAll() {
        return new ConcurrentHashMap<>(storage);
    }
}
