package com.njkongelf.tictactoe.service;


import com.njkongelf.tictactoe.json.GameResponse;
import com.njkongelf.tictactoe.json.requests.GameMove;
import com.njkongelf.tictactoe.json.requests.GameSetup;
import org.springframework.http.ResponseEntity;

public interface GameService {

    ResponseEntity<GameResponse> setupGame(GameSetup gameSetup);
    ResponseEntity<GameResponse> joinGame(String id,GameSetup gameSetup);
    ResponseEntity<GameResponse> moveInGame(String id, GameMove gameMove);

}
