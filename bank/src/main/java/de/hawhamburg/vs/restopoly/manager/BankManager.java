package de.hawhamburg.vs.restopoly.manager;


import de.hawhamburg.vs.restopoly.data.model.Bank;
import de.hawhamburg.vs.restopoly.data.model.Components;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class BankManager {
    private Map<Integer, Bank> banks = new HashMap<>();
    private int bankCount;

    public BankManager() {}

    public Bank createBank(int gameID, Components components) {
        if (banks.containsKey(gameID)) throw new RuntimeException("Bank already exists for game "+gameID);
        Bank b = new Bank(components);
        banks.put(gameID,b);
        return b;
    }

    public Optional<Bank> getBank(int gameID) {
        return Optional.ofNullable(banks.get(gameID));
    }
}
