package de.hawhamburg.vs.restopoly.model;

public class Components {
    private GameBoard board;

    public Components(GameBoard board) {
        this.board = board;
    }

    public Components() {
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }
}
