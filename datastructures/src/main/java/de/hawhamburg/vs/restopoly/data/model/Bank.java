package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Bank {
    private Map<String, BankAccount> accounts = new HashMap<>();
    private Collection<Transfer> transfers = new ArrayList<>();
    @JsonIgnore
    private Components components;

    public Bank(Components components) {
        this.components = components;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

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
        this.transferAmount(fromPlayer, toPlayer, amount, "Internal");
    }

    public void transferAmount(String fromPlayer, String toPlayer, int amount, String reason) {
        BankAccount fromAccount=accounts.get(fromPlayer);
        BankAccount toAccount=accounts.get(toPlayer);

        if(!fromPlayer.equals("bank")) {
            fromAccount.subtractAmount(amount);
        }

        if(!toPlayer.equals("bank")) {
            toAccount.addBalance(amount);
        }
        this.transfers.add(new Transfer(fromPlayer, toPlayer, amount, reason, ""));
    }

    public void createAccount(String player, int amount) {
        createAccount(player);
        addAmount(player,amount);
    }
}
