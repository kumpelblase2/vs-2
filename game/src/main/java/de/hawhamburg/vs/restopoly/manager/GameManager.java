package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.data.model.Components;
import de.hawhamburg.vs.restopoly.data.model.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GameManager {
    private final List<Game> games = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger();

    public Optional<Game> getGame(int id) {
        return this.games.stream().filter(g -> g.getGameid() == id).findFirst();
    }

    public Game createGame() {
        return this.createGame(null);
    }

    public void deleteGame(int id) {
        this.games.removeIf(g -> g.getGameid() == id);
    }

    public List<Game> getAllGames() {
        return games;
    }

    public Game createGame(Components components) {
        Game g = new Game(this.idCounter.incrementAndGet(), components, new ArrayList<>());
        components.setGame(components.getGame() + "/games/" + g.getGameid());
        this.games.add(g);
        return g;
    }
}
