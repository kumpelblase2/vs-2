package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.data.model.Event;
import de.hawhamburg.vs.restopoly.data.model.Subscription;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EventManager {
    private Map<Integer, List<Event>> events = new HashMap<>();
    private Map<Integer, Set<Subscription>> subscriptions = new HashMap<>();

    public void clearEventsOfGame(int gameid) {
        this.events.remove(gameid);
    }

    public void clearSubscriptions(int gameid) {
        this.subscriptions.remove(gameid);
    }

    public Set<Subscription> getSubscribersFor(int gameid) {
        return this.subscriptions.get(gameid);
    }

    public void addEvent(int gameid, Event event) {
        List<Event> current = this.events.getOrDefault(gameid, new ArrayList<>());
        current.add(event);
        this.events.put(gameid, current);
    }

    public void addSubscription(int gameid, Subscription subscription) {
        Set<Subscription> subs = this.subscriptions.getOrDefault(gameid, new HashSet<>());
        subs.add(subscription);
        this.subscriptions.put(gameid, subs);
    }

    public void removeSubscription(int gameid, Subscription subscription) {
        this.subscriptions.getOrDefault(gameid, new HashSet<>()).remove(subscription);
    }

    public List<Event> getEventsOf(int gameid) {
        return this.events.getOrDefault(gameid, new ArrayList<>());
    }

    public Set<Subscription> getAllSubscriptions() {
        return this.subscriptions.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
