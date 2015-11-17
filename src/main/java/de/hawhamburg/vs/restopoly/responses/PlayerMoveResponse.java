package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.GameBoard;
import de.hawhamburg.vs.restopoly.model.Player;

public class PlayerMoveResponse {
    private Player player;
    private GameBoard board;

    public PlayerMoveResponse(Player player, GameBoard board) {
        this.player = player;
        this.board = board;
    }

    public PlayerMoveResponse() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }
}
