package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;

import java.util.ArrayList;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;

public class Round {
    private String word;
    private int amountOfGuesses;
    private String hint;
    private Status roundState;
    private ArrayList<Feedback> guesses = new ArrayList<>();

    public Round(String word) {
        this.word = word;
        this.amountOfGuesses = 0;
        this.hint = generateHintOnStart();
        this.roundState = ACTIVE;
    }

    private String generateHintOnStart() {
        String hint = String.valueOf(word.charAt(0));

        for (int i = 1; i < word.length(); i++) {
            hint += ".";
        }

        return hint;
    }

    public String getHint() {
        return this.hint;
    }

    public int getAmountOfGuesses() {
        return this.amountOfGuesses;
    }

    public Status getRoundState() {
        return roundState;
    }
}
