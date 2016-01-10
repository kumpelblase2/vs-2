package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.data.model.Event;
import de.hawhamburg.vs.restopoly.data.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class EventManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventManager.class);

    private AtomicInteger idCount;
    private Collection<Event> events;
    private Map<Integer, Set<Subscription>> subscriptions = new HashMap<>();
    private final RestTemplate restTemplate;

    public EventManager() {
        this.restTemplate = new RestTemplate();
    }

    public void clearEventsOfGame(int gameid) {
        this.events.removeIf(e -> e.getGameid() == gameid);
    }

    public void clearSubscriptions(int gameid) {
        this.subscriptions.remove(gameid);
    }

    public Set<Subscription> getSubscribersFor(int gameid) {
        return this.subscriptions.get(gameid);
    }

    public int addEvent(int gameid, Event event) {
        event.setGameid(gameid);
        event.setId(idCount.incrementAndGet());
        this.events.add(event);
        return event.getId();
    }

    public void addSubscription(int gameid, Subscription subscription) {
        Set<Subscription> subs = this.subscriptions.getOrDefault(gameid, new HashSet<>());
        subs.add(subscription);
        this.subscriptions.putIfAbsent(gameid, subs);
    }

    public void removeSubscription(int gameid, Subscription subscription) {
        this.subscriptions.getOrDefault(gameid, new HashSet<>()).remove(subscription);
    }

    public List<Event> getEventsOf(int gameid) {
        return this.events.stream().filter(e -> e.getGameid() == gameid).collect(Collectors.toList());
    }

    public Set<Subscription> getAllSubscriptions() {
        return this.subscriptions.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public int publishEvent(int gameid, Event event) {
        int id = this.addEvent(gameid, event);
        this.getSubscribersFor(gameid).stream()
                .filter(sub -> sub.getEvent().isSame(event) && sub.hasValidUri()).forEach(sub -> {
            try {
                this.restTemplate.postForLocation(sub.getUri(), event);
            } catch(Exception e) {
                LOGGER.warn("Couldn't send event to uri " + sub.getUri() + " : " + e.getMessage());
            }
        });

        return id;
    }
}
