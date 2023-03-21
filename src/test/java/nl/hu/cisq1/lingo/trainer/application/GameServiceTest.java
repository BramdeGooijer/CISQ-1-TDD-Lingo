package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameServiceTest {
//    @Test
//    @DisplayName("generating GameDTO correctly")
//    void generatingDTO() {
//        Game game = new Game();
//        game.startGame("woord");
//
//        GameService gameService = mock(GameService.class);
//        GameDTO gameDTO = gameService.generateGameDTO(game);
//
//        GameDTO expectedGameDTO = new GameDTO();
//        expectedGameDTO.score = game.getScore();
//        expectedGameDTO.gameState = game.getGameState();
//        expectedGameDTO.hint = game.getCurrentRound().getHint();
//
//        if (game.getCurrentRound().getGuesses().size() > 0) {
//            expectedGameDTO.marks = game.getCurrentRound().getGuesses().get(-1).getMarks();
//        }
//
//        assertEquals(expectedGameDTO, gameDTO);
//    }

}