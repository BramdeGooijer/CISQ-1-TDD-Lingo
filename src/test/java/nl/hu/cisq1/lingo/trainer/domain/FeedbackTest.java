package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}