package com.njkongelf.tictactoe.json.responses;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.njkongelf.tictactoe.json.GameResponse;
import lombok.Data;

@Data
public class GameInit implements GameResponse {
    @JsonPropertyDescription(value = "Game Id")
    private String gameId;
}
