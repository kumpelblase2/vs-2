package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class GameBoard {
    private List<Field> fields;
    private Map<String, Integer> positions;
    @JsonIgnore
    private Map<String, Player> players;
    private Components components;

    public GameBoard(List<Field> fields, Map<String, Integer> positions, Components components) {
        this.fields = fields;
        this.positions = positions;
        this.components = components;
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

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public static class Player {
        private String id;
        private String uri;
        private String place;
        private int position;
        private String move;
        private String roll;

        public Player(String id, String place, int position, String uri, String move, String roll) {
            this.id = id;
            this.place = place;
            this.position = position;
            this.uri = uri;
            this.move = move;
            this.roll = roll;
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

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getMove() {
            return move;
        }

        public void setMove(String move) {
            this.move = move;
        }

        public String getRoll() {
            return roll;
        }

        public void setRoll(String roll) {
            this.roll = roll;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Player player = (Player) o;

            if (position != player.position) return false;
            if (id != null ? !id.equals(player.id) : player.id != null) return false;
            if (uri != null ? !uri.equals(player.uri) : player.uri != null) return false;
            if (place != null ? !place.equals(player.place) : player.place != null) return false;
            if (move != null ? !move.equals(player.move) : player.move != null) return false;
            return roll != null ? roll.equals(player.roll) : player.roll == null;

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (uri != null ? uri.hashCode() : 0);
            result = 31 * result + (place != null ? place.hashCode() : 0);
            result = 31 * result + position;
            result = 31 * result + (move != null ? move.hashCode() : 0);
            result = 31 * result + (roll != null ? roll.hashCode() : 0);
            return result;
        }
    }
}
