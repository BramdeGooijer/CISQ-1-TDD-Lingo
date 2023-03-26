package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.ClosedRoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    @Test
    @DisplayName("creating round through empty constructor")
    void creatingRoundThroughEmptyConstructor() {
        Round round = new Round();

        assertEquals(Round.class, round.getClass());
    }

    @Test
    @DisplayName("on round creation the first letter of hint should be shown")
    void hintFirstLetter() {
        Round round = new Round("woord");

        assertEquals("w....", round.getHint());
    }

    @Test
    @DisplayName("on round creation the amountOfGuesses should be 0")
    void zeroAmountOfGuesses() {
        Round round = new Round("woord");

        assertEquals(0, round.getAmountOfGuesses());
    }

    @Test
    @DisplayName("on round creation the roundState should be ACTIVE")
    void activeRoundState() {
        Round round = new Round("woord");

        assertEquals(ACTIVE, round.getRoundState());
    }

    @Test
    @DisplayName("a round out of guesses should reject making a guess")
    void guessingWordWithoutAttemptsLeft() {
        Round round = new Round("woord");

        round.guessWord("waard");
        round.guessWord("waard");
        round.guessWord("waard");
        round.guessWord("waard");
        round.guessWord("waard");

        assertThrows(
                ClosedRoundException.class,
                () -> round.guessWord("waard")
        );
    }

    @Test
    @DisplayName("out of guesses should set roundState to LOST")
    void outOfGuessesClosesRoundState() {
        Round round = new Round("woord");

        round.guessWord("waard");
        round.guessWord("waard");
        round.guessWord("waard");
        round.guessWord("waard");
        round.guessWord("waard");

        assertEquals(LOST, round.getRoundState());
    }

    @Test
    @DisplayName("guessing a word updates the hint attribute")
    void guessingWordUpdatesHint() {
        Round round = new Round("woord");

        round.guessWord("waard");

        assertEquals("w..rd", round.getHint());
    }

    @Test
    @DisplayName("guessing a word correctly changes roundstate to WON")
    void guessingWordCorrectly()  {
        Round round = new Round("woord");

        round.guessWord("woord");

        assertEquals(WON, round.getRoundState());
    }

    @Test
    @DisplayName("guessing a word not completely correct doesn't change roundstate")
    void guessingWordIncorrectly()  {
        Round round = new Round("woord");

        round.guessWord("waard");

        assertEquals(ACTIVE, round.getRoundState());
    }

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("guessing a word adds the guess to guesses")
    void guessingWordAddsGuessToList(int expectedLength, String woord)  {
        Round round = new Round("woord");

        for (int i = 0; i < expectedLength; i++) {
            round.guessWord(woord);
        }
        
        assertEquals(expectedLength, round.getAmountOfGuesses());

    }

    public static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of(1, "waard"),
                Arguments.of(2, "waard"),
                Arguments.of(3, "waard"),
                Arguments.of(4, "waard"),
                Arguments.of(5, "waard")
        );
    }
}