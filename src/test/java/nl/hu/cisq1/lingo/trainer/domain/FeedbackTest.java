package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.Exceptions.attemptMarkLengthException;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
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
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        Feedback feedback = new Feedback(
                "woord",
                List.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("two feedbacks are the same")
    void sameFeedback() {
        // Given
        var feedbackA = new Feedback(
                "woord",
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        var feedbackB = new Feedback(
                "woord",
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
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
                "waard",
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        var feedbackB = new Feedback(
                "woord",
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        // When
        boolean result = feedbackA.equals(feedbackB);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("two feedback marks are not the same")
    void differentFeedbackMark() {
        // Given
        var feedbackA = new Feedback(
                "woord",
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        var feedbackB = new Feedback(
                "woord",
                List.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        // When
        boolean result = feedbackA.equals(feedbackB);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("two feedback marks and attempts are not the same")
    void differentFeedbackMarkAndAttempt() {
        // Given
        var feedbackA = new Feedback(
                "waard",
                List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        var feedbackB = new Feedback(
                "woord",
                List.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        // When
        boolean result = feedbackA.equals(feedbackB);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("a hint always contains the first letter")
    void hintFirstLetter() throws attemptMarkLengthException {
        // word to guess: beest
        // attempt      : woord
        Feedback feedback = new Feedback(
                "woord",
                List.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        String previousHint = "b....";

        String nextHint = feedback.giveHint(previousHint);

        String expectedHint = "b....";

        assertEquals(expectedHint, nextHint);
    }

    @Test
    @DisplayName("a hint always contains correct letters after a guess")
    void hintForCorrectLetters() throws attemptMarkLengthException {
        // word to guess: beest
        // attempt      : plaat
        Feedback feedback = new Feedback(
                "plaat",
                List.of(ABSENT, ABSENT, ABSENT, ABSENT, CORRECT)
        );

        String previousHint = "b....";

        String nextHint = feedback.giveHint(previousHint);

        String expectedHint = "b...t";

        assertEquals(expectedHint, nextHint);
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("getting the correct hint for guesses")
    void correctHintForGuesses(String previousHint, String attempt, List<Mark> marks, String expectedHint) throws attemptMarkLengthException {
        // make feedback with the given data
        Feedback feedback = new Feedback(attempt, marks);

        String nextHint = feedback.giveHint(previousHint);

        assertEquals(nextHint, expectedHint);
    }

    public static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                // word to guess is groep
                Arguments.of("g....", "gegroet", List.of(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID), "g...."),
                Arguments.of("g....", "gerst", List.of(CORRECT, PRESENT, PRESENT, ABSENT, ABSENT), "g...."),
                Arguments.of("g....", "genen", List.of(CORRECT, ABSENT, ABSENT, CORRECT, ABSENT), "g..e."),
                Arguments.of("g..e.", "gedoe", List.of(CORRECT, PRESENT, ABSENT, PRESENT, ABSENT), "g..e."),
                Arguments.of("g..e.", "groep", List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), "groep")
        );
    }
}