package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Game;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlayersDTO {
    private Collection<String> players;

    public PlayersDTO(int gameId, Collection<Game.Player> inPlayers) {
        this.players = inPlayers.parallelStream().map(p -> "/games/" + gameId + "/players/" + p.getId()).collect(Collectors.toList());
    }

    public PlayersDTO(List<String> players) {
        this.players = players;
    }

    public PlayersDTO() {
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

        PlayersDTO that = (PlayersDTO) o;

        return players != null ? players.equals(that.players) : that.players == null;

    }

    @Override
    public int hashCode() {
        return players != null ? players.hashCode() : 0;
    }
}
