package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Field;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;

import java.util.List;

public class FieldDTO {
    private String place;
    private List<GameBoard.Player> players;

    public FieldDTO(String place, List<GameBoard.Player> players) {
        this.place = place;
        this.players = players;
    }

    public FieldDTO() {
    }

    public FieldDTO(int boardId, int placePos, Field current) {
        this.place = "/boards/" + boardId + "/places/" + placePos;
        this.players = current.getPlayers();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldDTO fieldDTO = (FieldDTO) o;

        if (place != null ? !place.equals(fieldDTO.place) : fieldDTO.place != null) return false;
        return players != null ? players.equals(fieldDTO.players) : fieldDTO.players == null;

    }

    @Override
    public int hashCode() {
        int result = place != null ? place.hashCode() : 0;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }
}
