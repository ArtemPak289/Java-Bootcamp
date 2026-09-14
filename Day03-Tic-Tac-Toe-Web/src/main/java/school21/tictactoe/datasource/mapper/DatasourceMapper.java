package school21.tictactoe.datasource.mapper;

public interface DatasourceMapper {

    school21.tictactoe.datasource.model.Game toDatasource(school21.tictactoe.domain.model.Game domainGame);

    school21.tictactoe.domain.model.Game toDomain(school21.tictactoe.datasource.model.Game datasourceGame);

    school21.tictactoe.datasource.model.Board toDatasourceBoard(school21.tictactoe.domain.model.Board domainBoard);

    school21.tictactoe.domain.model.Board toDomainBoard(school21.tictactoe.datasource.model.Board datasourceBoard);
}
