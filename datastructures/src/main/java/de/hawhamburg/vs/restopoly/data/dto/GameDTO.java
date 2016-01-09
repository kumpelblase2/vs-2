package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Components;
import de.hawhamburg.vs.restopoly.data.model.Game;

public class GameDTO {
    private int gameid;
    private Components components;
    private String players;
    private boolean started = false;

    public GameDTO(int gameid, Components components, String players, boolean started) {
        this.gameid = gameid;
        this.components = components;
        this.players = players;
        this.started = started;
    }

    public GameDTO() {
    }

    public GameDTO(Game current) {
        this.gameid = current.getGameid();
        this.components = current.getComponents();
        this.players = "/games/" + this.gameid + "/players";
        this.started = current.isStarted();
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

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
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

        GameDTO gameDTO = (GameDTO) o;

        if (gameid != gameDTO.gameid) return false;
        if (started != gameDTO.started) return false;
        if (components != null ? !components.equals(gameDTO.components) : gameDTO.components != null) return false;
        return players != null ? players.equals(gameDTO.players) : gameDTO.players == null;

    }

    @Override
    public int hashCode() {
        int result = gameid;
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (started ? 1 : 0);
        return result;
    }
}
