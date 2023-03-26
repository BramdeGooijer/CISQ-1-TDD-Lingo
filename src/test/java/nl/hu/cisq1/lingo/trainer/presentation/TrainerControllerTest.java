package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerControllerTest {
    @Test
    @DisplayName("getGameInfo returns GameDTO")
    void getGameInfoReturnsGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(new Game()));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertEquals(GameDTO.class, trainerController.getGameInfo(1L).getClass());
    }

    @Test
    @DisplayName("getGameInfo returns not found when game does not exist")
    void getGameInfoReturnsNotFound() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.getGameInfo(1L)
        );
    }

    @Test
    @DisplayName("startNewGame returns GameDTO")
    void startNewGameReturnsGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(new Game()));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertEquals(GameDTO.class, trainerController.startNewGame().getClass());
    }

    @Test
    @DisplayName("guessWord returns GameDTO")
    void guessWordReturnsGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("appel");

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);


        assertEquals(GameDTO.class, trainerController.guessWord(1L, "appel").getClass());
    }

    @Test
    @DisplayName("guessWord returns not found on Game not found")
    void guessWordReturnsNotFoundWhenGameNotFound() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.guessWord(1L, "appel")
        );
    }

    @Test
    @DisplayName("guessWord returns bad request on GameNotStarted")
    void guessWordReturnsBadRequestOnGameNotStarted() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.guessWord(1L, "appel")
        );
    }

    @Test
    @DisplayName("guessWord returns bad request on Closed round")
    void guessWordReturnsBadRequestOnClosedRound() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("appel");
        game.guessWord("appel");

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.guessWord(1L, "appel")
        );
    }

    @Test
    @DisplayName("startNewRound returns gameDTO")
    void startNewRoundReturnsGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("appel");
        game.guessWord("appel");

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertEquals(GameDTO.class, trainerController.startNewRound(1L).getClass());
    }

    @Test
    @DisplayName("startNewRound returns not found on when game does not exist")
    void startNewRoundReturnsNotFoundWhenGameDoesNotExist() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.startNewRound(1L)
        );
    }

    @Test
    @DisplayName("startNewRound returns bad request when game not started")
    void startNewRoundReturnsBadRequestOnGameNotStarted() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.startNewRound(1L)
        );
    }

    @Test
    @DisplayName("startNewRound returns bad request when round still active")
    void startNewRoundReturnsBadRequestOnRoundStillActive() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("appel");

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);
        TrainerController trainerController = new TrainerController(trainerService);

        assertThrows(
                ResponseStatusException.class,
                () -> trainerController.startNewRound(1L)
        );
    }
}