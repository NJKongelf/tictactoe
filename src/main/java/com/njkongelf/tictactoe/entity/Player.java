package com.njkongelf.tictactoe.entity;

import com.njkongelf.tictactoe.util.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Player {
    private String username;
    private boolean computerplayer;
    private final String piece;
    private List<Point> moves;

    public Player(String username,String piece){
        this.username=username;
        this.piece=piece;
        this.moves=new ArrayList<>();
    }
}
