package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.ServiceRegistrator;
import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.data.errors.OwnedByYourselfException;
import de.hawhamburg.vs.restopoly.data.model.Broker;
import de.hawhamburg.vs.restopoly.data.model.Estate;
import de.hawhamburg.vs.restopoly.data.model.Event;
import de.hawhamburg.vs.restopoly.data.model.Player;
import de.hawhamburg.vs.restopoly.manager.BrokerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
public class BrokerController {
    private static final String BANK_TRANSFER_URL = "/%d/transfer/from/%s/to/%s/%d";
    private static final String BANK_BUY_URL = "/%d/transfer/from/%s/%d";

    @Autowired
    private BrokerManager brokerManager;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${main_service}")
    private String mainServiceUrl;

    private String bankServiceUrl;

    @PostConstruct
    public void init() {
        this.bankServiceUrl = ServiceRegistrator.lookupService(this.mainServiceUrl, "banks");
    }

    // Create Broker
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT, value = "/brokers/{gameid}")
    public void createBroker(@PathVariable("gameid") int gameid) {
        if (this.brokerManager.getBroker(gameid).isPresent()) {
            throw new AlreadyExistsException();
        } else {
            this.brokerManager.createBroker(gameid);
        }
    }

    // Create Places
    @RequestMapping(method = RequestMethod.PUT, value = "/broker/{gameid}/places/{placeid}")
    public ResponseEntity<String> putNewPlace(@PathVariable("gameid") int gameid, @PathVariable("placeid") String placeid,
                                              @RequestBody Estate newPlace) {
        Optional<Broker> broker = brokerManager.getBroker(gameid);
        Broker br = broker.orElseThrow(NotFoundException::new);
        String uri = "/broker/" + gameid + "/places/" + placeid;
        if(br.hasPlace(placeid)) {
            return new ResponseEntity<>(uri, HttpStatus.OK);
        } else {
            br.addPlace(placeid, newPlace);
            return new ResponseEntity<String>(uri, HttpStatus.CREATED);
        }
    }

    // Player vistited Place
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/brokers/{gameid}/places/{placeid}/visit/{playerid}")
    public Collection<Event> postPlayerVisitsPlace(@PathVariable("gameid") int gameid, @PathVariable("placeid") String placeid, @PathVariable("playerid") String player) {
        Broker broker = brokerManager.getBroker(gameid).get();
        String owner = broker.getOwner(placeid).getId();
        if (!owner.equals(player)) {
            int amount = broker.getRent(placeid);
            String url = String.format(BANK_TRANSFER_URL, gameid, player, owner, amount);
            try {
                restTemplate.postForLocation(bankServiceUrl + url, "Player " + player + " visited " + placeid + " and paid " + amount + " rent");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    // Returns Owner
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/brokers/{gameid}/places/{placeid}/owner")
    public Player getOwner(@PathVariable("gameid") int gameid, @PathVariable("placeid") String place) {
        Player pl = brokerManager.getBroker(gameid).get().getOwner(place);
        if (pl == null)
            throw new NotFoundException();

        return pl;
    }

    //ToDo Add Events
    // Change Owner
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/broker/{gameid}/places/{placeid}/owner")
    public Collection<Event> changeOwner(@PathVariable("gameid") int gameid, @PathVariable("placeid") String place, @RequestBody Player player) {
        Broker broker = brokerManager.getBroker(gameid).get();
        String owner = broker.getOwner(place).getId();
        if (!broker.hasPlace(place))
            throw new NotFoundException();

        if (!owner.equals(player.getId())) {
            String url = String.format(BANK_TRANSFER_URL, gameid, player, owner, broker.getValue(place));
            try {
                restTemplate.postForLocation(url, "Player " + player + " bought " + place);
            } catch (Exception e) {
                e.printStackTrace();
            }
            broker.setOwner(place, player);
            return new ArrayList<>();
        }
        //ToDo Event ?
        throw new OwnedByYourselfException();
    }

    // Buy Place, fail if not for Sale (Already owned by someone)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/broker/{gameid}/places/{placeid}/owner")
    public Event buyPlace(@PathVariable("gameid") int gameid, @PathVariable("placeid") String place, @RequestBody Player player) {
        Broker broker = brokerManager.getBroker(gameid).get();
        if (broker.getOwner(place) == null) {
            broker.setOwner(place, player);
            String url = String.format(BANK_BUY_URL,gameid,player.getId(),broker.getValue(place));
            try {
                restTemplate.postForLocation(url, "Player " + player.getName() + " bought " + place + "from Bank");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        //TODO
        throw new AlreadyExistsException();
    }
}
