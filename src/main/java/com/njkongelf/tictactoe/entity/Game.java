package com.njkongelf.tictactoe.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {
    private Player player1;
    private Player player2;
    private List<Player> playerList;
    private Player winningPlayer;
    private Player nextPlayerTurn;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.playerList=new ArrayList<>();
    }
}
