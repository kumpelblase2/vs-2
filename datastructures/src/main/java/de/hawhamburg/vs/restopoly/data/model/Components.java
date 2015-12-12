package de.hawhamburg.vs.restopoly.data.model;

public class Components {
    private String board;
    private String game;
    private String dice;
    private String bank;
    private String broker;
    private String decks;
    private String events;

    public Components() {
    }

    public Components(String board, String game, String dice, String bank, String broker, String decks, String events) {
        this.board = board;
        this.game = game;
        this.dice = dice;
        this.bank = bank;
        this.broker = broker;
        this.decks = decks;
        this.events = events;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDice() {
        return dice;
    }

    public void setDice(String dice) {
        this.dice = dice;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getDecks() {
        return decks;
    }

    public void setDecks(String decks) {
        this.decks = decks;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Components that = (Components) o;

        if (board != null ? !board.equals(that.board) : that.board != null) return false;
        if (game != null ? !game.equals(that.game) : that.game != null) return false;
        if (dice != null ? !dice.equals(that.dice) : that.dice != null) return false;
        if (bank != null ? !bank.equals(that.bank) : that.bank != null) return false;
        if (broker != null ? !broker.equals(that.broker) : that.broker != null) return false;
        if (decks != null ? !decks.equals(that.decks) : that.decks != null) return false;
        return events != null ? events.equals(that.events) : that.events == null;

    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + (dice != null ? dice.hashCode() : 0);
        result = 31 * result + (bank != null ? bank.hashCode() : 0);
        result = 31 * result + (broker != null ? broker.hashCode() : 0);
        result = 31 * result + (decks != null ? decks.hashCode() : 0);
        result = 31 * result + (events != null ? events.hashCode() : 0);
        return result;
    }
}
