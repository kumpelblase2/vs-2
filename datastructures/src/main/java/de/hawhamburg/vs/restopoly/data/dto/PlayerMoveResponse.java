package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.GameBoard;

public class PlayerMoveResponse {
    private GameBoard.Player player;
    private GameBoard board;

    public PlayerMoveResponse(GameBoard.Player player, GameBoard board) {
        this.player = player;
        this.board = board;
    }

    public PlayerMoveResponse() {
    }

    public GameBoard.Player getPlayer() {
        return player;
    }

    public void setPlayer(GameBoard.Player player) {
        this.player = player;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }
}
