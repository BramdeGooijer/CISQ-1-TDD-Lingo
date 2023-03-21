package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private WordService wordService;
    private GameRepository gameRepository;

    public GameService(WordService wordService, GameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public GameDTO startNewGame() {
        Game game = new Game();
        game.startGame(wordService.provideRandomWord(5));

        return generateGameDTO(game);
    }

    public GameDTO guessWord() {

        return null;
    }

    public GameDTO startNewRound() {

        return null;
    }

    public GameDTO generateGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();

        gameDTO.gameId = game.getId();
        gameDTO.score = game.getScore();
        gameDTO.gameState = game.getGameState();
        gameDTO.currentRoundId = game.getCurrentRound().getId();
        gameDTO.hint = game.getCurrentRound().getHint();

        if (game.getCurrentRound().getGuesses().size() > 0) {
            gameDTO.marks = game.getCurrentRound().getGuesses().get(-1).getMarks();
        }

        return gameDTO;
    }
}
