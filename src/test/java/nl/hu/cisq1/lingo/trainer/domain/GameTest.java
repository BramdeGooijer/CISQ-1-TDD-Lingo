package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    @DisplayName("on game creation the score should be 0")
    void scoreShouldBeZero() {
        Game game = new Game();

        assertEquals(0, game.getScore());
    }

    @Test
    @DisplayName("on game creation the gameState should be active")
    void gameStateShouldBeActive() {
        Game game = new Game();

        assertEquals(ACTIVE, game.getGameState());
    }

    @Test
    @DisplayName("on game creation there should be an empty list of rounds")
    void emptyListOfRoundsCheck() {
        Game game = new Game();

        assertEquals(0, game.getRounds().size());
    }

    @Test
    @DisplayName("when starting a game a round should be added to rounds")
    void roundShouldBeAdded() {
        Game game = new Game();

        game.startGame();

        assertEquals(1, game.getRounds().size());
    }
}