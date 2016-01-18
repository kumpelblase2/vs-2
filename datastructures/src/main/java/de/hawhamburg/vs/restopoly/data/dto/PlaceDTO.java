package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Field;

public class PlaceDTO {
    public String name;
    public String broker;

    public PlaceDTO(String name, String broker) {
        this.name = name;
        this.broker = broker;
    }

    public PlaceDTO(Field inField, String broker) {
        this(inField.getPlace(), broker);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }
}
