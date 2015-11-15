package de.hawhamburg.vs.restopoly.responses;

import java.io.Serializable;
import java.util.List;

public class GameDTO implements Serializable {
    public int gameid;
    public List<PlayerDTO> players;
    public boolean started = false;

    public GameDTO(int gameid, List<PlayerDTO> players, boolean started) {
        this.gameid = gameid;
        this.players = players;
        this.started = started;
    }

    public GameDTO() {
    }
}
