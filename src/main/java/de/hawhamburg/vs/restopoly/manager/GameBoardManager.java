package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.components.BoardFactory;
import de.hawhamburg.vs.restopoly.model.GameBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameBoardManager {
    private Map<Integer, GameBoard> boards = new HashMap<>();

    @Autowired
    private BoardFactory factory;

    public Optional<GameBoard> getBoard(int gameId) {
        return Optional.ofNullable(this.boards.get(gameId));
    }

    public GameBoard createBoard(int gameId) {
        return this.boards.putIfAbsent(gameId, factory.createBoard());
    }

    public Collection<Integer> getGameIds() {
        return boards.keySet();
    }
}
