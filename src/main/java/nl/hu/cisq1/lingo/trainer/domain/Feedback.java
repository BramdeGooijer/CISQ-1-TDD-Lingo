package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;

import java.util.*;

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
        /**
        HOE PAK JE GENERATE MARKS CORRECT AAN
        dictionary met hoe vaak een letter voor kan voorkomen als die 0 is absent teruggeven
        loop eerst over correct en dan over present if already one with correct present -> absent
        1 keer overheen met alles correct voorgang geven daarna normaal
        **/

        List<Mark> marks = new ArrayList<>();

//        check if word is too long
        if (attempt.length() != wordToGuess.length()) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                marks.add(INVALID);
            }
            return marks;
        }

        Map<String, Integer> letterCount = new HashMap<>();

        List<String> letters = new ArrayList<>();
        int amount = 0;

//        algorithm for counting character occurances
        for (int i = 0; i < wordToGuess.length(); i++) {
            for(int x = 0; x < wordToGuess.length(); x++) {
                if (String.valueOf(wordToGuess.charAt(i)).equals(String.valueOf(wordToGuess.charAt(x)))) {
                    amount += 1;
                }
            }
            if (!letters.contains(String.valueOf(wordToGuess.charAt(i)))) {
                letters.add(String.valueOf(wordToGuess.charAt(i)));
                letterCount.put(String.valueOf(wordToGuess.charAt(i)), amount);
            }
            amount = 0;
        }

//        loop over letters that are correct and set the rest on hold
        for (int i = 0; i < wordToGuess.length(); i++) {
            char letterWord = wordToGuess.charAt(i);
            char letterAttempt = attempt.charAt(i);

            if (letterAttempt == letterWord) {
                marks.add(CORRECT);
                letterCount.put(String.valueOf(letterWord), letterCount.get(String.valueOf(letterWord)) - 1);
            } else {
                marks.add(HOLD);
            }
        }

//        check if there are still (the same) letters left and put those on present otherwise put them on absent
        for (int i = 0; i < wordToGuess.length(); i++) {
            char letterAttempt = attempt.charAt(i);

            if (    wordToGuess.indexOf(letterAttempt) >= 0 &&
                    letterCount.get(String.valueOf(letterAttempt)) > 0 &&
                    !marks.get(i).equals(CORRECT)) {

                marks.set(i, PRESENT);
                letterCount.put(String.valueOf(letterAttempt), letterCount.get(String.valueOf(letterAttempt)) - 1);

            } else if (marks.get(i) != CORRECT) {
                marks.set(i, ABSENT);
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
