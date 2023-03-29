package nl.hu.cisq1.lingo.trainer.presentation;

import jakarta.websocket.server.PathParam;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.application.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.application.exceptions.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.ClosedRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameNotStartedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundStillActiveException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("lingo")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/game/{id}")
    public GameDTO getGameInfo(@PathVariable Long id) {
        try {
            return trainerService.getGameInfo(id);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }

    @PostMapping("/game")
    public GameDTO startNewGame() {
        return trainerService.startNewGame();
    }

    @PostMapping("/guess")
    public GameDTO guessWord(@RequestBody GameRequestDTO gameRequestDTO) {
        try {
            return trainerService.guessWord(gameRequestDTO.gameId, gameRequestDTO.attempt);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        } catch (GameNotStartedException | ClosedRoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @PostMapping("/round/{id}")
    public GameDTO startNewRound(@PathVariable Long id) {
        try {
            return trainerService.startNewRound(id);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        } catch (GameNotStartedException | RoundStillActiveException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

}
