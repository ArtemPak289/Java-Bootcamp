package school21.tictactoe.web.mapper;

public interface WebMapper {

    school21.tictactoe.web.model.Game toWeb(school21.tictactoe.domain.model.Game domainGame);

    school21.tictactoe.domain.model.Game toDomain(school21.tictactoe.web.model.Game webGame);

    school21.tictactoe.web.model.Board toWebBoard(school21.tictactoe.domain.model.Board domainBoard);

    school21.tictactoe.domain.model.Board toDomainBoard(school21.tictactoe.web.model.Board webBoard);
}
