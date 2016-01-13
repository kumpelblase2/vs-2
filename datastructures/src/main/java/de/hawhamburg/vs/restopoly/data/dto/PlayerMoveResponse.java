package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.GameBoard;

public class PlayerMoveResponse {
    private GameBoard.Player player;
    private GameBoardDTO board;

    public PlayerMoveResponse(GameBoard.Player player, GameBoardDTO board) {
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

    public GameBoardDTO getBoard() {
        return board;
    }

    public void setBoard(GameBoardDTO board) {
        this.board = board;
    }
}
