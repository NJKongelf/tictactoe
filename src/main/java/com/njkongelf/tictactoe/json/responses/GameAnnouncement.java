package com.njkongelf.tictactoe.json.responses;

import com.njkongelf.tictactoe.json.GameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameAnnouncement implements GameResponse {
    private String announcement;
}
