package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.application.exceptions.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.ClosedRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameNotStartedException;
import nl.hu.cisq1.lingo.words.data.WordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.*;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.ABSENT;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.WON;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private GameRepository gameRepository;

    private static final String RANDOM_WORD_5 = "groep";
    private static final String RANDOM_WORD_6 = "school";
    private static final String RANDOM_WORD_7 = "student";

    @Test
    @DisplayName("game gets saved in database when starting a new game")
    void gameGetsSavedWhenStartingGame() {
        GameDTO gameDTO = trainerService.startNewGame();

        assertDoesNotThrow(() -> trainerService.getGameInfo(gameDTO.gameId));
    }

    @Test
    @DisplayName("guessWord throws not found when game does not exist")
    void notFoundWhenGameDoesNotExistOnGuess() {
        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.guessWord(1L, "appel")
        );
    }

    @Test
    @DisplayName("geslaagd raden van een 5 letter woord")
    void geslaagdRadenVanEenVijfLetterWoord() {
        GameDTO gameDTO = trainerService.startNewGame();

        GameDTO guessDTO = trainerService.guessWord(gameDTO.gameId, "groep");

        assertEquals(WON, guessDTO.roundState);
    }

    @Test
    @DisplayName("Na 5 raadpogingen kan je niet meer raden")
    void naVijfRaadpogingenKanJeNietMeerRaden() {
        GameDTO gameDTO = trainerService.startNewGame();

        trainerService.guessWord(gameDTO.gameId, "rapen");
        trainerService.guessWord(gameDTO.gameId, "rapen");
        trainerService.guessWord(gameDTO.gameId, "rapen");
        trainerService.guessWord(gameDTO.gameId, "rapen");
        trainerService.guessWord(gameDTO.gameId, "rapen");

        assertThrows(
                ClosedRoundException.class,
                ()-> trainerService.guessWord(gameDTO.gameId, "rapen")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("game gets saved correctly while guessing")
    void gameGetsSavedCorrectlyWhileGuessing(String poging, List<Mark> marks) {
        GameDTO gameDTO = trainerService.startNewGame();

        trainerService.guessWord(gameDTO.gameId, poging);

        assertEquals(marks, trainerService.getGameInfo(gameDTO.gameId).marks);
    }

    public static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of("gegroet", List.of(INVALID, INVALID, INVALID, INVALID, INVALID)),
                Arguments.of("gerst", List.of(CORRECT, PRESENT, PRESENT, ABSENT, ABSENT)),
                Arguments.of("genen", List.of(CORRECT, ABSENT, ABSENT, CORRECT, ABSENT)),
                Arguments.of("gedoe", List.of(CORRECT, PRESENT, ABSENT, PRESENT, ABSENT)),
                Arguments.of("groep", List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT))
        );
    }

    @BeforeEach
    void loadTestData() {
        gameRepository.deleteAll();
        wordRepository.deleteAll();
        wordRepository.save(new Word(RANDOM_WORD_5));
        wordRepository.save(new Word(RANDOM_WORD_6));
        wordRepository.save(new Word(RANDOM_WORD_7));
    }

    @AfterEach
    void clearTestData() {
        gameRepository.deleteAll();
        wordRepository.deleteAll();
    }
}
