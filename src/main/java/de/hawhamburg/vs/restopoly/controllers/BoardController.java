package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.model.Game;
import de.hawhamburg.vs.restopoly.model.GameBoard;
import de.hawhamburg.vs.restopoly.repository.GameRepository;
import de.hawhamburg.vs.restopoly.responses.BoardDTO;
import de.hawhamburg.vs.restopoly.responses.ThrowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class BoardController {
    @Autowired
    private GameRepository repository;

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/roll")
    public BoardDTO movePlayer(@PathParam("gameid") int gameid, @PathParam("playerid") String playerid, @RequestBody ThrowDTO rolls) {
        GameBoard board = repository.findOne(gameid).getComponents().getBoard();
        Game game = repository.findOne(gameid);
        if (game.getCurrentPlayer().getId().equals(playerid)) {

        }
    }
}
