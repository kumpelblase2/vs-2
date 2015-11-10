package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.model.Game;
import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.repository.GameRepository;
import de.hawhamburg.vs.restopoly.responses.GameCreateResponse;
import de.hawhamburg.vs.restopoly.responses.GameDTO;
import de.hawhamburg.vs.restopoly.responses.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class GameController {
    @Autowired
    private GameRepository repository;

    @RequestMapping(method = RequestMethod.POST, value = "/game")
    public ResponseEntity<GameCreateResponse> createGame() {
        Game created = new Game();
        // TODO FILL created.setComponents(new Components(new GameBoard()));
        this.repository.save(created);
        return new ResponseEntity<>(new GameCreateResponse(created.getGameid()), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}")
    public void registerPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String player,
                               @RequestParam(required = false, value = "name") String playername,
                               @RequestParam(required = false, value = "uri") String uri) {
        Game game = this.repository.findOne(gameid);
        if(game != null) {
            game.getPlayers().add(new Player(player, playername == null ? player : playername, uri, 0, false));
            this.repository.save(game);
        } else {
            throw new RuntimeException("Not found.");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}/ready")
    public void readyPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") final String playerid) {
        Game game = this.repository.findOne(gameid);
        if(game != null) {
            game.getPlayers().stream().filter(pl -> pl.getId().equals(playerid)).forEach(pl -> {
                pl.setReady(true);
                if(game.isStarted()) {
                    game.nextPlayer();
                }
            });

            if(game.getPlayers().stream().allMatch(Player::isReady)) {
                game.setStarted(true);
            }
            this.repository.save(game);
        } else {
            throw new RuntimeException("Not found.");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players/{playerid}/ready")
    public boolean isPlayerReady(@PathVariable("gameid") int gameid, @PathVariable("playerid") final String playerid) {
        Game game = this.repository.findOne(gameid);
        if(game != null) {
            Optional<Player> player = game.getPlayers().stream().filter(pl -> pl.getId().equals(playerid)).findFirst();
            return player.orElseThrow(RuntimeException::new).isReady();
        } else {
            throw new RuntimeException("Not found.");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players")
    public Collection<PlayerDTO> getAllPlayers(@PathVariable("gameid") int gameid) {
        return this.repository.findOne(gameid).getPlayers().stream().map(pl -> new PlayerDTO(pl, gameid)).collect(Collectors.toList());
    }

    @RequestMapping("/games/{gameid}/players/current")
    public PlayerDTO getCurrentPlayer(@PathVariable("gameid") int gameid) {
        return new PlayerDTO(this.repository.findOne(gameid).getCurrentPlayer(), gameid);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/turn")
    public ResponseEntity getPlayerMutex(@PathVariable("gameid") int gameid, @RequestBody Player player) {
        Game game = this.repository.findOne(gameid);
        if(game.getCurrentPlayer().getId().equals(player.getId())) {
            if(game.isMutexAcquired()) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                game.setMutexAcquired(true);
                this.repository.save(game);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/games/{gameid}/players/turn")
    public ResponseEntity removePlayerMutex(@PathVariable("gameid") int gameid, @RequestParam("player") String playerid) {
        Game game = this.repository.findOne(gameid);
        if(game.getCurrentPlayer().getId().equals(playerid)) {
            if(game.isMutexAcquired()) {
                game.setMutexAcquired(false);
                this.repository.save(game);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players/turn")
    public ResponseEntity<PlayerDTO> getPlayerMutex(@PathVariable("gameid") int gameid) {
        Game game = this.repository.findOne(gameid);
        if(game.isMutexAcquired()) {
            return new ResponseEntity<>(new PlayerDTO(game.getCurrentPlayer(), gameid), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/games")
    public Collection<GameDTO> getGames() {
        return ((Collection<Game>) repository.findAll()).stream().map(GameDTO::new).collect(Collectors.toList());
    }
}
