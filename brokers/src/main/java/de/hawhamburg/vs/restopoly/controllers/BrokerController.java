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
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class BrokerController {
    private static final String BANK_TRANSFER_URL = "/%d/transfer/from/%s/to/%s/%d";
    private static final String BANK_BUY_URL = "/%d/transfer/from/%s/%d";

    private static final String BROKER_URL = "/broker/{brokerid}";

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
    @RequestMapping(method = RequestMethod.POST, value = "/broker")
    public void createBroker(UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        Broker b = brokerManager.createBroker();
        response.setHeader("Location", uriBuilder.path(BROKER_URL).buildAndExpand(b.getId()).toUriString());
    }

    // Create Places
    @RequestMapping(method = RequestMethod.PUT, value = "/broker/{brokerId}/places/{placeid}")
    public ResponseEntity<String> putNewPlace(@PathVariable("brokerId") int brokerId, @PathVariable("placeid") String placeid,
                                              @RequestBody Estate newPlace) {
        Optional<Broker> broker = brokerManager.getBroker(brokerId);
        Broker br = broker.orElseThrow(NotFoundException::new);
        String uri = "/broker/" + brokerId + "/places/" + placeid;
        if(br.hasPlace(placeid)) {
            return new ResponseEntity<>(uri, HttpStatus.OK);
        } else {
            br.addPlace(placeid, newPlace);
            return new ResponseEntity<String>(uri, HttpStatus.CREATED);
        }
    }

    // Player vistited Place
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/broker/{brokerId}/places/{placeid}/visit/{playerid}")
    public Collection<Event> postPlayerVisitsPlace(@PathVariable("brokerId") int brokerId, @PathVariable("placeid") String placeid, @PathVariable("playerid") String player) {
        Broker broker = brokerManager.getBroker(brokerId).get();
        String owner = broker.getOwner(placeid).getId();
        if (!owner.equals(player)) {
            int amount = broker.getRent(placeid);
            String url = String.format(BANK_TRANSFER_URL, brokerId, player, owner, amount);
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
    @RequestMapping(method = RequestMethod.GET, value = "/broker/{brokerId}/places/{placeid}/owner")
    public Player getOwner(@PathVariable("brokerId") int brokerId, @PathVariable("placeid") String place) {
        Player pl = brokerManager.getBroker(brokerId).get().getOwner(place);
        if (pl == null)
            throw new NotFoundException();

        return pl;
    }

    //ToDo Add Events
    // Change Owner
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/broker/{brokerId}/places/{placeid}/owner")
    public Collection<Event> changeOwner(@PathVariable("brokerId") int brokerId, @PathVariable("placeid") String place, @RequestBody Player player) {
        Broker broker = brokerManager.getBroker(brokerId).get();

        if (!broker.hasPlace(place))
            throw new NotFoundException();

        Player owner = broker.getOwner(place);
        if (owner == null || !owner.getId().equals(player.getId())) {
            String url = String.format(BANK_TRANSFER_URL, brokerId, player, owner, broker.getValue(place));
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
}
