package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.*;

public class Feedback {
    private String attempt;
    private List<Mark> marks;

    public Feedback(String wordToGuess, String attempt) {
        this.attempt = attempt;
        this.marks = this.generate(wordToGuess, attempt);
    }

    public boolean isWordGuessed() {
        return this.marks
                .stream()
                .allMatch(CORRECT::equals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    public List<Mark> generate(String wordToGuess, String attempt) {
        List<Mark> marks = new ArrayList<>();

        if (attempt.length() != wordToGuess.length()) {
            for (int i = 0; i < attempt.length(); i++) {
                marks.add(INVALID);
            }
            return marks;
        }

        for (int i = 0; i < wordToGuess.length(); i++) {
            char letterWord = wordToGuess.charAt(i);
            char letterAttempt = attempt.charAt(i);

            if (letterAttempt == letterWord) {
                marks.add(CORRECT);
            } else if (wordToGuess.indexOf(letterAttempt) >= 0) {
                marks.add(PRESENT);
            } else {
                marks.add(ABSENT);
            }
        }

        return marks;
    }

    public String giveHint(String previousHint) {
        String result = "";

        if (this.attempt.length() > previousHint.length()) {
            return previousHint;
        }

        for (int i = 0; i < this.marks.size(); i++) {
            char letter = this.attempt.charAt(i);
            Mark mark = this.marks.get(i);


            if (mark.equals(CORRECT)) {
                result += letter;
            } else {
                result += previousHint.charAt(i);
            }
        }

        return result;
    }

    public List<Mark> getMarks() {
        return marks;
    }
}
