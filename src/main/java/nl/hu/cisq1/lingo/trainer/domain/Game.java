package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameAlreadyStartedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameNotStartedException;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;

public class Game {
    private int score;
    private Status gameState;
    private List<Round> allRounds = new ArrayList<>();
    private Round currentRound;

    public Game() {
        this.score = 0;
        this.gameState = ACTIVE;
    }

    public void startGame(String word) {
        if (currentRound == null) {
            Round round = new Round(word);
            this.allRounds.add(round);
            this.currentRound = round;
        } else {
            throw new GameAlreadyStartedException("De game is al begonnen!");
        }
    }

    public void guessWord(String attempt) {
        if (currentRound == null) {
            throw new GameNotStartedException("De game is nog niet begonnen");
        }

        this.currentRound.guessWord(attempt);

        if (currentRound.getRoundState() == WON) {
            this.score += 5 * (5 - currentRound.getAmountOfGuesses()) + 5;
        }
    }

    public int getScore() {
        return this.score;
    }

    public Status getGameState() {
        return this.gameState;
    }

    public List<Round> getAllRounds() {
        return this.allRounds;
    }

    public Round getCurrentRound() {
        return currentRound;
    }
}
