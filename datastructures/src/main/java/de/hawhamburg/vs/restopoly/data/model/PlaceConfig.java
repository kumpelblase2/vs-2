package de.hawhamburg.vs.restopoly.data.model;

import java.util.Arrays;

public class PlaceConfig {
    private String name;
    private int value;
    private int[] rent;
    private int[] houseCost;

    public PlaceConfig(String name, int value, int[] rent, int[] houseCost) {
        this.name = name;
        this.value = value;
        this.rent = rent;
        this.houseCost = houseCost;
    }

    public PlaceConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[] getRent() {
        return rent;
    }

    public void setRent(int[] rent) {
        this.rent = rent;
    }

    public int[] getHouseCost() {
        return houseCost;
    }

    public void setHouseCost(int[] houseCost) {
        this.houseCost = houseCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceConfig that = (PlaceConfig) o;

        if (value != that.value) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!Arrays.equals(rent, that.rent)) return false;
        return Arrays.equals(houseCost, that.houseCost);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + value;
        result = 31 * result + Arrays.hashCode(rent);
        result = 31 * result + Arrays.hashCode(houseCost);
        return result;
    }
}
