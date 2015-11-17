package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.manager.GameManager;
import de.hawhamburg.vs.restopoly.model.Game;
import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.responses.GameCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class GameController {
    @Autowired
    private GameManager gameManager;

    @RequestMapping(method = RequestMethod.POST, value = "/game")
    public ResponseEntity<GameCreateResponse> createGame() {
        Game created = this.gameManager.createGame();
        return new ResponseEntity<>(new GameCreateResponse(created.getGameid()), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}")
    public ResponseEntity registerPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String player,
                               @RequestParam(required = false, value = "name") String playername,
                               @RequestParam(required = false, value = "uri") String uri) {
        return this.gameManager.getGame(gameid).map(g -> {
            if(g.hasPlayer(player)) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
            g.getPlayers().add(new Player(player, playername == null ? player : playername, uri, 0, false, g.getGameid()));
            return new ResponseEntity(HttpStatus.CREATED);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}/ready")
    public void readyPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") final String playerid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        game.map(g -> {
            g.getPlayers().stream().filter(pl -> pl.getId().equals(playerid)).forEach(pl -> {
                pl.setReady(true);
                if(g.isStarted() && g.getCurrentPlayer().equals(pl)) {
                    g.nextPlayer();
                }
            });

            if(g.getPlayers().stream().allMatch(Player::isReady)) {
                g.setStarted(true);
            }

            return g;
        });
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players/{playerid}/ready")
    public boolean isPlayerReady(@PathVariable("gameid") int gameid, @PathVariable("playerid") final String playerid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(g -> g.getPlayers().stream().filter(Player::isReady).anyMatch(pl -> pl.getId().equals(playerid))).orElse(false);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players")
    public ResponseEntity<List<Player>> getAllPlayers(@PathVariable("gameid") int gameid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(Game::getPlayers).map(pl -> new ResponseEntity<>(pl, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping("/games/{gameid}/players/current")
    public ResponseEntity<Player> getCurrentPlayer(@PathVariable("gameid") int gameid) {
        return this.gameManager.getGame(gameid).map(Game::getCurrentPlayer).map(pl -> new ResponseEntity<>(pl, HttpStatus.OK))
                .orElse(new ResponseEntity<Player>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/turn")
    public ResponseEntity getPlayerMutex(@PathVariable("gameid") int gameid, @RequestBody Player player) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(g -> {
            if(g.getCurrentPlayer().getId().equals(player.getId())) {
                if(g.isMutexAcquired()) {
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    g.setMutexAcquired(true);
                    return new ResponseEntity(HttpStatus.CREATED);
                }
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/games/{gameid}/players/turn")
    public ResponseEntity removePlayerMutex(@PathVariable("gameid") int gameid, @RequestParam("player") String playerid) {
        return this.gameManager.getGame(gameid).map(g -> {
            if(g.getCurrentPlayer().getId().equals(playerid)) {
                if(g.isMutexAcquired()) {
                    g.setMutexAcquired(false);
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players/turn")
    public ResponseEntity<Player> getPlayerMutex(@PathVariable("gameid") int gameid) {
        return this.gameManager.getGame(gameid).filter(Game::isMutexAcquired).map(Game::getCurrentPlayer).map(pl -> new ResponseEntity<>(pl, HttpStatus.OK))
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping("/games")
    public Collection<Game> getGames() {
        return this.gameManager.getAllGames();
    }
}
