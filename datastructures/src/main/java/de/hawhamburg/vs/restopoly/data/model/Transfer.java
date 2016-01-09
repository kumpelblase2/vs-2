package de.hawhamburg.vs.restopoly.data.model;

public class Transfer {
    private String from;
    private String to;
    private int amount;
    private String reason;
    private String event;

    public Transfer(String from, String to, int amount, String reason, String event) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.reason = reason;
        this.event = event;
    }

    public Transfer() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        if (amount != transfer.amount) return false;
        if (from != null ? !from.equals(transfer.from) : transfer.from != null) return false;
        if (to != null ? !to.equals(transfer.to) : transfer.to != null) return false;
        if (reason != null ? !reason.equals(transfer.reason) : transfer.reason != null) return false;
        return event != null ? event.equals(transfer.event) : transfer.event == null;

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + amount;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }
}
