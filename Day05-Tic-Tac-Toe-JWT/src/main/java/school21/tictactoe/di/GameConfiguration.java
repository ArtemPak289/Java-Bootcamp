package school21.tictactoe.di;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import school21.tictactoe.datasource.mapper.DatasourceMapper;
import school21.tictactoe.datasource.mapper.DatasourceMapperImpl;
import school21.tictactoe.datasource.repository.GameRepository;
import school21.tictactoe.domain.service.GameService;
import school21.tictactoe.domain.service.GameServiceImpl;
import school21.tictactoe.web.mapper.WebMapper;
import school21.tictactoe.web.mapper.WebMapperImpl;

@Configuration
public class GameConfiguration {

    @Bean
    public DatasourceMapper datasourceMapper() {
        return new DatasourceMapperImpl();
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
