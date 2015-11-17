package de.hawhamburg.vs.restopoly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard {
    private List<Field> fields;
    private Map<String, Integer> positions;
    @JsonIgnore
    private Map<String, Player> players;

    public GameBoard(List<Field> fields, Map<String, Integer> positions) {
        this.fields = fields;
        this.positions = positions;
        this.players = new HashMap<>();
    }

    public GameBoard() {
        this.positions = new HashMap<>();
        this.fields = new ArrayList<>();
        this.players = new HashMap<>();
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
        int currentPosition = this.positions.get(playerid);
        this.fields.get(currentPosition).removePlayer(playerid);
        int nextPosition = (currentPosition + i) % this.fields.size();
        Player player = this.players.get(playerid);
        player.setPosition(nextPosition);
        this.fields.get(nextPosition).getPlayers().add(player);
        this.positions.put(playerid, nextPosition);
    }

    public void addPlayer(Player player) {
        this.positions.putIfAbsent(player.getId(), 0);
        this.players.putIfAbsent(player.getId(), player);
        this.movePlayer(player.getId(), 0);
    }

    public void removePlayer(String playerid) {
        int position = this.positions.remove(playerid);
        this.fields.get(position).removePlayer(playerid);
    }
}
