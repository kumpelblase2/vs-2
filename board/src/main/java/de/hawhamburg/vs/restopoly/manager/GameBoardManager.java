package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.components.BoardFactory;
import de.hawhamburg.vs.restopoly.data.model.Components;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GameBoardManager {
    private final Map<Integer, GameBoard> boards = new HashMap<>();

    @Autowired
    private BoardFactory factory;

    public Optional<GameBoard> getBoard(int gameId) {
        return Optional.ofNullable(this.boards.get(gameId));
    }

    public void createBoard(int gameId, Components inComponents) {
        this.boards.putIfAbsent(gameId, factory.createBoard(inComponents));
    }

    public Collection<Integer> getGameIds() {
        return boards.keySet();
    }

    public Collection<GameBoard> getBoards() {
        return this.boards.values();
    }

    public void deleteBoard(int gameid) {
        this.boards.remove(gameid);
    }
}
