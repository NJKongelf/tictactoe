package com.njkongelf.tictactoe.json.requests;

import lombok.Data;

@Data
public class GameMove {
    private String piece;
    private int row;
    private int column;

}
