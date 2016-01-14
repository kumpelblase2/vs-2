package de.hawhamburg.vs.restopoly.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Bank {
    private int id;
    private Map<String, BankAccount> accounts = new HashMap<>();
    private List<Transfer> transfers = new ArrayList<>();
    @JsonIgnore
    private Components components;

    public Bank(int id, Components components) {
        this.components = components;
        this.id = id;
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

    public int transferAmount(String fromPlayer, String toPlayer, int amount, String reason) {
        BankAccount fromAccount=accounts.get(fromPlayer);
        BankAccount toAccount=accounts.get(toPlayer);

        if(!fromPlayer.equals("bank")) {
            fromAccount.subtractAmount(amount);
        }

        if(!toPlayer.equals("bank")) {
            toAccount.addBalance(amount);
        }
        this.transfers.add(new Transfer(fromPlayer, toPlayer, amount, reason, ""));
        return this.transfers.size() - 1;
    }

    public void createAccount(String player, int amount) {
        createAccount(player);
        addAmount(player,amount);
    }

    public Transfer getTransfer(int transferId) {
        return this.transfers.get(transferId);
    }

    public List<Transfer> getTransfers() {
        return this.transfers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (getId() != bank.getId()) return false;
        if (accounts != null ? !accounts.equals(bank.accounts) : bank.accounts != null) return false;
        if (getTransfers() != null ? !getTransfers().equals(bank.getTransfers()) : bank.getTransfers() != null)
            return false;
        return getComponents() != null ? getComponents().equals(bank.getComponents()) : bank.getComponents() == null;

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        result = 31 * result + (getTransfers() != null ? getTransfers().hashCode() : 0);
        result = 31 * result + (getComponents() != null ? getComponents().hashCode() : 0);
        return result;
    }
}
