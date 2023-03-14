package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class GameNotStartedException extends RuntimeException {
    public GameNotStartedException(String message) {
        super(message);
    }
}
