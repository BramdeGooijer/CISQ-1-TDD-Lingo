package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.application.exceptions.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    private WordService wordService;
    private GameRepository gameRepository;

    public TrainerService(WordService wordService, GameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public GameDTO startNewGame() {
        Game game = new Game();
        game.startGame(wordService.provideRandomWord(5));

        gameRepository.save(game);
        return generateGameDTO(game);
    }

    public GameDTO guessWord(Long gameId, String attempt) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("De game met dit id bestaat niet"));

        game.guessWord(attempt);

        gameRepository.save(game);
        return generateGameDTO(game);
    }

    public GameDTO startNewRound(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("De game met dit id bestaat niet"));

        if (game.getCurrentRound().getHint().length() == 7) {
            game.startRound(wordService.provideRandomWord(5));
        } else {
            game.startRound(wordService.provideRandomWord(game.getCurrentRound().getHint().length() + 1));
        }

        gameRepository.save(game);
        return generateGameDTO(game);
    }

    private GameDTO generateGameDTO(Game game) {
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
