package de.hawhamburg.vs.restopoly.model;

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
}
