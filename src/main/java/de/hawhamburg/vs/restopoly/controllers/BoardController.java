package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import de.hawhamburg.vs.restopoly.model.GameBoard;
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
    private GameBoardManager gameBoardManager;

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/roll")
    public GameBoard movePlayer(@PathParam("gameid") int gameid, @PathParam("playerid") String playerid, @RequestBody ThrowDTO rolls) {
        return null;
    }
}
