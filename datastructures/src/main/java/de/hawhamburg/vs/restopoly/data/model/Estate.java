package de.hawhamburg.vs.restopoly.data.model;

import java.util.List;

/**
 * Created by JanDennis on 04.12.2015.
 */
public class Estate {
    private String place;
    private String owner;
    private int value;
    private List<Integer> rent;
    private List<Integer> cost;
    private int houses;

    public Estate() {}

    public Estate(String place, String owner, int value, List<Integer> rent, List<Integer> cost, int houses) {
        this.place = place;
        this.owner = owner;
        this.value = value;
        this.rent = rent;
        this.cost = cost;
        this.houses = houses;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Integer> getRent() {
        return rent;
    }

    public void setRent(List<Integer> rent) {
        this.rent = rent;
    }

    public List<Integer> getCost() {
        return cost;
    }

    public void setCost(List<Integer> cost) {
        this.cost = cost;
    }

    public int getHouses() {
        return houses;
    }

    public void setHouses(int houses) {
        this.houses = houses;
    }
}
