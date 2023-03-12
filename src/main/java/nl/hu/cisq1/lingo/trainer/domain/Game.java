package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;

public class Game {
    private int score;
    private Status gameState;
    private List<Round> rounds = new ArrayList<>();

    public Game() {
        this.score = 0;
        this.gameState = ACTIVE;
    }

    public void startGame() {

    }

    public int getScore() {
        return this.score;
    }

    public Status getGameState() {
        return this.gameState;
    }

    public List<Round> getRounds() {
        return this.rounds;
    }
}
