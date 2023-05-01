package com.njkongelf.tictactoe.json.responses;

import com.njkongelf.tictactoe.json.GameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameErrorResponse implements GameResponse {
    private String message;
}
