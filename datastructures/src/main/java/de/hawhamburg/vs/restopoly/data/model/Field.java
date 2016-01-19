package de.hawhamburg.vs.restopoly.data.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private String place;
    private List<GameBoard.Player> players;
    private String broker;

    public Field(String place, List<GameBoard.Player> players, String broker) {
        this.place = place;
        this.players = players;
        this.broker = broker;
    }

    public Field(PlaceConfig place) {
        this(place.getName(), new ArrayList<>(), "");
    }

    public Field() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<GameBoard.Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<GameBoard.Player> players) {
        this.players = players;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (getPlace() != null ? !getPlace().equals(field.getPlace()) : field.getPlace() != null) return false;
        return !(getPlayers() != null ? !getPlayers().equals(field.getPlayers()) : field.getPlayers() != null);

    }

    @Override
    public int hashCode() {
        int result = getPlace() != null ? getPlace().hashCode() : 0;
        result = 31 * result + (getPlayers() != null ? getPlayers().hashCode() : 0);
        return result;
    }

    public void removePlayer(String playerid) {
        this.players.removeIf(pl -> pl.getId().equals(playerid));
    }
}
