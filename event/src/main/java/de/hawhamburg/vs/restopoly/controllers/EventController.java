package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.data.model.Event;
import de.hawhamburg.vs.restopoly.data.model.Subscription;
import de.hawhamburg.vs.restopoly.manager.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class EventController {
    private static final String EVENTS_URL = "/events/{eventid}";

    @Autowired
    private EventManager manager;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/events")
    public String emitEvent(@RequestParam("gameid") int gameid, @RequestBody Event event, UriComponentsBuilder uriBuilder) {
        int eventId = this.manager.addEvent(gameid, event);
        event.setUri(uriBuilder.path(EVENTS_URL).buildAndExpand(eventId).toUriString());
        this.manager.publishEvent(gameid, event);
        return "/events/" + eventId;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/events/subscriptions")
    public void subscribe(@RequestBody Subscription subscription) {
        this.manager.addSubscription(subscription.getGameid(), subscription);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/events/subscriptions")
    public Set<Subscription> getSubscriptions(@RequestParam(value = "gameid", required = false) Integer gameid) {
        if(gameid != null) {
            return this.manager.getSubscribersFor(gameid);
        } else {
            return this.manager.getAllSubscriptions();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/events")
    public List<String> getPastEvents(@RequestParam("gameid") int gameid) {
        return this.manager.getEventsOf(gameid).stream().map(e -> "/events/" + e.getId()).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/events")
    public void clearEvents(@RequestParam("gameid") int gameid) {
        this.manager.clearEventsOfGame(gameid);
    }

    @RequestMapping(method = RequestMethod.GET, value = EVENTS_URL)
    public Event getEvent(@PathVariable("eventid") int eventID) {
        Event ev = this.manager.findEvent(eventID);
        if(ev == null) {
            throw new NotFoundException();
        }

        return ev;
    }
}
