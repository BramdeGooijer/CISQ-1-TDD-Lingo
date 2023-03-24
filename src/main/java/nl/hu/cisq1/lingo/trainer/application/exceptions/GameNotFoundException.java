package nl.hu.cisq1.lingo.trainer.application.exceptions;

import java.util.function.Supplier;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
