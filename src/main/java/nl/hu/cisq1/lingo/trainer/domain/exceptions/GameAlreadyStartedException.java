package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
