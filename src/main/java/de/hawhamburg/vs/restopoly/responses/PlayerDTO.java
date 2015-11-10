package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.Player;

public class PlayerDTO {
    public String id;
    public String name;
    public String uri;
    public int position;
    public boolean ready;
    public String place;

    public PlayerDTO(String id, String name, String uri, int position, boolean ready, String place) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.position = position;
        this.ready = ready;
        this.place = place;
    }

    public PlayerDTO() {
    }

    public PlayerDTO(Player player, int gameid) {
        this.id = player.getId();
        this.name = player.getName();
        this.uri = player.getUri();
        this.position = player.getPosition();
        this.ready = player.isReady();
        this.place = String.format("/board/%d/places/%d", gameid, this.position);
    }
}
