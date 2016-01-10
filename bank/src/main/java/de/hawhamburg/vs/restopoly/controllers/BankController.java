package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.dto.GameCreateDTO;
import de.hawhamburg.vs.restopoly.data.dto.TransfersDTO;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.data.model.*;
import de.hawhamburg.vs.restopoly.data.dto.PlayerAndAmountDTO;
import de.hawhamburg.vs.restopoly.manager.BankManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class BankController {
    @Autowired
    private BankManager manager;
    private final RestTemplate restTemplate = new RestTemplate();

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
    public Event postTransferMoneyToAccount(@PathVariable("gameid") int gameid, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        int transfer = b.transferAmount("bank", to, amount, reason);
        Event ev = new Event("Transfer " + amount + " from bank to " + to, "bank-transfer", reason, "/banks/" + gameid + "/transfers/" + transfer, "/game/" + gameid + "/players/" + to, "");
        createTransfer(b.getComponents(), gameid, ev);
        return ev;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/{amount}")
    public Event postTransferMoneyFromAccount(@PathVariable("gameid") int gameid, @PathVariable("from") String from, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        int transfer = b.transferAmount(from, "bank", amount, reason);
        Event ev = new Event("Transfer " + amount + " from " + from + " to bank", "bank-transfer", reason, "/banks/" + gameid + "/transfers/" + transfer, "/game/" + gameid + "/players/" + from, "");
        createTransfer(b.getComponents(), gameid, ev);
        return ev;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{gameid}/transfer/from/{from}/to/{to}/{amount}")
    public Event postTransferMoneyFromAccountToAccount(@PathVariable("gameid") int gameid, @PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        int transfer = b.transferAmount(from, to, amount, reason);
        Event ev = new Event("Transfer " + amount + " from " + from + " to " + to, "bank-transfer", reason, "/banks/" + gameid + "/transfers/" + transfer, "/game/" + gameid + "/players/" + from, "");
        createTransfer(b.getComponents(), gameid, ev);
        return ev;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/banks/{gameid}")
    public void createNewBank(@PathVariable("gameid") int gameId, GameCreateDTO inCreateDTO) {
        manager.createBank(gameId, inCreateDTO.getComponents());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{gameid}/transfers/{transfer}")
    public Transfer getTransfer(@PathVariable("gameid") int gameId, @PathVariable("transfer") int transferId) {
        return this.manager.getBank(gameId).map(b -> b.getTransfer(transferId)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{gameid}/transfers")
    public TransfersDTO getTransfers(@PathVariable("gameid") int gameId) {
        return this.manager.getBank(gameId).map(Bank::getTransfers).map(tr -> new TransfersDTO(gameId, tr)).orElseThrow(NotFoundException::new);
    }

    private void createTransfer(Components components, int gameid, Event ev) {
        String result = this.restTemplate.postForObject(components.getEvents() + "/events?gameid=" + gameid, ev, String.class);
        ev.setUri(result);
    }
}
