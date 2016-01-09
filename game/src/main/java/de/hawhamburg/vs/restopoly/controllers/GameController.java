package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.dto.GameCreateDTO;
import de.hawhamburg.vs.restopoly.data.dto.GameDTO;
import de.hawhamburg.vs.restopoly.data.dto.PlayersDTO;
import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.data.model.Game;
import de.hawhamburg.vs.restopoly.data.dto.GameCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import de.hawhamburg.vs.restopoly.manager.GameManager;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class GameController {
    private static final String BOARD_URL = "/%d";
    private static final String BOARD_PLAYER_URL = "/%d/players/%s";
    private static final String PLAYER_TURN_URL = "/turn";

    @Autowired
    private GameManager gameManager;

    @Value("${main_service}")
    private String mainServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/games")
    public GameCreateResponse createGame(@RequestBody GameCreateDTO gameComponents) {
        Game created = this.gameManager.createGame(gameComponents.getComponents());

        String url = created.getComponents().getBoard() + String.format(BOARD_URL,created.getGameid());

        restTemplate.postForLocation(url, gameComponents);
        return new GameCreateResponse(created.getGameid());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}")
    public void registerPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String player,
                               @RequestParam("name") String playername, @RequestParam("uri") String uri) {
        Game g = this.gameManager.getGame(gameid).orElseThrow(NotFoundException::new);
        if(g.hasPlayer(player)) {
            throw new AlreadyExistsException();
        }
        Game.Player newPlayer = new Game.Player(player, playername == null ? player : playername, uri, false);
        g.getPlayers().add(newPlayer);

        String url = g.getComponents().getBoard() + String.format(BOARD_PLAYER_URL, gameid, player);
        restTemplate.put(url, newPlayer);
    }

    @RequestMapping("/games/{gameid}/players/{playerid}")
    public Game.Player getPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String player) {
        Game g = this.gameManager.getGame(gameid).orElseThrow(NotFoundException::new);
        return g.getPlayers().stream().filter(pl -> pl.getId().equals(player)).findFirst().orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/games/{gameid}/players/{playerid}")
    public void deletePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String player) {
        Game g = this.gameManager.getGame(gameid).orElseThrow(NotFoundException::new);
        if(!g.getPlayers().removeIf(pl -> pl.getId().equals(player))) {
            throw new NotFoundException();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/{playerid}/ready")
    public void readyPlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") final String playerid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        game.map(g -> {
            g.getPlayers().stream().filter(pl -> pl.getId().equals(playerid)).forEach(pl -> {
                pl.setReady(true);
                if(g.isStarted() && g.getCurrentPlayer().equals(pl)) {
                    g.nextPlayer();
                    notifyCurrentPlayer(g);
                }
            });

            if(g.getPlayers().stream().allMatch(Game.Player::isReady)) {
                g.start();
                notifyCurrentPlayer(g);
            }

            return g;
        });
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players/{playerid}/ready")
    public boolean isPlayerReady(@PathVariable("gameid") int gameid, @PathVariable("playerid") final String playerid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(g -> g.getPlayers().stream().filter(Game.Player::isReady).anyMatch(pl -> pl.getId().equals(playerid))).orElse(false);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games/{gameid}/players")
    public PlayersDTO getAllPlayers(@PathVariable("gameid") int gameid) {
        Optional<Game> game = this.gameManager.getGame(gameid);
        return game.map(g -> new PlayersDTO(g.getGameid(), g.getPlayers())).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/games/{gameid}/players/current")
    public Game.Player getCurrentPlayer(@PathVariable("gameid") int gameid) {
        return this.gameManager.getGame(gameid).map(Game::getCurrentPlayer)
                .orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/games/{gameid}/players/turn")
    public ResponseEntity getPlayerMutex(@PathVariable("gameid") int gameid, @RequestParam("player") String playerName, @RequestBody Game.Player player) {
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
    public Game.Player getPlayerMutex(@PathVariable("gameid") int gameid) {
        return this.gameManager.getGame(gameid).filter(Game::isMutexAcquired).map(Game::getCurrentPlayer)
                            .orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/games")
    public Collection<GameDTO> getGames() {
        return this.gameManager.getAllGames().parallelStream().map(GameDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/games/{gameid}")
    public GameDTO getGameInfo(@PathVariable("gameid") int gameId) {
        return this.gameManager.getGame(gameId).map(GameDTO::new).orElseThrow(NotFoundException::new);
    }

    private void notifyCurrentPlayer(Game g) {
        try {
            this.restTemplate.postForLocation(g.getCurrentPlayer().getUri() + PLAYER_TURN_URL, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
