package school21.tictactoe.datasource.repository;

import school21.tictactoe.datasource.mapper.DatasourceMapper;
import school21.tictactoe.datasource.mapper.DatasourceMapperImpl;
import school21.tictactoe.datasource.model.Game;

import java.util.Optional;
import java.util.UUID;

public class GameRepositoryImpl implements GameRepository {
    private final GameStorage storage;
    private final DatasourceMapper mapper;

    public GameRepositoryImpl(GameStorage storage) {
        this(storage, new DatasourceMapperImpl());
    }

    public GameRepositoryImpl(GameStorage storage, DatasourceMapper mapper) {
        this.storage = storage;
        this.mapper = mapper != null ? mapper : new DatasourceMapperImpl();
    }

    @Override
    public void save(Game game) {
        if (game != null) {
            storage.save(game);
        }
    }

    @Override
    public Game get(UUID id) {
        if (id == null) return null;
        return storage.get(id);
    }

    @Override
    public void save(school21.tictactoe.domain.model.Game game) {
        if (game != null) {
            storage.save(mapper.toDatasource(game));
        }
    }

    @Override
    public school21.tictactoe.domain.model.Game getDomain(UUID id) {
        if (id == null) return null;
        Game entity = storage.get(id);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return storage.findById(id);
    }
}
