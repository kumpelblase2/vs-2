package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.manager.BankManager;

import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.responses.BankTransferResponce;
import de.hawhamburg.vs.restopoly.responses.PlayerAndAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by JanDennis on 17.11.2015.
 */
@RestController
public class BankController {
    @Autowired
    private BankManager manager;

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/players")
    public ResponseEntity createNewBankAccount(@PathVariable int gameid, @RequestBody PlayerAndAmountDTO body) {
        String player = body.getPlayer().getId();
        int amount = body.getAmount();

        try {
            manager.getBank(gameid).createAccount(player, amount);
        } catch (RuntimeException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{gameid}/players/{playerid}")
    public ResponseEntity<Integer> getAmount(@PathVariable int gameid, @PathVariable String playerid) {
        int result;
        try {
            result = manager.getBank(gameid).getBalance(playerid);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Integer>(result,HttpStatus.OK);
//        Optional<String> player = game.getPlayers().stream().filter(pl -> pl.getId().equals(playerid)).findFirst();
//        return player.map(pl -> new ResponseEntity<>(manager.getBank(game).getBalance(pl), HttpStatus.OK)).orElse(new ResponseEntity<Integer>(HttpStatus.NOT_FOUND));

    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/to/{to}/{amount}")
    public BankTransferResponce postTranferMoneyToAccount(@PathVariable int gameid, @PathVariable String to, @PathVariable int amount, @RequestBody String reason) {


//        Player player = game.getPlayers().stream().filter(pl -> pl.getId().equals(to)).findFirst().get();
//
        manager.getBank(gameid).addAmount(to,amount);
//
        return new BankTransferResponce(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/{amount}")
    public BankTransferResponce postTranferMoneyFromAccount(@PathVariable int gameid, @PathVariable String from, @PathVariable int amount, @RequestBody String reason) {


//        Player player = game.getPlayers().stream().filter(pl -> pl.getId().equals(from)).findFirst().get();
//
        manager.getBank(gameid).subtractAmount(from, amount);
//
        return new BankTransferResponce(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/to/{to}/{amount}")
    public BankTransferResponce postTranferMoneyFromAccountToAccount(@PathVariable int gameid,@PathVariable String from, @PathVariable String to, @PathVariable int amount, @RequestBody String reason) {


//        Player playerFrom = game.getPlayers().stream().filter(pl -> pl.getId().equals(from)).findFirst().get();
//        Player playerTo = game.getPlayers().stream().filter(pl -> pl.getId().equals(to)).findFirst().get();

        manager.getBank(gameid).subtractAmount(from,amount);
        manager.getBank(gameid).addAmount(to,amount);

        return new BankTransferResponce(reason, new Player());
    }

}
