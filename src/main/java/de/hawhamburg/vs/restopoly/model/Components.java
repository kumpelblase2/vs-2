package de.hawhamburg.vs.restopoly.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class Components {
    @OneToOne(cascade = CascadeType.ALL)
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
