package de.hawhamburg.vs.restopoly.components;

import com.google.gson.Gson;
import de.hawhamburg.vs.restopoly.data.model.Components;
import de.hawhamburg.vs.restopoly.data.model.Field;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;
import de.hawhamburg.vs.restopoly.data.dto.PlaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class BoardFactory {
    private List<PlaceDTO> defaultBoard;
    private AtomicInteger idCount = new AtomicInteger();

    @Autowired
    public BoardFactory(@Value("${board.location}") String inBoardLocation) throws FileNotFoundException {
        this.defaultBoard = Arrays.asList(new Gson().fromJson(new FileReader(new File(inBoardLocation)), PlaceDTO[].class));
    }

    public GameBoard createBoard(Components inComponents) {
        return new GameBoard(idCount.incrementAndGet(), defaultBoard.stream().map(Field::new).collect(Collectors.toList()), new HashMap<>(), inComponents);
    }
}
