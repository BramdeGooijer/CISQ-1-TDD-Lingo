package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class RoundStillActiveException extends RuntimeException {
    public RoundStillActiveException(String message) {
        super(message);
    }
}
