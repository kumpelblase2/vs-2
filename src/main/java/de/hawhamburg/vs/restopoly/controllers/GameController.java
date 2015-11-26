package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.errors.NotFoundException;
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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/game")
    public GameCreateResponse createGame() {
        Game created = this.gameManager.createGame();
        return new GameCreateResponse(created.getGameid());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}")
    public void registerPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String player,
                               @RequestParam(required = false, value = "name") String playername,
                               @RequestParam(required = false, value = "uri") String uri) {
        Game g = this.gameManager.getGame(gameid).orElseThrow(NotFoundException::new);
        if(g.hasPlayer(player)) {
            throw new AlreadyExistsException();
        }
        g.getPlayers().add(new Player(player, playername == null ? player : playername, uri, 0, false, g.getGameid()));
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
                g.start();
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
    public List<Player> getAllPlayers(@PathVariable("gameid") int gameid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(Game::getPlayers).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/games/{gameid}/players/current")
    public Player getCurrentPlayer(@PathVariable("gameid") int gameid) {
        return this.gameManager.getGame(gameid).map(Game::getCurrentPlayer)
                .orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/turn")
    public ResponseEntity getPlayerMutex(@PathVariable("gameid") int gameid, @RequestParam("player") String playerName, @RequestBody Player player) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(g -> {
            if(g.getCurrentPlayer().getId().equals(playerName)) {
                if(g.isMutexAcquired()) {
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    g.setMutexAcquired(true);
                    return new ResponseEntity(HttpStatus.CREATED);
                }
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        }).orElseThrow(NotFoundException::new);
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
        }).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players/turn")
    public Player getPlayerMutex(@PathVariable("gameid") int gameid) {
        return this.gameManager.getGame(gameid).filter(Game::isMutexAcquired).map(Game::getCurrentPlayer)
                            .orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/games")
    public Collection<Game> getGames() {
        return this.gameManager.getAllGames();
    }
}
