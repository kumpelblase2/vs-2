package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.GameBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GameBoardDTO {
    private Collection<FieldDTO> fields;
    private Map<String, Integer> positions;

    public GameBoardDTO(Collection<FieldDTO> fields, Map<String, Integer> positions) {
        this.fields = fields;
        this.positions = positions;
    }

    public GameBoardDTO() {
    }

    public GameBoardDTO(GameBoard current) {
        this.fields = new ArrayList<>();
        for(int i = 0; i < current.getFields().size(); i++){
            this.fields.add(new FieldDTO(current.getId(), i, current.getFields().get(i)));
        }
        this.positions = current.getPositions();
    }

    public Collection<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(Collection<FieldDTO> fields) {
        this.fields = fields;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Integer> positions) {
        this.positions = positions;
    }
}
