package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Game {
    private int gameid;
    @JsonIgnore
    private Components components;
    private List<Player> players;
    @JsonIgnore
    private Player currentPlayer;
    @JsonIgnore
    private int currentPlayerNumber = 0;
    @JsonIgnore
    private boolean mutexAcquired = false;
    private boolean started = false;

    public Game(int gameid, Components components, List<Player> players) {
        this.gameid = gameid;
        this.components = components;
        this.players = players;
    }

    public Game() {
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayerNumber = currentPlayer;
        this.currentPlayer = this.players.get(currentPlayer);
    }

    public boolean isMutexAcquired() {
        return mutexAcquired;
    }

    public void setMutexAcquired(boolean mutexAcquired) {
        this.mutexAcquired = mutexAcquired;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentPlayerNumber = this.players.indexOf(currentPlayer);
        this.mutexAcquired = false;
    }

    public boolean canStart() {
        return this.getPlayers().stream().allMatch(Player::isReady);
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (gameid != game.gameid) return false;
        if (components != null ? !components.equals(game.components) : game.components != null) return false;
        return !(players != null ? !players.equals(game.players) : game.players != null);

    }

    @Override
    public int hashCode() {
        int result = gameid;
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }

    public void nextPlayer() {
        this.setCurrentPlayer(this.currentPlayerNumber + 1 % this.players.size());
    }

    public boolean hasPlayer(String inId) {
        return this.players.stream().anyMatch(player -> player.getId().equals(inId));
    }

    public void start() {
        this.started = true;
        this.players.stream().forEach(pl -> pl.setReady(false));
        this.currentPlayer = this.players.get(this.currentPlayerNumber);
    }

    public static class Player {
        private String id;
        private String name;
        private String uri;
        private boolean ready;

        public Player(String id, String name, String uri, boolean ready) {
            this.id = id;
            this.name = name;
            this.uri = uri;
            this.ready = ready;
        }

        public Player(String id) {
            this.id = id;
        }

        public Player() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isReady() {
            return ready;
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Player player = (Player) o;

            if (id != null ? !id.equals(player.id) : player.id != null) return false;
            return !(name != null ? !name.equals(player.name) : player.name != null);

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }
}
