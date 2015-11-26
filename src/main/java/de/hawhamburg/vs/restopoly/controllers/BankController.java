package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.manager.BankManager;

import de.hawhamburg.vs.restopoly.model.Player;
import de.hawhamburg.vs.restopoly.responses.BankTransferResponse;
import de.hawhamburg.vs.restopoly.responses.PlayerAndAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class    BankController {
    @Autowired
    private BankManager manager;

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/players")
    public ResponseEntity createNewBankAccount(@PathVariable("gameid") int gameid, @RequestBody PlayerAndAmountDTO body) {
        String player = body.getPlayer().getId();
        int amount = body.getAmount();

        try {
            manager.getBank(gameid).createAccount(player, amount);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{gameid}/players/{playerid}")
    public ResponseEntity<Integer> getAmount(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        int result;
        try {
            result = manager.getBank(gameid).getBalance(playerid);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Integer>(result,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/to/{to}/{amount}")
    public BankTransferResponse postTransferMoneyToAccount(@PathVariable("gameid") int gameid, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        manager.getBank(gameid).addAmount(to,amount);
        return new BankTransferResponse(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/{amount}")
    public BankTransferResponse postTransferMoneyFromAccount(@PathVariable("gameid") int gameid, @PathVariable("from") String from, @PathVariable("amount") int amount, @RequestBody String reason) {
        manager.getBank(gameid).subtractAmount(from, amount);
        return new BankTransferResponse(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/to/{to}/{amount}")
    public BankTransferResponse postTransferMoneyFromAccountToAccount(@PathVariable("gameid") int gameid, @PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        manager.getBank(gameid).subtractAmount(from,amount);
        manager.getBank(gameid).addAmount(to,amount);

        return new BankTransferResponse(reason, new Player());
    }

}
