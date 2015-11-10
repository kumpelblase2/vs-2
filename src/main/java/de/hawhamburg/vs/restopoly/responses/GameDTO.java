package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.Game;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class GameDTO implements Serializable {
    public int gameid;
    public List<PlayerDTO> players;
    public boolean started = false;

    public GameDTO(Game game) {
        this.gameid = game.getGameid();
        this.started = game.isStarted();
        this.players = game.getPlayers().stream().map(pl -> new PlayerDTO(pl, game.getGameid())).collect(Collectors.toList());
    }
}
