package com.njkongelf.tictactoe.json.responses;

import com.njkongelf.tictactoe.json.GameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamePlayerJoined implements GameResponse {
    private String name;
    private String piece;
    private String message;
}
