package nl.hu.cisq1.lingo.trainer.presentation.dto;

import jakarta.validation.constraints.NotNull;

public class GameRequestDTO {
    @NotNull
    public Long gameId;
    @NotNull
    public String attempt;
}
