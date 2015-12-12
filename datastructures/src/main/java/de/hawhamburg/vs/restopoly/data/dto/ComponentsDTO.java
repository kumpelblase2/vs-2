package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Components;

public class ComponentsDTO {
    private Components components;

    public ComponentsDTO(Components components) {
        this.components = components;
    }

    public ComponentsDTO() {
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentsDTO that = (ComponentsDTO) o;

        return components != null ? components.equals(that.components) : that.components == null;

    }

    @Override
    public int hashCode() {
        return components != null ? components.hashCode() : 0;
    }
}
