package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.ClosedRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameAlreadyStartedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameNotStartedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundStillActiveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    @DisplayName("on game creation the score should be 0")
    void scoreShouldBeZero() {
        Game game = new Game();

        assertEquals(0, game.getScore());
    }

    @Test
    @DisplayName("on game creation the gameState should be active")
    void gameStateShouldBeActive() {
        Game game = new Game();

        assertEquals(ACTIVE, game.getGameState());
    }

    @Test
    @DisplayName("on game creation there should be an empty list of rounds")
    void emptyListOfRoundsCheck() {
        Game game = new Game();

        assertEquals(0, game.getAllRounds().size());
    }

    @Test
    @DisplayName("when starting a game current round should be set")
    void currentRoundIsSet() {
        Game game = new Game();

        game.startGame("woord");

        assertEquals(Round.class, game.getCurrentRound().getClass());
    }

    @Test
    @DisplayName("when starting a game a round should be added to rounds")
    void roundShouldBeAdded() {
        Game game = new Game();

        game.startGame("woord");

        assertEquals(1, game.getAllRounds().size());
    }

    @Test
    @DisplayName("reject starting a game when game already started")
    void rejectGameWhenStarted() {
        Game game = new Game();

        game.startGame("woord");

        assertThrows(
                GameAlreadyStartedException.class,
                () -> game.startGame("woord")
        );
    }

    @Test
    @DisplayName("guessing word through Game correct, check roundstate")
    void guessingThroughGameCorrect() {
        Game game = new Game();

        game.startGame("woord");
        game.guessWord("woord");

        assertEquals(WON, game.getCurrentRound().getRoundState());
    }

    @Test
    @DisplayName("guessing word through Game wrong, check roundstate")
    void guessingThroughGameWrong() {
        Game game = new Game();

        game.startGame("woord");

        game.guessWord("waard");
        game.guessWord("waard");
        game.guessWord("waard");
        game.guessWord("waard");
        game.guessWord("waard");

        assertEquals(LOST, game.getCurrentRound().getRoundState());
    }

    @Test
    @DisplayName("guessing word through Game when game is already Won")
    void guessingWordWhenGameAlreadyWon() {
        Game game = new Game();

        game.startGame("woord");
        game.guessWord("woord");

        assertThrows(
                ClosedRoundException.class,
                () -> game.guessWord("waard")
        );

    }

    @Test
    @DisplayName("guessing word through Game when game is already Won")
    void guessingWordWhenGameAlreadyLost() {
        Game game = new Game();

        game.startGame("woord");

        game.guessWord("waard");
        game.guessWord("waard");
        game.guessWord("waard");
        game.guessWord("waard");
        game.guessWord("waard");

        assertThrows(
                ClosedRoundException.class,
                () -> game.guessWord("waard")
        );
    }

    @Test
    @DisplayName("guessing a word when game hasn't started")
    void guessingWhenGameNotStarted() {
        Game game = new Game();

        assertThrows(
                GameNotStartedException.class,
                () -> game.guessWord("waard")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScoreExamples")
    @DisplayName("winning rounds should give back correct scores")
    void winningRoundsGiveCorrectScore(int amountOfGuessesWrong, int expectedScore) {
        Game game = new Game();

        game.startGame("woord");

        for (int i = 0; i < amountOfGuessesWrong;  i++) {
            game.guessWord("waard");
        }

        game.guessWord("woord");

        assertEquals(expectedScore, game.getScore());
    }

    public static Stream<Arguments> provideScoreExamples() {
        return Stream.of(
                Arguments.of(0, 25),
                Arguments.of(1, 20),
                Arguments.of(2, 15),
                Arguments.of(3, 10),
                Arguments.of(4, 5)
        );
    }

    @Test
    @DisplayName("calling startRound starts a new round")
    void callingStartRoundStartsRound() {
        Game game = new Game();

        game.startGame("woord");
        game.guessWord("woord");

        Round newRound = game.startRound("autos");

        assertEquals(2, game.getAllRounds().size());
        assertEquals(game.getCurrentRound(), newRound);
    }

    @Test
    @DisplayName("reject startRound when game not active")
    void rejectStartRoundWhenGameNotActive() {
        Game game = new Game();

        assertThrows(
                GameNotStartedException.class,
                () -> game.startRound("woord")
        );
    }

    @Test
    @DisplayName("reject startRound when round is still active")
    void rejectStartRoundWhenRoundStillActive() {
        Game game = new Game();

        game.startGame("woord");
        game.guessWord("woord");

        game.startRound("waard");

        assertThrows(
                RoundStillActiveException.class,
                () -> game.startRound("woest")
        );
    }

}