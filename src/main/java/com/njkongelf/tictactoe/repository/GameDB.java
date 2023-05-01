package com.njkongelf.tictactoe.repository;

import com.njkongelf.tictactoe.entity.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
public class GameDB {
    private final Map<String, Game> gameMap = new HashMap<>();
    private static final GameDB gameDB = new GameDB();

    public static GameDB getInstanceOf() {
        return gameDB;
    }

}
