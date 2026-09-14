package school21.tictactoe.di;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import school21.tictactoe.datasource.mapper.DatasourceMapper;
import school21.tictactoe.datasource.mapper.DatasourceMapperImpl;
import school21.tictactoe.datasource.repository.GameRepository;
import school21.tictactoe.datasource.repository.GameRepositoryImpl;
import school21.tictactoe.datasource.repository.GameStorage;
import school21.tictactoe.domain.service.GameService;
import school21.tictactoe.domain.service.GameServiceImpl;
import school21.tictactoe.web.mapper.WebMapper;
import school21.tictactoe.web.mapper.WebMapperImpl;

@Configuration
public class GameConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public GameStorage gameStorage() {
        return new GameStorage();
    }

    @Bean
    public DatasourceMapper datasourceMapper() {
        return new DatasourceMapperImpl();
    }

    @Bean
    public GameRepository gameRepository(GameStorage gameStorage, DatasourceMapper datasourceMapper) {
        return new GameRepositoryImpl(gameStorage, datasourceMapper);
    }

    @Bean
    public GameService gameService(GameRepository gameRepository, DatasourceMapper datasourceMapper) {
        return new GameServiceImpl(gameRepository, datasourceMapper);
    }

    @Bean
    public WebMapper webMapper() {
        return new WebMapperImpl();
    }
}
