package de.hawhamburg.vs.restopoly.data.model;

import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;

import java.util.List;
import java.util.Map;

/**
 * Created by JanDennis on 04.12.2015.
 */
public class Broker {
    private Map<String, Estate> places;

    public Boolean hasPlace(String Place) {
        return places.containsKey(Place);
    }
    public void addPlace(String name, Estate place) {
        places.putIfAbsent(name,place);
    }
}
