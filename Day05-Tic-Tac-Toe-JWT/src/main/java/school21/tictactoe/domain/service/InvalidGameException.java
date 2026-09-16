package school21.tictactoe.domain.service;

public class InvalidGameException extends RuntimeException {
    public InvalidGameException(String message) {
        super(message);
    }

    public InvalidGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
