package de.hawhamburg.vs.restopoly.manager;


import de.hawhamburg.vs.restopoly.data.model.Bank;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankManager {
    private Map<Integer, Bank> banks = new HashMap<>();
    private int bankCount;

    public BankManager() {}

    public Bank createBank(int gameID) {
        if (banks.containsKey(gameID)) throw new RuntimeException("Bank already exists for game "+gameID);
        Bank b = new Bank();
        banks.put(gameID,b);
        return b;
    }

    public Bank getBank(int gameID) {
        if (banks.containsKey(gameID))
        return banks.get(gameID);
        else return createBank(gameID);
    }
}
