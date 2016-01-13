package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.components.BoardFactory;
import de.hawhamburg.vs.restopoly.data.model.Components;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameBoardManager {
    private final Set<GameBoard> boards = new HashSet<>();

    @Autowired
    private BoardFactory factory;

    public Optional<GameBoard> getBoard(int boardId) {
        return this.boards.parallelStream().filter(b -> b.getId() == boardId).findAny();
    }

    public GameBoard createBoard(Components inComponents) {
        GameBoard gameBoard = factory.createBoard(inComponents);
        this.boards.add(gameBoard);
        return gameBoard;
    }

    public Collection<GameBoard> getBoards() {
        return this.boards;
    }

    public void deleteBoard(int boardId) {
        this.boards.removeIf(b -> b.getId() == boardId);
    }
}
