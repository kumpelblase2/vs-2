package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.model.GameBoard;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameBoardManager {
    private Map<Integer, GameBoard> boards = new HashMap<>();

    public Optional<GameBoard> getBoard(int gameId) {
        return Optional.ofNullable(this.boards.get(gameId));
    }

    public GameBoard createBoard(int gameId) {
        return this.boards.putIfAbsent(gameId, new GameBoard());
    }

    public Collection<Integer> getGameIds() {
        return boards.keySet();
    }
}
