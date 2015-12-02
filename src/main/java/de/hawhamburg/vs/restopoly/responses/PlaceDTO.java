package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.Field;

public class PlaceDTO {
    public String name;

    public PlaceDTO(String name) {
        this.name = name;
    }

    public PlaceDTO(Field inField) {
        this(inField.getPlace());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
