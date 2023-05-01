package com.njkongelf.tictactoe.controller;

import com.njkongelf.tictactoe.json.GameResponse;
import com.njkongelf.tictactoe.json.requests.GameMove;
import com.njkongelf.tictactoe.json.requests.GameSetup;
import com.njkongelf.tictactoe.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class GameController {

    @Autowired
    private GameService service;

    @PostMapping("/game")
    public ResponseEntity<GameResponse> setupGame(@RequestBody GameSetup gameSetup) {
        log.info("Setting up game for : "+gameSetup.getName());
        return service.setupGame(gameSetup);

    }

    @PutMapping("/{id}/join")
    public ResponseEntity<GameResponse> joinGame(@PathVariable("id") String id,@RequestBody GameSetup gameSetup){
        log.info(gameSetup.getName()+" trying to join game "+id);
        return service.joinGame(id, gameSetup);
    }
    @PutMapping("/{id}/move")
    public ResponseEntity<GameResponse> moveInGame(@PathVariable("id") String id,@RequestBody GameMove move){
        return service.moveInGame(id,move);
    }
}
