package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.model.Game;
import de.hawhamburg.vs.restopoly.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameManager {
    private List<Game> games = new ArrayList<>();
    private int idCounter = 1;

    public Optional<Game> getGame(int id) {
        return this.games.stream().filter(g -> g.getGameid() == id).findFirst();
    }

    public Game createGame() {
        Game g = new Game(this.idCounter++, null, new ArrayList<Player>());
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
