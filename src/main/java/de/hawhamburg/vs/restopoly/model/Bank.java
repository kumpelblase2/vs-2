package de.hawhamburg.vs.restopoly.model;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by JanDennis on 17.11.2015.
 */
public class Bank {
    private Map<String, BankAccount> accounts;

    public Bank() {}

    public void createAccount(String playerID) {
        if (accounts.containsKey(playerID)) throw new RuntimeException("Account already exists for playerID "+playerID);
        else {
            accounts.put(playerID,new BankAccount(playerID));
        }
    }

    public int getBalance(String playerID) {
        if (accounts.containsKey(playerID)) {
            return accounts.get(playerID).getBalance();
        } else {
            throw new NoSuchElementException("No Account found for Player "+playerID);
        }
    }

    public void addAmount(String playerID, int amount) {
        accounts.get(playerID).addBalance(amount);
    }

    public void subtractAmount(String playerID, int amount) {
        accounts.get(playerID).subtractAmount(amount);
    }

    public void transferAmount(String fromPlayer, String toPlayer, int amount) {
        BankAccount fromAccount=accounts.get(fromPlayer);
        BankAccount toAccount=accounts.get(toPlayer);

        fromAccount.subtractAmount(amount);
        toAccount.addBalance(amount);
    }


    public void createAccount(String player, int amount) {
        createAccount(player);
        addAmount(player,amount);
    }
}
