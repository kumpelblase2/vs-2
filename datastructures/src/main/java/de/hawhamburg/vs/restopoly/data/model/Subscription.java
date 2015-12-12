package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Subscription {
    private int gameid;
    private String uri;
    private Event event;

    public Subscription(int gameid, String uri, Event event) {
        this.gameid = gameid;
        this.uri = uri;
        this.event = event;
    }

    public Subscription() {
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @JsonIgnore
    public boolean hasValidUri() {
        return this.uri != null && !this.uri.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (getGameid() != that.getGameid()) return false;
        return getUri().equals(that.getUri()) && getEvent().equals(that.getEvent());

    }

    @Override
    public int hashCode() {
        int result = getGameid();
        result = 31 * result + getUri().hashCode();
        result = 31 * result + getEvent().hashCode();
        return result;
    }
}
