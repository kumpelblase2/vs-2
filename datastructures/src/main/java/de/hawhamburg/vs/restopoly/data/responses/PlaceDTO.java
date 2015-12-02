package de.hawhamburg.vs.restopoly.data.responses;

import de.hawhamburg.vs.restopoly.data.model.Field;

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
