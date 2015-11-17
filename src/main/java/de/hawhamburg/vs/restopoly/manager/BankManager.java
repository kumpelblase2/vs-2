package de.hawhamburg.vs.restopoly.manager;


import de.hawhamburg.vs.restopoly.model.Bank;
import de.hawhamburg.vs.restopoly.model.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankManager {
    private Map<Game, Bank> banks = new HashMap<>();
    private int bankCount;

    public BankManager() {}

    public Bank createBank(Game gameID) {
        if (banks.containsKey(gameID)) throw new RuntimeException("Bank already exists for game "+gameID);
        Bank b = new Bank();
        banks.put(gameID,b);
        return b;
    }

    public Bank getBank(Game gameID) {
        return banks.get(gameID);
    }
}
