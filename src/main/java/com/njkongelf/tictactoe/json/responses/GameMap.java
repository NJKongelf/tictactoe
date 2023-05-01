package com.njkongelf.tictactoe.json.responses;

import com.njkongelf.tictactoe.json.GameResponse;
import com.njkongelf.tictactoe.util.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameMap implements GameResponse {
    private String message;
    private List<Point> pieceX;
    private List<Point> pieceO;
}
