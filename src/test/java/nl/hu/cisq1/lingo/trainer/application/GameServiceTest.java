package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class GameServiceTest {
    @Test
    @DisplayName("starting a game")
    void startingAGame() {
        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        when(wordService.provideRandomWord(5)).thenReturn("appel");

        verify(wordService, times(1)).provideRandomWord(5);
    }

}