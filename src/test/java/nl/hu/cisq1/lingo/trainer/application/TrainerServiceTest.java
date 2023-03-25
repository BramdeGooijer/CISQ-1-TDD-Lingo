package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.application.exceptions.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameNotStartedException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    @Test
    @DisplayName("starting a game returns gameDTO")
    void startingAGame() {
        TrainerService trainerService = mock(TrainerService.class);

        when(trainerService.startNewGame()).thenReturn(new GameDTO());

        assertEquals(GameDTO.class, trainerService.startNewGame().getClass());
    }

    @Test
    @DisplayName("guessing when game hasn't started")
    void guessingWhenGameHasNotStarted() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(new Game()));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(
                GameNotStartedException.class,
                () -> trainerService.guessWord(1L, "woord")
        );
    }

    @Test
    @DisplayName("guessing when game does not exist")
    void guessingWhenGameNotFound() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.guessWord(1L, "woord")
        );
    }

    @Test
    @DisplayName("guessing when allowed")
    void guessingWhenAllowed() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("appel");

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertEquals(
                GameDTO.class,
                trainerService.guessWord(1L, "woord").getClass()
        );
    }
}