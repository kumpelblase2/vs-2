package de.hawhamburg.vs.restopoly.manager;


import de.hawhamburg.vs.restopoly.data.model.Bank;
import de.hawhamburg.vs.restopoly.data.model.Components;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BankManager {
    private Set<Bank> banks = new HashSet<>();
    private AtomicInteger bankCount = new AtomicInteger();

    public BankManager() {}

    public Bank createBank(Components components) {
        int id = this.bankCount.incrementAndGet();
        Bank b = new Bank(id, components);
        this.banks.add(b);
        return b;
    }

    public Optional<Bank> getBank(int bankId) {
        return this.banks.parallelStream().filter(b -> b.getId() == bankId).findFirst();
    }
}
