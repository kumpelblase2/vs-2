package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.data.model.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameManager {
    private final List<Game> games = new ArrayList<>();
    private int idCounter = 1;

    public Optional<Game> getGame(int id) {
        return this.games.stream().filter(g -> g.getGameid() == id).findFirst();
    }

    public Game createGame() {
        Game g = new Game(this.idCounter++, null, new ArrayList<>());
        this.games.add(g);
        return g;
    }

    public void deleteGame(int id) {
        this.games.removeIf(g -> g.getGameid() == id);
    }

    public List<Game> getAllGames() {
        return games;
    }
}
