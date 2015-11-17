package de.hawhamburg.vs.restopoly.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard {
    private List<Field> fields;
    private Map<String, Integer> positions;

    public GameBoard(List<Field> fields, Map<String, Integer> positions) {
        this.fields = fields;
        //this.positions = positions;
    }

    public GameBoard() {
        this.positions = new HashMap<>();
        this.fields = new ArrayList<>();
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Integer> positions) {
        this.positions = positions;
    }

    public void movePlayer(String playerid, int i) {
        this.positions.put(playerid, i);
    }
}
