package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.EventPublisher;
import de.hawhamburg.vs.restopoly.data.dto.*;
import de.hawhamburg.vs.restopoly.data.model.Event;
import de.hawhamburg.vs.restopoly.data.model.Field;
import de.hawhamburg.vs.restopoly.data.model.GameBoard;
import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class BoardController {
    private static final String MUTEX_CHECK_URL = "/%d/players/turn";
    private static final String CREATED_PLAYER_LOCATION = "/boards/{gameid}/players/{playerid}";
    private static final String CREATED_BOARD_LOCATION = "/boards/{gameid}";

    @Autowired
    private GameBoardManager gameBoardManager;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(method = RequestMethod.GET, value = "/boards")
    public Collection<GameBoardDTO> getBoards() {
        return this.gameBoardManager.getGameIds().parallelStream().map(id -> new GameBoardDTO(id, this.gameBoardManager.getBoard(id).get())).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = CREATED_PLAYER_LOCATION)
    public void placePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody GameBoard.Player player, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        GameBoard b = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
        player.setId(playerid);
        if(player.getMove() == null || player.getMove().isEmpty()) {
            player.setMove("/boards/" + gameid + "/players/" + playerid + "/move");
        }

        if(player.getRoll() == null || player.getRoll().isEmpty()) {
            player.setRoll("/boards/" + gameid + "/players/" + playerid + "/roll");
        }
        b.addPlayer(player);
        response.setHeader("Location", uriBuilder.path(CREATED_PLAYER_LOCATION).buildAndExpand(gameid, playerid).toUriString());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{gameid}/players/{playerid}")
    public void removePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        GameBoard b = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
        b.removePlayer(playerid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/roll")
    public PlayerMoveResponse movePlayer(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody ThrowDTO rolls,
                                         UriComponentsBuilder uriBuilder) {
        return this.movePlayerRelative(gameid, playerid, rolls.roll1.number + rolls.roll2.number, uriBuilder);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{gameid}/players/{playerid}/move")
    public PlayerMoveResponse movePlayerRelative(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid, @RequestBody int amount,
                                                 UriComponentsBuilder uriBuilder) {
        GameBoard b = this.gameBoardManager.getBoard(gameid).filter(bo -> bo.getPositions().containsKey(playerid))
                .orElseThrow(NotFoundException::new);

        String turnCheckUrl = b.getComponents().getGame() + String.format(MUTEX_CHECK_URL, gameid);
        GameBoard.Player player;
        try {
            player = this.restTemplate.getForObject(turnCheckUrl, GameBoard.Player.class);
        } catch(Exception e) {
            throw new NotFoundException();
        }

        if(player.getId().equals(playerid)) {
            b.movePlayer(playerid, amount);
            String playerUri = uriBuilder.path(CREATED_PLAYER_LOCATION).buildAndExpand(gameid, playerid).toUriString();
            EventPublisher.sendEvent(b.getComponents().getEvents(), gameid,
                    new Event("Player " + playerid + " moved " + amount, "player-move", "", playerUri, playerid, null));
            return new PlayerMoveResponse(player, b);
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{gameid}/players/{playerid}")
    public GameBoard.Player getPlayerPosition(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> b.getPlayer(playerid)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{gameid}")
    public GameBoardDTO getBoard(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).map(b -> new GameBoardDTO(gameid, b)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{gameid}/players")
    public BoardPlayersDTO getPlayers(@PathVariable("gameid") int gameid) {
        return this.gameBoardManager.getBoard(gameid).map(GameBoard::getPlayers).map(pl -> new BoardPlayersDTO(gameid, pl)).orElseThrow(NotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = CREATED_BOARD_LOCATION)
    public void createBoard(@PathVariable("gameid") int gameid, @RequestBody  GameCreateDTO inGameCreateDTO, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        if(this.gameBoardManager.getBoard(gameid).isPresent()) {
            throw new AlreadyExistsException();
        } else {
            this.gameBoardManager.createBoard(gameid, inGameCreateDTO.getComponents());
            response.setHeader("Location", uriBuilder.path(CREATED_BOARD_LOCATION).buildAndExpand(gameid).toUriString());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{gameid}")
    public void deleteBoard(@PathVariable("gameid") int gameid) {
        this.gameBoardManager.deleteBoard(gameid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{gameid}/places")
    public Collection<String> getPlaces(@PathVariable("gameid") int gameid) {
        Collection<Field> fields = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new).getFields();
        Collection<String> result = new ArrayList<>(fields.size());
        for(int i = 0; i < fields.size(); i++) {
            result.add("/boards/" + gameid + "/places/" + i);
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/borads/{gameid}/places/{place}")
    public PlaceDTO getPlace(@PathVariable("gameid") int gameid, @PathVariable("place") int place) {
        GameBoard board = this.gameBoardManager.getBoard(gameid).orElseThrow(NotFoundException::new);
        if(board.getFields().size() > place && place >= 0) {
            return new PlaceDTO(board.getFields().get(place));
        } else {
            throw new NotFoundException();
        }
    }
}
