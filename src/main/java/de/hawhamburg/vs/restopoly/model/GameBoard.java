package de.hawhamburg.vs.restopoly.model;

import java.util.List;
import java.util.Map;

public class GameBoard {
    private int id;
    private List<Field> fields;
    private Map<String, Integer> positions;

    public GameBoard(int id, List<Field> fields, Map<String, Integer> positions) {
        this.id = id;
        this.fields = fields;
        //this.positions = positions;
    }

    public GameBoard() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /*public Map<String, Integer> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Integer> positions) {
        this.positions = positions;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
