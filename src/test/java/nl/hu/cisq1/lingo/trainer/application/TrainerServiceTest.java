package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.application.exceptions.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameNotStartedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundStillActiveException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.*;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    @Test
    @DisplayName("starting a new game gives back a GameDTO")
    void startingGameReturnsGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertEquals(GameDTO.class, trainerService.startNewGame().getClass());
    }

    @Test
    @DisplayName("starting a new game generates correct GameDTO")
    void startingGameGeneratesCorrectGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        GameDTO gameDTO = trainerService.startNewGame();

        assertNull(gameDTO.gameId);
        assertEquals(0, gameDTO.score);
        assertEquals(ACTIVE, gameDTO.gameState);
        assertNull(gameDTO.currentRoundId);
        assertEquals("a....", gameDTO.hint);
        assertNull(gameDTO.marks);
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

    @Test
    @DisplayName("guessing generates correct GameDTO")
    void guessingGeneratesCorrectGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("appel");

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        GameDTO gameDTO = trainerService.guessWord(1L, "appie");

        assertNull(gameDTO.gameId);
        assertEquals(0, gameDTO.score);
        assertEquals(ACTIVE, gameDTO.gameState);
        assertNull(gameDTO.currentRoundId);
        assertEquals("app..", gameDTO.hint);
        assertEquals(List.of(CORRECT, CORRECT, CORRECT, ABSENT, PRESENT), gameDTO.marks);
    }

    @Test
    @DisplayName("starting round when game does not exist")
    void startinRoundWhenGameDoesNotExist() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(GameNotFoundException.class,
                () -> trainerService.startNewRound(1L));
    }

    @Test
    @DisplayName("starting round when game not started")
    void startingRoundWhenGameNotStarted() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(new Game()));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(
                GameNotStartedException.class,
                () -> trainerService.startNewRound(1L)
        );
    }

    @Test
    @DisplayName("starting round when round still active")
    void startingRoundWhenRoundStillActive() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();
        game.startGame("woord");

        when(wordService.provideRandomWord(anyInt())).thenReturn("appel");
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(
                RoundStillActiveException.class,
                () -> trainerService.startNewRound(1L));
    }

    @ParameterizedTest
    @MethodSource("provideGameInfo")
    @DisplayName("getting word with correct length through rounds")
    void correctWordLengthThroughRounds(int round, int expectedWordLength, String wordToGuess) {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        Game game = new Game();

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        when(wordService.provideRandomWord(5)).thenReturn(wordToGuess);
        when(wordService.provideRandomWord(6)).thenReturn(wordToGuess);
        when(wordService.provideRandomWord(7)).thenReturn(wordToGuess);

        TrainerService trainerService = new TrainerService(wordService, gameRepository);


        game.startGame(wordService.provideRandomWord(5));
        trainerService.guessWord(1L, wordToGuess);

        GameDTO gameDTO = new GameDTO();

        for (int i = 0; i < round; i++) {
            gameDTO = trainerService.startNewRound(1L);
            trainerService.guessWord(1L, wordToGuess);
        }

        assertEquals(expectedWordLength, gameDTO.hint.length());
    }

    public static Stream<Arguments> provideGameInfo() {
        return Stream.of(
                Arguments.of(1, 5, "appel"),
                Arguments.of(2, 6, "appels"),
                Arguments.of(3, 7, "kikkers"),
                Arguments.of(4, 5, "appel"),
                Arguments.of(5, 6, "appels"),
                Arguments.of(6, 7, "kikkers"),
                Arguments.of(7, 5, "appel")
        );
    }

    @Test
    @DisplayName("getGameInfo gives back GameDTO")
    void getGameInfoReturnsGameDTO() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(new Game()));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertEquals(GameDTO.class, trainerService.getGameInfo(1L).getClass());
    }

    @Test
    @DisplayName("getGameInfoWhenGameDoesNotExist")
    void getGameInfoWhenGameDoesNotExist() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(GameNotFoundException.class,
                () -> trainerService.getGameInfo(1L));
    }
}