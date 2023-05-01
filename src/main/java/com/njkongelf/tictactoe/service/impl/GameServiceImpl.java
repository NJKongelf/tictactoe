package com.njkongelf.tictactoe.service.impl;

import com.njkongelf.tictactoe.entity.Game;
import com.njkongelf.tictactoe.entity.Player;
import com.njkongelf.tictactoe.enums.PieceEnum;
import com.njkongelf.tictactoe.json.GameResponse;
import com.njkongelf.tictactoe.json.requests.GameMove;
import com.njkongelf.tictactoe.json.requests.GameSetup;
import com.njkongelf.tictactoe.json.responses.*;
import com.njkongelf.tictactoe.repository.GameDB;
import com.njkongelf.tictactoe.service.GameService;
import com.njkongelf.tictactoe.util.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameDB repository;


    @Override
    public ResponseEntity<GameResponse> setupGame(GameSetup gameSetup) {
        if (checkPlayerPiece(gameSetup))
            return ResponseEntity.badRequest().body(new GameErrorResponse("Please use letters 'x' or 'o' as piece"));
        GameInit gameInit = new GameInit();
        gameInit.setGameId(String.valueOf(UUID.randomUUID()));
        Player playerOne = new Player(gameSetup.getName(), gameSetup.getPiece().toUpperCase());
        Player playerTwo = setupPlayerTwo(gameSetup, true);
        playerTwo.setUsername("Computer");
        repository.getGameMap().put(gameInit.getGameId(), new Game(playerOne, playerTwo));
        return ResponseEntity.ok(gameInit);
    }

    @Override
    public ResponseEntity<GameResponse> joinGame(String id, GameSetup gameSetup) {
        Optional<Game> game = Optional.ofNullable(repository.getGameMap().get(id));
        if (game.isPresent()) {
            Game gameplay = game.get();
            if (game.get().getPlayerList().isEmpty()) {
                Player p1 = gameplay.getPlayer1();
                Player p2 = setupPlayerTwo(p1);
                p2.setUsername(gameSetup.getName());
                gameplay.setPlayer2(p2);
                startGame(gameplay);
                return ResponseEntity.ok(new GamePlayerJoined(p2.getUsername(), p2.getPiece(), "Two player game set! " + p1.getPiece() + " makes first move"));
            } else {
                return ResponseEntity.badRequest().body(new GameErrorResponse("Game already started"));
            }
        }
        return ResponseEntity.badRequest().body(new GameErrorResponse("No Game with id: " + id));
    }

    @Override
    public ResponseEntity<GameResponse> moveInGame(String id, GameMove gameMove) {
        Optional<Game> gamePlay = Optional.ofNullable(repository.getGameMap().get(id));
        if (gamePlay.isPresent()) {
            Game game = gamePlay.get();
            startGameIfNotStarted(game);
            Point point = new Point(gameMove.getRow(), gameMove.getColumn());
            if (moveAlreadyDone(point, game))
                return ResponseEntity.badRequest().body(new GameErrorResponse("Move already done"));
            Optional<Player> player = getPlayerByPiece(gameMove.getPiece().toUpperCase(), game);
            if (player.isPresent()) {
                Optional<ResponseEntity<GameResponse>> playerMoved = playerMove(game, player.get(), point, gameMove);
                if (playerMoved.isPresent())
                    return playerMoved.get();
                return ResponseEntity.ok(
                        new GameMap("Current status of game",
                                playersMovesList(PieceEnum.X.toString(), game),
                                playersMovesList(PieceEnum.O.toString(), game)));
            }
        }
        return ResponseEntity.badRequest().body(new GameErrorResponse("No game with id:" + id));
    }


    private Optional<ResponseEntity<GameResponse>> playerMove(Game game, Player player, Point playerPoint, GameMove move) {
        if (!game.getNextPlayerTurn().equals(player))
            return Optional.of(ResponseEntity.badRequest().body(new GameErrorResponse("Wrong players turn!")));
        player.getMoves().add(playerPoint);
        if (didGameEnd(game, player).isPresent())
            return Optional.of(didGameEnd(game, player).get());
        if (game.getPlayer2().isComputerplayer()) {
            game.getPlayer2().getMoves().add(computerMove(game));
            game.setNextPlayerTurn(game.getPlayer1());
            if (didGameEnd(game, game.getPlayer2()).isPresent()) {
                return Optional.of(didGameEnd(game, game.getPlayer2()).get());
            }
        } else {
            nextPlayersTurn(game, move);
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<GameResponse>> didGameEnd(Game game, Player player) {
        if (moveEndsInWin(game))
            return Optional.of(ResponseEntity.ok(new GameAnnouncement(player.getUsername() + " made a winning move")));
        if (gameFinnished(game))
            return Optional.of(ResponseEntity.ok(new GameAnnouncement("GAME OVER! No more moves possible")));
        return Optional.empty();
    }

    private Optional<Player> getPlayerByPiece(String piece, Game game) {
        for (Player p : game.getPlayerList()) {
            if (p.getPiece().equals(piece))
                return Optional.of(p);
        }
        return Optional.empty();
    }

    private boolean gameFinnished(Game game) {
        int moves = 0;
        for (Player p : game.getPlayerList()) {
            moves = moves + p.getMoves().size();
        }
        return moves >= 8;
    }

    private void nextPlayersTurn(Game game, GameMove move) {
        for (Player p : game.getPlayerList()) {
            if (!(p.getPiece().equalsIgnoreCase(move.getPiece())))
                game.setNextPlayerTurn(p);
        }
    }

    private List<Point> playersMovesList(String piece, Game game) {
        for (Player p : game.getPlayerList()) {
            if (p.getPiece().equalsIgnoreCase(piece))
                return p.getMoves();
        }
        return new ArrayList<>();
    }

    private Player setupPlayerTwo(GameSetup gameSetup, boolean computerplayer) {
        Player playerTwo = switch (gameSetup.getPiece().toUpperCase()) {
            case "X" -> new Player("Player2", PieceEnum.O.toString());
            case "O" -> new Player("Player2", PieceEnum.X.toString());
            default -> throw new IllegalStateException();
        };
        playerTwo.setComputerplayer(computerplayer);
        return playerTwo;
    }

    private Player setupPlayerTwo(Player player) {
        GameSetup gameSetup = new GameSetup();
        gameSetup.setName(player.getUsername());
        gameSetup.setPiece(player.getPiece());
        return setupPlayerTwo(gameSetup, false);
    }

    private boolean checkPlayerPiece(GameSetup setup) {
        return (!"X".equalsIgnoreCase(setup.getPiece()) && !"O".equalsIgnoreCase(setup.getPiece()));
    }

    private boolean moveEndsInWin(Game game) {
        for (Player pl : game.getPlayerList()) {
            if (pl.getMoves().size() >= 2) {
                boolean row = rowWin(pl.getMoves());
                boolean collum = collumWin(pl.getMoves());
                boolean diagonal = diagonalWin(pl.getMoves());
                if (row || collum || diagonal) {
                    game.setWinningPlayer(pl);
                    return true;
                }
            }
        }
        return false;
    }

    private void startGame(Game game) {
        List<Player> players = new ArrayList<>();
        players.add(game.getPlayer1());
        players.add(game.getPlayer2());
        game.setPlayerList(players);
        game.setNextPlayerTurn(game.getPlayer1());
    }

    private void startGameIfNotStarted(Game game) {
        if (game.getPlayerList().isEmpty()) {
            startGame(game);
        }
    }

    private boolean rowWin(List<Point> moves) {
        long inRow;
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            inRow = moves.stream().filter(point -> point.getRow() == finalI).count();
            if (inRow > 2)
                return true;
        }
        return false;
    }

    private boolean collumWin(List<Point> moves) {
        long inRow;
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            inRow = moves.stream().filter(point -> point.getColumn() == finalI).count();
            if (inRow > 2)
                return true;
        }
        return false;
    }

    private boolean diagonalWin(List<Point> moves) {
        List<Point> points1 = new ArrayList<>();
        List<Point> points2 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            Optional<Point> p = moves.stream().filter(point -> point.getRow() == finalI && point.getColumn() == finalI).findFirst();
            p.ifPresent(points1::add);
        }
        for (int i = 3; i >= 1; i--) {
            int finalI = i;
            Optional<Point> p = moves.stream().filter(point -> point.getRow() == finalI && point.getColumn() == finalI).findFirst();
            p.ifPresent(points2::add);
        }
        return points1.size() == 3 || points2.size() == 3;
    }

    private boolean moveAlreadyDone(Point point, Game game) {
        for (Player p : game.getPlayerList()) {
            for (Point points : p.getMoves()) {
                if (points.equals(point))
                    return true;
            }
        }
        return false;
    }

    private Point computerMove(Game game) {
        List<Point> mapOfPoints = new ArrayList<>();
        List<Point> possiblePoints = new ArrayList<>();
        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y <= 3; y++) {
                mapOfPoints.add(new Point(x, y));
            }
        }
        for (Point p : mapOfPoints) {
            if (!moveAlreadyDone(p, game))
                possiblePoints.add(p);
        }
        Random random = new Random();
        return possiblePoints.get(random.nextInt(0, possiblePoints.size()));
    }
}