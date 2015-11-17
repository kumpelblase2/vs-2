package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import de.hawhamburg.vs.restopoly.model.GameBoard;
import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.responses.ThrowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;

@RestController
public class BoardController {
    private static final String MUTEX_CHECK_URL = "/games/{gameid}/turn";

    @Autowired
    private GameBoardManager gameBoardManager;

    @Value("${main_service}")
    private String mainServiceUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/roll")
    public ResponseEntity movePlayer(@PathParam("gameid") int gameid, @PathParam("playerid") String playerid, @RequestBody ThrowDTO rolls) {
        String turnCheckUrl = this.mainServiceUrl + MUTEX_CHECK_URL.replace("{gameid}", gameid + "");
        return this.gameBoardManager.getBoard(gameid).filter(b -> b.getPositions().containsKey(playerid)).map(b -> {
            Player player = this.restTemplate.getForObject(turnCheckUrl, Player.class);
            if(player.getId().equals(playerid)) {
                b.movePlayer(playerid, rolls.roll1.number + rolls.roll2.number);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping("/boards/{gameid}/players/{playerid}")
    public ResponseEntity<Player> getPlayerPosition(@PathParam("gameid") int gameid, @PathParam("playerid") String playerid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> b.getPositions().get(playerid)).map(pos -> {
            Player pl = new Player(playerid);
            pl.setPosition(pos);
            return new ResponseEntity<>(pl, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping("/boards/{gameid}")
    public ResponseEntity<GameBoard> getBoard(@PathParam("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> new ResponseEntity<>(b, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
