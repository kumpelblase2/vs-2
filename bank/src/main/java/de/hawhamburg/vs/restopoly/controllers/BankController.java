package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.dto.GameCreateDTO;
import de.hawhamburg.vs.restopoly.data.dto.TransfersDTO;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.data.model.*;
import de.hawhamburg.vs.restopoly.data.dto.PlayerAndAmountDTO;
import de.hawhamburg.vs.restopoly.manager.BankManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
public class BankController {
    private static final String ACCOUNT_URL = "/banks/{bankid}/players/{playerid}";
    private static final String BANKS_URL = "/banks/{bankid}";

    @Autowired
    private BankManager manager;
    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{bankid}/players")
    public ResponseEntity createNewBankAccount(@PathVariable("bankid") int gameid, @RequestBody PlayerAndAmountDTO body,
                                               UriComponentsBuilder uriBuilder) {
        String player = body.getPlayer().getId();
        int amount = body.getAmount();
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        try {
            b.createAccount(player, amount);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriBuilder.path(ACCOUNT_URL).buildAndExpand(gameid, player).toUriString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = ACCOUNT_URL)
    public Integer getAmount(@PathVariable("bankid") int gameid, @PathVariable("playerid") String playerid) {
        return manager.getBank(gameid).orElseThrow(NotFoundException::new).getBalance(playerid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{bankid}/transfer/to/{to}/{amount}")
    public Event postTransferMoneyToAccount(@PathVariable("bankid") int gameid, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        int transfer = b.transferAmount("bank", to, amount, reason);
        Event ev = new Event("Transfer " + amount + " from bank to " + to, "bank-transfer", reason, "/banks/" + gameid + "/transfers/" + transfer, "/game/" + gameid + "/players/" + to, "");
        createTransfer(b.getComponents(), gameid, ev);
        return ev;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{bankid}/transfer/from/{from}/{amount}")
    public Event postTransferMoneyFromAccount(@PathVariable("bankid") int gameid, @PathVariable("from") String from, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(gameid).orElseThrow(NotFoundException::new);
        int transfer = b.transferAmount(from, "bank", amount, reason);
        Event ev = new Event("Transfer " + amount + " from " + from + " to bank", "bank-transfer", reason, "/banks/" + gameid + "/transfers/" + transfer, "/game/" + gameid + "/players/" + from, "");
        createTransfer(b.getComponents(), gameid, ev);
        return ev;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks/{bankid}/transfer/from/{from}/to/{to}/{amount}")
    public Event postTransferMoneyFromAccountToAccount(@PathVariable("bankid") int bankid, @PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("amount") int amount, @RequestBody String reason) {
        Bank b = manager.getBank(bankid).orElseThrow(NotFoundException::new);
        int transfer = b.transferAmount(from, to, amount, reason);
        Event ev = new Event("Transfer " + amount + " from " + from + " to " + to, "bank-transfer", reason, "/banks/" + bankid + "/transfers/" + transfer, b.getComponents().getGame() + "/players/" + from, "");
        createTransfer(b.getComponents(), bankid, ev);
        return ev;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/banks")
    public void createNewBank(GameCreateDTO inCreateDTO, UriComponentsBuilder uriBuilder,
                              HttpServletResponse response) {
        Bank b = manager.createBank(inCreateDTO.getComponents());
        response.setHeader("Location", uriBuilder.path(BANKS_URL).buildAndExpand(b.getId()).toUriString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{bankid}/transfers/{transfer}")
    public Transfer getTransfer(@PathVariable("bankid") int gameId, @PathVariable("transfer") int transferId) {
        return this.manager.getBank(gameId).map(b -> b.getTransfer(transferId)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/banks/{bankid}/transfers")
    public TransfersDTO getTransfers(@PathVariable("bankid") int gameId) {
        return this.manager.getBank(gameId).map(Bank::getTransfers).map(tr -> new TransfersDTO(gameId, tr)).orElseThrow(NotFoundException::new);
    }

    private void createTransfer(Components components, int gameid, Event ev) {
        try {
            String result = this.restTemplate.postForObject(components.getEvents() + "/events?gameid=" + gameid, ev, String.class);
            ev.setUri(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
