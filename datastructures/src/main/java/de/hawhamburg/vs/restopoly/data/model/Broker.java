package de.hawhamburg.vs.restopoly.data.model;

import java.util.HashMap;
import java.util.Map;

public class Broker {
    private int id;
    private Map<String, Estate> places = new HashMap<>();
    private Map<String, Player> owners = new HashMap<>();

    public Broker(int id) {
        this.id = id;
    }

    public boolean hasPlace(String Place) {
        return places.containsKey(Place);
    }

    public void addPlace(String name, Estate place) {
        places.put(name, place);
    }

    public void setOwner(String place, Player owner) {
        //Check if Place exists
        if(this.hasPlace(place)) {
            if (owners.containsKey(place)) {
                owners.replace(place, owner);
            } else {
                owners.put(place, owner);
            }
        }
    }

    public String getOwnerID(String place) {
        return places.get(place).getOwner();
    }

    public Player getOwner(String place) {
        return owners.get(place);
    }

    public int getValue(String place) {
        return places.get(place).getValue();
    }

    public int getRent(String place) {
        return places.get(place).getRent().get(places.get(place).getHouses());
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Broker broker = (Broker) o;

        if (id != broker.id) return false;
        if (places != null ? !places.equals(broker.places) : broker.places != null) return false;
        return owners != null ? owners.equals(broker.owners) : broker.owners == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (places != null ? places.hashCode() : 0);
        result = 31 * result + (owners != null ? owners.hashCode() : 0);
        return result;
    }
}
