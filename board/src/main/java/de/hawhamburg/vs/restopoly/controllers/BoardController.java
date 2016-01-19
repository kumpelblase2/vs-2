package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.EventPublisher;
import de.hawhamburg.vs.restopoly.data.dto.*;
import de.hawhamburg.vs.restopoly.data.model.*;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.manager.GameBoardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CrossOrigin
@RestController
public class BoardController {
    private static final String MUTEX_CHECK_URL = "/players/current";
    private static final String CREATED_PLAYER_LOCATION = "/boards/{boardid}/players/{playerid}";
    private static final String CREATED_BOARD_LOCATION = "/boards/{boardid}";

    @Autowired
    private GameBoardManager gameBoardManager;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(method = RequestMethod.GET, value = "/boards")
    public Collection<GameBoardDTO> getBoards() {
        return this.gameBoardManager.getBoards().parallelStream().map(GameBoardDTO::new).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/boards")
    public void createBoard(@RequestBody GameCreateDTO createComponents, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        GameBoard board = this.gameBoardManager.createBoard(createComponents.getComponents());
        String boardUrl = uriBuilder.path(CREATED_BOARD_LOCATION).buildAndExpand(board.getId()).toUriString();
        board.getComponents().setBoard(boardUrl);
        response.setHeader("Location", boardUrl);
        try {
            String bankLocation = restTemplate.postForLocation(board.getComponents().getBank() + "/banks", new GameCreateDTO(board.getComponents())).toString();
            board.getComponents().setBank(bankLocation);

            String brokerLocation = restTemplate.postForLocation(board.getComponents().getBroker() + "/broker", new GameCreateDTO(board.getComponents())).toString();
            board.getComponents().setBroker(brokerLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<PlaceConfig> placeConfigList = this.gameBoardManager.getConfigForBoard(board);
        for(int i = 0; i < placeConfigList.size(); i++) {
            PlaceConfig config = placeConfigList.get(i);
            try {
                List<Integer> rent = IntStream.of(config.getRent()).boxed().collect(Collectors.toList());
                List<Integer> house = IntStream.of(config.getHouseCost()).boxed().collect(Collectors.toList());
                this.restTemplate.put(board.getComponents().getBroker() + "/places/" + i, new Estate(config.getName(), null, config.getValue(), rent, house, 0));
                board.getFields().get(i).setBroker(board.getComponents().getBroker() + "/places/" + i);
            } catch (Exception e) {
                System.out.println("Could not place. " + board.getComponents().getBroker() + "/places/" + i);
            }
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = CREATED_PLAYER_LOCATION)
    public void placePlayer(@PathVariable("boardid") int boardid, @PathVariable("playerid") String playerid, @RequestBody GameBoard.Player player, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        GameBoard b = this.gameBoardManager.getBoard(boardid).orElseThrow(NotFoundException::new);
        player.setId(playerid);
        if(player.getMove() == null || player.getMove().isEmpty()) {
            player.setMove("/boards/" + boardid + "/players/" + playerid + "/move");
        }

        if(player.getRoll() == null || player.getRoll().isEmpty()) {
            player.setRoll("/boards/" + boardid + "/players/" + playerid + "/roll");
        }
        b.addPlayer(player);
        response.setHeader("Location", uriBuilder.path(CREATED_PLAYER_LOCATION).buildAndExpand(boardid, playerid).toUriString());

        try {
            this.restTemplate.postForLocation(b.getComponents().getBank() + "/players", new PlayerAndAmountDTO(playerid, 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{boardid}/players/{playerid}")
    public void removePlayer(@PathVariable("boardid") int boardid, @PathVariable("playerid") String playerid) {
        GameBoard b = this.gameBoardManager.getBoard(boardid).orElseThrow(NotFoundException::new);
        b.removePlayer(playerid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{boardid}/players/{playerid}/roll")
    public PlayerMoveResponse movePlayer(@PathVariable("boardid") int boardid, @PathVariable("playerid") String playerid, @RequestBody ThrowDTO rolls,
                                         UriComponentsBuilder uriBuilder) {
        return this.movePlayerRelative(boardid, playerid, rolls.roll1.number + rolls.roll2.number, uriBuilder);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/boards/{boardid}/players/{playerid}/move")
    public PlayerMoveResponse movePlayerRelative(@PathVariable("boardid") int boardid, @PathVariable("playerid") String playerid, @RequestBody int amount,
                                                 UriComponentsBuilder uriBuilder) {
        GameBoard b = this.gameBoardManager.getBoard(boardid).orElseThrow(NotFoundException::new);

        String turnCheckUrl = b.getComponents().getGame() + MUTEX_CHECK_URL;
        GameBoard.Player player;
        try {
            player = this.restTemplate.getForObject(turnCheckUrl, GameBoard.Player.class);
        } catch(Exception e) {
            e.printStackTrace();
            throw new NotFoundException();
        }

        if(player.getId().equals(playerid)) {
            b.movePlayer(playerid, amount);
            String playerUri = uriBuilder.path(CREATED_PLAYER_LOCATION).buildAndExpand(boardid, playerid).toUriString();
            EventPublisher.sendEvent(b.getComponents().getEvents(),
                    new Event("Player " + playerid + " moved " + amount, "player-move", "", playerUri, playerid, null));
            return new PlayerMoveResponse(player, new GameBoardDTO(b));
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{boardid}/players/{playerid}")
    public GameBoard.Player getPlayerPosition(@PathVariable("boardid") int boardid, @PathVariable("playerid") String playerid) {
        return this.gameBoardManager.getBoard(boardid).map(b -> b.getPlayer(playerid)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{boardid}")
    public GameBoardDTO getBoard(@PathVariable("boardid") int boardid) {
        return this.gameBoardManager.getBoard(boardid).map(GameBoardDTO::new).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{boardid}/players")
    public BoardPlayersDTO getPlayers(@PathVariable("boardid") int boardid) {
        return this.gameBoardManager.getBoard(boardid).map(GameBoard::getPlayers).map(pl -> new BoardPlayersDTO(boardid, pl)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/boards/{boardid}")
    public void deleteBoard(@PathVariable("boardid") int boardid) {
        this.gameBoardManager.deleteBoard(boardid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{boardid}/places")
    public Collection<String> getPlaces(@PathVariable("boardid") int boardid) {
        Collection<Field> fields = this.gameBoardManager.getBoard(boardid).orElseThrow(NotFoundException::new).getFields();
        Collection<String> result = new ArrayList<>(fields.size());
        for(int i = 0; i < fields.size(); i++) {
            result.add("/boards/" + boardid + "/places/" + i);
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/boards/{boardid}/places/{place}")
    public PlaceDTO getPlace(@PathVariable("boardid") int boardid, @PathVariable("place") int place) {
        GameBoard board = this.gameBoardManager.getBoard(boardid).orElseThrow(NotFoundException::new);
        if(board.getFields().size() > place && place >= 0) {
            return new PlaceDTO(board.getFields().get(place));
        } else {
            throw new NotFoundException();
        }
    }
}
