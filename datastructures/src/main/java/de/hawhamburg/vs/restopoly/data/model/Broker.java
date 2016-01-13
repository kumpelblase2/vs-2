package de.hawhamburg.vs.restopoly.data.model;

import java.util.HashMap;
import java.util.Map;

public class Broker {
    private Map<String, Estate> places = new HashMap<>();
    private Map<String, Player> owners = new HashMap<>();

    public Boolean hasPlace(String Place) {
        return places.containsKey(Place);
    }

    public void addPlace(String name, Estate place) {
        places.putIfAbsent(name, place);
    }

    public void setOwner(String place, Player owner) {
        if (owners.containsKey(place)) {
            owners.replace(place, owner);
        } else {
            owners.put(place, owner);
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
}
