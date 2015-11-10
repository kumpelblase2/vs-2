package de.hawhamburg.vs.restopoly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Field {

    @GeneratedValue
    @Id
    @JsonIgnore
    private int id;
    @OneToOne
    private Place place;
    @OneToMany
    private List<Player> players;

    public Field(int id, Place place, List<Player> players) {
        this.id = id;
        this.place = place;
        this.players = players;
    }

    public Field() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (id != field.id) return false;
        if (place != null ? !place.equals(field.place) : field.place != null) return false;
        return !(players != null ? !players.equals(field.players) : field.players != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }
}
