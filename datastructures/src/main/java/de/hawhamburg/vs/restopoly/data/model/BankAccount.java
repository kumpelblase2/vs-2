package de.hawhamburg.vs.restopoly.data.model;

/**
 * Created by JanDennis on 17.11.2015.
 */
public class BankAccount {
    private String playerID;
    private int balance;

    public BankAccount() {}

    public BankAccount(String playerID) {
        this.playerID = playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public void addBalance(int amount) {
        if (Math.abs(amount) != amount) throw new IllegalArgumentException("Amount has to be positive!");
        this.balance += amount;
    }

    public void subtractAmount(int amount) {
        if (Math.abs(amount) != amount) throw new IllegalArgumentException("Amount has to be positive!");
        this.balance = Math.max(0,balance-amount);
    }

    public int getBalance() {
        return balance;
    }
}
