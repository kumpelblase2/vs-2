package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import de.hawhamburg.vs.restopoly.model.GameBoard;
import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.responses.ThrowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class BoardController {
    private static final String MUTEX_CHECK_URL = "/games/{gameid}/players/turn";

    @Autowired
    private GameBoardManager gameBoardManager;

    @Value("${main_service}")
    private String mainServiceUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = "/boards/{gameid}/players/{playerid}")
    public void placePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody Player player) {
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
        String turnCheckUrl = this.mainServiceUrl + MUTEX_CHECK_URL.replace("{gameid}", gameid + "");
        GameBoard b = this.gameBoardManager.getBoard(gameid).filter(bo -> bo.getPositions().containsKey(playerid))
                .orElseThrow(NotFoundException::new);

        Player player = this.restTemplate.getForObject(turnCheckUrl, Player.class);
        if(player.getId().equals(playerid)) {
            b.movePlayer(playerid, rolls.roll1.number + rolls.roll2.number);
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping("/boards/{gameid}/players/{playerid}")
    public Player getPlayerPosition(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> b.getPositions().get(playerid)).map(pos -> {
            Player pl = new Player(playerid);
            pl.setPosition(pos);
            return pl;
        }).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/boards/{gameid}")
    public GameBoard getBoard(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
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
}
