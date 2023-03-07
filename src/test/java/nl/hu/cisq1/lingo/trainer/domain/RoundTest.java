package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
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
}