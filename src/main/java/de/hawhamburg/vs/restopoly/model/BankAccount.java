package de.hawhamburg.vs.restopoly.model;

/**
 * Created by JanDennis on 17.11.2015.
 */
public class BankAccount {
    private Player playerID;
    private int balance;

    public BankAccount() {}

    public BankAccount(Player playerID) {
        this.playerID = playerID;
    }

    public void setPlayerID(Player playerID) {
        this.playerID = playerID;
    }

    public void addBalance(int amount) {
        if (Math.abs(amount) != amount) throw new IllegalArgumentException("Amount has to be positive!");
        this.balance += amount;
    }

    public void subtractAmount(int amount) {
        if (Math.abs(amount) != amount) throw new IllegalArgumentException("Amount has to be positive!");
        this.balance =Math.max(0,balance-amount);
    }

    public int getBalance() {
        return balance;
    }
}
