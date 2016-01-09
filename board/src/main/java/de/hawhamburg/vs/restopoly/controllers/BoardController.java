package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.dto.*;
import de.hawhamburg.vs.restopoly.data.model.Field;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;
import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class BoardController {
    private static final String MUTEX_CHECK_URL = "/%d/players/turn";

    @Autowired
    private GameBoardManager gameBoardManager;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/boards")
    public Collection<GameBoardDTO> getBoards() {
        return this.gameBoardManager.getGameIds().parallelStream().map(id -> new GameBoardDTO(id, this.gameBoardManager.getBoard(id).get())).collect(Collectors.toList());
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
    public PlayerMoveResponse movePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody ThrowDTO rolls) {
        return this.movePlayerRelative(gameid, playerid, rolls.roll1.number + rolls.roll2.number);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/move")
    public PlayerMoveResponse movePlayerRelative(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody int amount) {
        GameBoard b = this.gameBoardManager.getBoard(gameid).filter(bo -> bo.getPositions().containsKey(playerid))
                .orElseThrow(NotFoundException::new);

        String turnCheckUrl = b.getComponents().getGame() + String.format(MUTEX_CHECK_URL, gameid);

        GameBoard.Player player = this.restTemplate.getForObject(turnCheckUrl, GameBoard.Player.class);
        if(player.getId().equals(playerid)) {
            b.movePlayer(playerid, amount);
            return new PlayerMoveResponse(player, b);
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping("/boards/{gameid}/players/{playerid}")
    public GameBoard.Player getPlayerPosition(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> b.getPlayer(playerid)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/boards/{gameid}")
    public GameBoardDTO getBoard(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> new GameBoardDTO(gameid, b)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/boards/{gameid}/players")
    public BoardPlayersDTO getPlayers(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).map(GameBoard::getPlayers).map(pl -> new BoardPlayersDTO(gameid, pl)).orElseThrow(NotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}")
    public void createBoard(@PathVariable("gameid") int gameid, GameCreateDTO inGameCreateDTO) {
        if(this.gameBoardManager.getBoard(gameid).isPresent()) {
            throw new AlreadyExistsException();
        } else {
            this.gameBoardManager.createBoard(gameid, inGameCreateDTO.getComponents());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{gameid}")
    public void deleteBoard(@PathVariable("gameid") int gameid) {
        this.gameBoardManager.deleteBoard(gameid);
    }

    @RequestMapping("/boards/{gameid}/places")
    public Collection<String> getPlaces(@PathVariable("gameid") int gameid) {
        Collection<Field> fields = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new).getFields();
        Collection<String> result = new ArrayList<>(fields.size());
        for(int i = 0; i < fields.size(); i++) {
            result.add("/boards/" + gameid + "/places/" + i);
        }

        return result;
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
