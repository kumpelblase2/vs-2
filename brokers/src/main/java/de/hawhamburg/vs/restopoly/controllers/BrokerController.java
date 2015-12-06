package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.errors.AlreadyExistsException;
import de.hawhamburg.vs.restopoly.data.model.Broker;
import de.hawhamburg.vs.restopoly.data.model.Estate;
import de.hawhamburg.vs.restopoly.data.model.Event;
import de.hawhamburg.vs.restopoly.data.model.Player;
import de.hawhamburg.vs.restopoly.manager.BrokerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by JanDennis on 04.12.2015.
 */

@RestController
public class BrokerController {
    @Autowired
    private BrokerManager brokerManager;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT,value = "/brokers/{gameid}")
    public void createBoard(@PathVariable("gameid") int gameid) {
        if(this.brokerManager.getBroker(gameid).isPresent()) {
            throw new AlreadyExistsException();
        } else {
            this.brokerManager.createBroker(gameid);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/broker/{gameid}/places/{placeid}")
    public ResponseEntity<String> putNewPlace(@PathVariable("gameid") int gameid, @PathVariable("placeid") String placeid,
                                              @RequestBody Estate newPlace) {
        Optional<Broker> broker = brokerManager.getBroker(gameid);
        if (broker.isPresent()) (broker.get()).addPlace(placeid,newPlace);
        return new ResponseEntity<String>(placeid,HttpStatus.OK);
        //ToDO 201 Status bei neuerstellung
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/brokers/{gameid}/places/{placeid}/visit/{playerid}")
    public Collection<Event> postPlayerVisitsPlace(@PathVariable("gameid") int gameid, @PathVariable("placeid") String placeid, @PathVariable("playerid") String player) {
        //ToDo Event
        return null;
    }

    @RequestMapping("/brokers/{gameid}/places/{placeid}/owner")
    public ResponseEntity<Player> getOwner(@PathVariable("gameid") int gameid, @PathVariable("placeid") String place) {
        Player pl = brokerManager.getBroker(gameid).get().getOwner(place);
        if (pl==null) return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Player>(pl,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/broker/{gameid}/places/{placeid}/owner")
    public ResponseEntity<Event> buyPlace(@PathVariable("gameid") int gameid,@PathVariable("placeid") String place) {
        Broker broker = brokerManager.getBroker(gameid).get();
        if (broker.getOwner(place)==null) {
           // broker.setOwner(place,);
        }
        //TODO
        return null;
    }
}
