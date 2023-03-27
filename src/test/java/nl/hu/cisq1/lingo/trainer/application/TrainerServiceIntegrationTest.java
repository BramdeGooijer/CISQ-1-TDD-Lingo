package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.words.data.WordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

//    @Test
    @DisplayName("geslaagd raden van een 5 letter woord")
    void geslaagdRadenVanEenVijfLetterWoord() {
        GameDTO gameDTO = trainerService.startNewGame();

        GameDTO guessDTO = trainerService.guessWord(gameDTO.gameId, "appel");

        System.out.println(guessDTO.hint);
    }
}
