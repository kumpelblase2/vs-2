package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Event {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int gameid;
    private String name;
    private String type;
    private String reason;
    private String resource;
    private Player player;

    public Event(String name, String type, String reason, String resource, Player player) {
        this.name = name;
        this.type = type;
        this.reason = reason;
        this.resource = resource;
        this.player = player;
    }

    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!getType().equals(event.getType())) return false;
        if (getReason() != null ? !getReason().equals(event.getReason()) : event.getReason() != null) return false;
        return getResource().equals(event.getResource()) && !(getPlayer() != null ? !getPlayer().equals(event.getPlayer()) : event.getPlayer() != null);

    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + getResource().hashCode();
        result = 31 * result + (getPlayer() != null ? getPlayer().hashCode() : 0);
        return result;
    }

    public boolean isSame(Event event) {
        if (!getType().equals(event.getType())) return false;
        if (getReason() != null && !getResource().equals(event.getResource())) return false;
        if (getReason() != null && !getReason().equals(event.getReason())) return false;
        return !(getPlayer() != null && !getPlayer().equals(event.getPlayer()));
    }
}
