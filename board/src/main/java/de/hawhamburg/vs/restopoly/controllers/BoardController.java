package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.ServiceRegistrator;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;
import de.hawhamburg.vs.restopoly.data.dto.PlaceDTO;
import de.hawhamburg.vs.restopoly.data.dto.ThrowDTO;
import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class BoardController {
    private static final String MUTEX_CHECK_URL = "/%d/players/turn";

    @Autowired
    private GameBoardManager gameBoardManager;

    @Value("${main_service}")
    private String mainServiceUrl;

    private String gameServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        this.gameServiceUrl = ServiceRegistrator.lookupService(this.mainServiceUrl, "Nyuu~Games");
    }

    @RequestMapping("/boards")
    public Collection<GameBoard> getBoards() {
        return this.gameBoardManager.getBoards();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = "/boards/{gameid}/players/{playerid}")
    public void placePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody GameBoard.Player player) {
        GameBoard b = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
        player.setId(playerid);
        b.addPlayer(player);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{gameid}/players/{playerid}")
    public void removePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        GameBoard b = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
        b.removePlayer(playerid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/roll")
    public void movePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody ThrowDTO rolls) {
        this.movePlayerRelative(gameid, playerid, rolls.roll1.number + rolls.roll2.number);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/move")
    public void movePlayerRelative(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody int amount) {
        String turnCheckUrl = this.gameServiceUrl + String.format(MUTEX_CHECK_URL, gameid);
        GameBoard b = this.gameBoardManager.getBoard(gameid).filter(bo -> bo.getPositions().containsKey(playerid))
                .orElseThrow(NotFoundException::new);

        GameBoard.Player player = this.restTemplate.getForObject(turnCheckUrl, GameBoard.Player.class);
        if(player.getId().equals(playerid)) {
            b.movePlayer(playerid, amount);
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping("/boards/{gameid}/players/{playerid}")
    public GameBoard.Player getPlayerPosition(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> b.getPlayer(playerid)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/boards/{gameid}")
    public GameBoard getBoard(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/boards/{gameid}/players")
    public Collection<GameBoard.Player> getPlayers(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new).getPlayers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}")
    public void createBoard(@PathVariable("gameid") int gameid) {
        if(this.gameBoardManager.getBoard(gameid).isPresent()) {
            throw new AlreadyExistsException();
        } else {
            this.gameBoardManager.createBoard(gameid);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{gameid}")
    public void deleteBoard(@PathVariable("gameid") int gameid) {
        this.gameBoardManager.deleteBoard(gameid);
    }

    @RequestMapping("/boards/{gameid}/places")
    public Collection<PlaceDTO> getPlaces(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new)
                .getFields().stream().map(PlaceDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/borads/{gameid}/places/{place}")
    public PlaceDTO getPlace(@PathVariable("gameid") int gameid, @PathVariable("place") int place) {
        GameBoard board = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
        if(board.getFields().size() > place && place >= 0) {
            return new PlaceDTO(board.getFields().get(place));
        } else {
            throw new NotFoundException();
        }
    }
}
