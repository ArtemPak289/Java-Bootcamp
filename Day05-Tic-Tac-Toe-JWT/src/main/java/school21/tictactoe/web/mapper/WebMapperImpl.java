package school21.tictactoe.web.mapper;

public class WebMapperImpl implements WebMapper {

    @Override
    public school21.tictactoe.web.model.Game toWeb(school21.tictactoe.domain.model.Game domainGame) {
        if (domainGame == null) {
            return null;
        }
        return new school21.tictactoe.web.model.Game(
                domainGame.getId(),
                toWebBoard(domainGame.getBoard())
        );
    }

    @Override
    public school21.tictactoe.domain.model.Game toDomain(school21.tictactoe.web.model.Game webGame) {
        if (webGame == null) {
            return null;
        }
        return new school21.tictactoe.domain.model.Game(
                webGame.getId() != null ? webGame.getId() : webGame.getUuid(),
                toDomainBoard(webGame.getBoard())
        );
    }

    @Override
    public school21.tictactoe.web.model.Board toWebBoard(school21.tictactoe.domain.model.Board domainBoard) {
        if (domainBoard == null) {
            return null;
        }
        return new school21.tictactoe.web.model.Board(domainBoard.getBoard());
    }

    @Override
    public school21.tictactoe.domain.model.Board toDomainBoard(school21.tictactoe.web.model.Board webBoard) {
        if (webBoard == null) {
            return null;
        }
        return new school21.tictactoe.domain.model.Board(webBoard.getBoard());
    }
}
