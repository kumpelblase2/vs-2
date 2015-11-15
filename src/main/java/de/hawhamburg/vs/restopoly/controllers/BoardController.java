package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.model.Field;
import de.hawhamburg.vs.restopoly.model.Game;
import de.hawhamburg.vs.restopoly.model.GameBoard;
import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.repository.GameRepository;
import de.hawhamburg.vs.restopoly.responses.BoardDTO;
import de.hawhamburg.vs.restopoly.responses.PlayerDTO;
import de.hawhamburg.vs.restopoly.responses.ThrowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
public class BoardController {
    @Autowired
    private GameRepository repository;


    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/roll")
    public BoardDTO movePlayer(@PathParam("gameid") Integer gameid, @PathParam("playerid") String playerid, @RequestBody ThrowDTO rolls) {
        Game game = repository.findOne(1);
        GameBoard board = game.getComponents().getBoard();
        Optional<Player> player = game.getPlayers().stream().filter(pl -> pl.getId().equals(playerid)).findFirst();

        if (player.isPresent() && game.getCurrentPlayer().equals(player.get())) {
            Optional<Field> resultField = board.getFields().stream().filter(field -> field.getPlayers().contains(player.get())).findFirst();
            if (resultField.isPresent()) {
                // Ändere Position in FieldInfo
                resultField.get().getPlayers().remove(player.get());
                int newPos = board.getFields().indexOf(resultField)+(rolls.roll1.number + rolls.roll2.number);
                board.getFields().get(newPos).getPlayers().add(player.get());

                // Ändere Position in Playerinfo
                player.get().setPosition(player.get().getPosition()+newPos);
            }
        }
    return new BoardDTO(new PlayerDTO(player.get(),gameid),board);
    }
}
