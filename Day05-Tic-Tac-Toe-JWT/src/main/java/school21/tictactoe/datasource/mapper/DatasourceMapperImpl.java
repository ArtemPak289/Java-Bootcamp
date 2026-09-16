package school21.tictactoe.datasource.mapper;

public class DatasourceMapperImpl implements DatasourceMapper {

    @Override
    public school21.tictactoe.datasource.model.Game toDatasource(school21.tictactoe.domain.model.Game domainGame) {
        if (domainGame == null) {
            return null;
        }
        school21.tictactoe.datasource.model.Game dsGame = new school21.tictactoe.datasource.model.Game(
                domainGame.getId(),
                toDatasourceBoard(domainGame.getBoard())
        );
        dsGame.setUserId(domainGame.getUserId());
        dsGame.setCreationDate(domainGame.getCreationDate());
        dsGame.setStatus(domainGame.getStatus());
        return dsGame;
    }

    @Override
    public school21.tictactoe.domain.model.Game toDomain(school21.tictactoe.datasource.model.Game datasourceGame) {
        if (datasourceGame == null) {
            return null;
        }
        school21.tictactoe.domain.model.Game domainGame = new school21.tictactoe.domain.model.Game(
                datasourceGame.getId(),
                toDomainBoard(datasourceGame.getBoard())
        );
        domainGame.setUserId(datasourceGame.getUserId());
        domainGame.setCreationDate(datasourceGame.getCreationDate());
        domainGame.setStatus(datasourceGame.getStatus());
        return domainGame;
    }

    @Override
    public school21.tictactoe.datasource.model.Board toDatasourceBoard(school21.tictactoe.domain.model.Board domainBoard) {
        if (domainBoard == null) {
            return null;
        }
        return new school21.tictactoe.datasource.model.Board(domainBoard.getBoard());
    }

    @Override
    public school21.tictactoe.domain.model.Board toDomainBoard(school21.tictactoe.datasource.model.Board datasourceBoard) {
        if (datasourceBoard == null) {
            return null;
        }
        return new school21.tictactoe.domain.model.Board(datasourceBoard.getBoard());
    }
}
