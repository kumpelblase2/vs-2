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

    public GameBoardDTO(int boardId, GameBoard current) {
        this.fields = new ArrayList<>();
        for(int i = 0; i < current.getFields().size(); i++){
            this.fields.add(new FieldDTO(boardId, i, current.getFields().get(i)));
        }
        this.positions = current.getPositions();
    }
}
