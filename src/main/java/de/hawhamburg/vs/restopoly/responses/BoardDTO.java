package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.GameBoard;

/**
 * Created by tim on 10.11.15.
 */
public class BoardDTO {
    public PlayerDTO player;
    public GameBoard board;

    public BoardDTO(PlayerDTO player, GameBoard board) {
        this.player = player;
        this.board = board;
    }
}
