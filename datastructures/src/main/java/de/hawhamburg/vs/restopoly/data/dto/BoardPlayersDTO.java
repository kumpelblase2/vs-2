package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.GameBoard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BoardPlayersDTO {
    private Collection<String> players;

    public BoardPlayersDTO(int gameId, Collection<GameBoard.Player> inPlayers) {
        this.players = inPlayers.parallelStream().map(p -> "/boards/" + gameId + "/players/" + p.getId()).collect(Collectors.toList());
    }

    public BoardPlayersDTO(List<String> players) {
        this.players = players;
    }

    public BoardPlayersDTO() {
    }

    public Collection<String> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<String> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardPlayersDTO that = (BoardPlayersDTO) o;

        return players != null ? players.equals(that.players) : that.players == null;

    }

    @Override
    public int hashCode() {
        return players != null ? players.hashCode() : 0;
    }
}
