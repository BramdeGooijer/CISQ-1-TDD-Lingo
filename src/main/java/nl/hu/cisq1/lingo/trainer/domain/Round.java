package nl.hu.cisq1.lingo.trainer.domain;

import jakarta.persistence.*;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.ClosedRoundException;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;

@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String hint;

    @Enumerated(EnumType.STRING)
    private Status roundState;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> guesses = new ArrayList<>();

    public Round() {

    }

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

        if (this.getAmountOfGuesses() >= 5 && !feedback.isWordGuessed()) {
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

    public List<Feedback> getGuesses() {
        return guesses;
    }

    public Long getId() {
        return id;
    }
}
