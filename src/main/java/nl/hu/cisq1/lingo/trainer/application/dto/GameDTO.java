package nl.hu.cisq1.lingo.trainer.application.dto;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;

import java.util.List;

public class GameDTO {
    public Long gameId;
    public int score;
    public Status gameState;

    public Long currentRoundId;
    public Status roundState;
    public String hint;
    public List<Mark> marks;
}
