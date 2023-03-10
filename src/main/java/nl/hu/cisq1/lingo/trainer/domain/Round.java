package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.ClosedRoundException;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;

import java.util.ArrayList;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;

public class Round {
    private String word;
    private String hint;
    private Status roundState;
    private ArrayList<Feedback> guesses = new ArrayList<>();

    public Round(String word) {
        this.word = word;
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

    public void guessWord(String attempt) {
        if (this.roundState == LOST) {
            throw new ClosedRoundException("De ronde is verloren!");
        }

        if (this.roundState == WON) {
            throw new ClosedRoundException("De ronde is gewonnen!");
        }


        Feedback feedback = new Feedback(this.word, attempt);
        this.hint = feedback.giveHint(this.hint);
        guesses.add(feedback);


        if (feedback.isWordGuessed()) {
            this.roundState = WON;
        }

        if (this.getAmountOfGuesses() >= 5) {
            this.roundState = LOST;
        }
    }

    public String getHint() {
        return this.hint;
    }

    public int getAmountOfGuesses() {
        return this.guesses.size();
    }

    public Status getRoundState() {
        return roundState;
    }
}
