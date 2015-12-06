package de.hawhamburg.vs.restopoly.data.model;

import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;


import java.util.List;
import java.util.Map;

/**
 * Created by JanDennis on 04.12.2015.
 */
public class Broker {
    private Map<String, Estate> places;
    private Map<String, Player> owners;

    public Boolean hasPlace(String Place) {
        return places.containsKey(Place);
    }
    public void addPlace(String name, Estate place) {
        places.putIfAbsent(name,place);
    }
    public void setOwner(String place, Player owner) {
        if (owners.containsKey(place)) {
            owners.replace(place,owner);
        } else {
            owners.put(place,owner);
        }
    }
    public String getOwnerID(String name) {
        return places.get(name).getOwner();
    }
    public Player getOwner(String name) {
        return owners.get(name);
    }
}
