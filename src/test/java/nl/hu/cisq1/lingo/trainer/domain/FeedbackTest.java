package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    @Test
    @DisplayName("check if hashcodes are equal")
    void hashcodeIsEqual() {
        Feedback feedbackA = new Feedback("woord", "woord");
        Feedback feedbackB = new Feedback("woord", "woord");

        assertEquals(feedbackA.hashCode(), feedbackB.hashCode());
    }

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        /**
        * GIVEN: none
        * WHEN: create a new Feedback object with a guess "woord"
        * and marks: List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
        * THEN: the result of feedback.isWordGuessed() should be true.
        **/

        Feedback feedback = new Feedback(
                "woord",
                "woord"
        );

        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        Feedback feedback = new Feedback(
                "beest",
                "woord"
        );

        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("two feedbacks are the same")
    void sameFeedback() {
        // Given
        var feedbackA = new Feedback(
                "beest",
                "woord"
        );

        var feedbackB = new Feedback(
                "beest",
                "woord"
        );

        // When
        boolean result = feedbackA.equals(feedbackB);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("two feedback attempts are not the same")
    void differentFeedbackAttempt() {
        // Given
        var feedbackA = new Feedback(
                "beest",
                "waard"
        );

        var feedbackB = new Feedback(
                "beest",
                "woord"
        );

        // When
        boolean result = feedbackA.equals(feedbackB);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("a hint always contains the first letter")
    void hintFirstLetter() {
        // word to guess: beest
        // attempt      : woord
        Feedback feedback = new Feedback(
                "beest",
                "woord"
        );

        String previousHint = "b....";

        String nextHint = feedback.giveHint(previousHint);

        String expectedHint = "b....";

        assertEquals(expectedHint, nextHint);
    }

    @Test
    @DisplayName("a hint always contains correct letters after a guess")
    void hintForCorrectLetters() {
        // word to guess: beest
        // attempt      : plaat
        Feedback feedback = new Feedback(
                "beest",
                "plaat"
        );

        String previousHint = "b....";

        String nextHint = feedback.giveHint(previousHint);

        String expectedHint = "b...t";

        assertEquals(expectedHint, nextHint);
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("getting the correct hint for guesses")
    void correctHintForGuesses(String wordToGuess, String previousHint, String attempt, String expectedHint) {
        // make feedback with the wordToGuess and the attempt, it generates feedback inside the constructor
        Feedback feedback = new Feedback(wordToGuess, attempt);

        String nextHint = feedback.giveHint(previousHint);


        assertEquals(nextHint, expectedHint);
    }

    public static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("groep", "g....", "gegroet", "g...."),
                Arguments.of("groep", "g....", "gerst", "g...."),
                Arguments.of("groep", "g....", "genen", "g..e."),
                Arguments.of("groep", "g..e.", "gedoe", "g..e."),
                Arguments.of("groep", "g..e.", "groep", "groep")
        );
    }

    @Test
    @DisplayName("generating marks for a correct word")
    void generatingMarksForCorrectWord() {
        Feedback feedback = new Feedback("woord", "woord");

        assertEquals(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), feedback.getMarks());
    }

    @Test
    @DisplayName("generating marks for an incorrect word")
    void genaratingMarksForIncorrectWord() {
        Feedback feedback = new Feedback("woord", "chase");

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT), feedback.getMarks());
    }

    @Test
    @DisplayName("generating marks for a word with letters present")
    void generatingMarksForPresentLettersInWord() {
        Feedback feedback = new Feedback("aaaab", "baaaa");

        assertEquals(List.of(PRESENT, CORRECT, CORRECT, CORRECT, PRESENT), feedback.getMarks());
    }

    @Test
    @DisplayName("generating marks for a word that is too long (INVALID)")
    void generatingMarksForLongWord() {
        Feedback feedback = new Feedback("kikker", "kikkerdril");

        assertEquals(List.of(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID), feedback.getMarks());
    }
}