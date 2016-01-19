package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.data.model.Broker;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BrokerManager {
    private Set<Broker> brokers = new HashSet<>();
    private AtomicInteger brokerCount = new AtomicInteger();
//    private Map<Integer, Broker> brokers = new HashMap<>();


    public Broker createBroker() {
        int id = this.brokerCount.incrementAndGet();
        Broker b = new Broker(id);
        this.brokers.add(b);
        return b;
    }

    public Optional<Broker> getBroker(int brokerId) {
        return this.brokers.parallelStream().filter(bro -> bro.getId() == brokerId).findFirst();
    }
}
