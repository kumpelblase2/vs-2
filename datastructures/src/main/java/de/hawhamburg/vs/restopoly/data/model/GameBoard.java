package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

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
        if(currentPosition > 0 && currentPosition < this.fields.size()) {
            this.fields.get(currentPosition).removePlayer(playerid);
            int nextPosition = (currentPosition + i) % this.fields.size();
            Player player = this.players.get(playerid);
            player.setPosition(nextPosition);
            if(nextPosition > 0 && nextPosition < this.fields.size())
                this.fields.get(nextPosition).getPlayers().add(player);

            this.positions.put(playerid, nextPosition);
        }
    }

    public void addPlayer(Player player) {
        this.positions.putIfAbsent(player.getId(), 0);
        this.players.putIfAbsent(player.getId(), player);
        this.fields.get(0).getPlayers().add(player);
        this.movePlayer(player.getId(), 0);
    }

    public void removePlayer(String playerid) {
        int position = this.positions.remove(playerid);
        this.fields.get(position).removePlayer(playerid);
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public Player getPlayer(String playerid) {
        return this.players.get(playerid);
    }

    public static class Player {
        private String id;
        private String place;
        private int position;

        public Player(String id, String place, int position) {
            this.id = id;
            this.place = place;
            this.position = position;
        }

        public Player() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Player player = (Player) o;

            if (position != player.position) return false;
            if (id != null ? !id.equals(player.id) : player.id != null) return false;
            return place != null ? place.equals(player.place) : player.place == null;

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (place != null ? place.hashCode() : 0);
            result = 31 * result + position;
            return result;
        }
    }
}
