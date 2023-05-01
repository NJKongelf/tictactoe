package com.njkongelf.tictactoe.json.requests;

import lombok.Data;

@Data
public class GameSetup {
    private String name;
    private boolean computerplayer;
    private String piece;
}
