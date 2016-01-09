package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.dto.GameCreateDTO;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.data.model.Bank;
import de.hawhamburg.vs.restopoly.data.model.Player;
import de.hawhamburg.vs.restopoly.data.dto.BankTransferResponse;
import de.hawhamburg.vs.restopoly.data.dto.PlayerAndAmountDTO;
import de.hawhamburg.vs.restopoly.manager.BankManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankController {
    @Autowired
    private BankManager manager;

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/players")
    public ResponseEntity createNewBankAccount(@PathVariable("gameid") int gameid, @RequestBody PlayerAndAmountDTO body) {
        String player = body.getPlayer().getId();
        int amount = body.getAmount();
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        try {
            b.createAccount(player, amount);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{gameid}/players/{playerid}")
    public Integer getAmount(@PathVariable("gameid") int gameid, @PathVariable("playerid") String playerid) {
        return manager.getBank(gameid).orElseThrow(NotFoundException::new).getBalance(playerid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/to/{to}/{amount}")
    public BankTransferResponse postTransferMoneyToAccount(@PathVariable("gameid") int gameid, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        manager.getBank(gameid).orElseThrow(NotFoundException::new).transferAmount("bank", to, amount, reason);
        return new BankTransferResponse(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/{amount}")
    public BankTransferResponse postTransferMoneyFromAccount(@PathVariable("gameid") int gameid, @PathVariable("from") String from, @PathVariable("amount") int amount, @RequestBody String reason) {
        manager.getBank(gameid).orElseThrow(NotFoundException::new).transferAmount(from, "bank", amount, reason);
        return new BankTransferResponse(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/to/{to}/{amount}")
    public BankTransferResponse postTransferMoneyFromAccountToAccount(@PathVariable("gameid") int gameid, @PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        b.transferAmount(from, to, amount, reason);

        return new BankTransferResponse(reason, new Player());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}")
    public void createNewBank(@PathVariable("gameid") int gameId, GameCreateDTO inCreateDTO) {
        manager.createBank(gameId, inCreateDTO.getComponents());
    }

}
