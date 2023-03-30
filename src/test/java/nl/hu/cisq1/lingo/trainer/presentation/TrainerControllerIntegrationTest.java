package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.*;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.CORRECT;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.ACTIVE;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Status.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    GameRepository gameRepository;

    private static final String RANDOM_WORD_5 = "groep";
    private static final String RANDOM_WORD_6 = "school";
    private static final String RANDOM_WORD_7 = "student";

    @Test
    @DisplayName("getGameInfo should throw response not found when game not found")
    void rejectGetGameInfoWhenGameNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/lingo/game/%s", 9999999L));

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("get correct game info through controller")
    void getCorrectGameInfoThroughController() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder createGame = MockMvcRequestBuilders
                .post("/lingo/game");

        MvcResult getResult = mockMvc.perform(createGame).andExpect(status().isOk()).andReturn();
        String responseBody = getResult.getResponse().getContentAsString();
        GameDTO getResultDTO = objectMapper.readValue(responseBody, GameDTO.class);


        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/lingo/game/%s", getResultDTO.gameId));

        MvcResult postResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        String postResponseBody = postResult.getResponse().getContentAsString();
        GameDTO postResultDTO = objectMapper.readValue(postResponseBody, GameDTO.class);

        assertEquals(getResultDTO.gameId, postResultDTO.gameId);
        assertEquals(getResultDTO.score, postResultDTO.score);
        assertEquals(getResultDTO.gameState, postResultDTO.gameState);
        assertEquals(getResultDTO.currentRoundId, postResultDTO.currentRoundId);
        assertEquals(getResultDTO.roundState, postResultDTO.roundState);
        assertEquals(getResultDTO.hint, postResultDTO.hint);
        assertEquals(getResultDTO.marks, postResultDTO.marks);
    }

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("get correct feedback when guessing through controller")
    void guessThroughControllerAssertCorrectFeedback(String poging, List<Mark> marks) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder createGame = MockMvcRequestBuilders.post("/lingo/game");

        MvcResult getResult = mockMvc.perform(createGame).andExpect(status().isOk()).andReturn();
        String responseBody = getResult.getResponse().getContentAsString();
        GameDTO getResultDTO = objectMapper.readValue(responseBody, GameDTO.class);

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"gameId\": \"%s\", \"attempt\": \"%s\"}", getResultDTO.gameId, poging));

        MvcResult postResult = mockMvc.perform(guessRequest).andExpect(status().isOk()).andReturn();
        String postResponseBody = postResult.getResponse().getContentAsString();
        GameDTO postResultDTO = objectMapper.readValue(postResponseBody, GameDTO.class);

        assertEquals(marks, postResultDTO.marks);
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

    @Test
    @DisplayName("starting a round when possible")
    void startingARoundWhenPossible() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder createGame = MockMvcRequestBuilders.post("/lingo/game");

        MvcResult getResult = mockMvc.perform(createGame).andExpect(status().isOk()).andReturn();
        String responseBody = getResult.getResponse().getContentAsString();
        GameDTO getResultDTO = objectMapper.readValue(responseBody, GameDTO.class);

        assertEquals(ACTIVE, getResultDTO.roundState);

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"gameId\": \"%s\", \"attempt\": \"%s\"}", getResultDTO.gameId, "groep"));

        MvcResult guessResult = mockMvc.perform(guessRequest).andExpect(status().isOk()).andReturn();
        String guessResponseBody = guessResult.getResponse().getContentAsString();
        GameDTO guessResultDTO = objectMapper.readValue(guessResponseBody, GameDTO.class);

        assertEquals(WON, guessResultDTO.roundState);

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/lingo/round/%s", getResultDTO.gameId));

        MvcResult postResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        String postResponseBody = postResult.getResponse().getContentAsString();
        GameDTO postResultDTO = objectMapper.readValue(postResponseBody, GameDTO.class);

        assertEquals(ACTIVE, postResultDTO.roundState);
    }

    @Test
    @DisplayName("starting a round should throw bad request when round still active")
    void startingARoundWhenRoundStillActive() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder createGame = MockMvcRequestBuilders.post("/lingo/game");

        MvcResult getResult = mockMvc.perform(createGame).andExpect(status().isOk()).andReturn();
        String responseBody = getResult.getResponse().getContentAsString();
        GameDTO getResultDTO = objectMapper.readValue(responseBody, GameDTO.class);

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/lingo/round/%s", getResultDTO.gameId));

        mockMvc.perform(request).andExpect(status().isBadRequest());
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
