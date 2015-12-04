package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.data.model.Broker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by JanDennis on 04.12.2015.
 */
@Component
public class BrokerManager {
    private Map<Integer, Broker> brokers = new HashMap<>();


    public Broker createBroker(int gameid) {
        return this.brokers.putIfAbsent(gameid,new Broker());
    }

    public Optional<Broker> getBroker(int gameId) {
        return Optional.ofNullable(this.brokers.get(gameId));
    }
}
