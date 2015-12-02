package de.hawhamburg.vs.restopoly.data.responses;

import de.hawhamburg.vs.restopoly.data.model.Player;

/**
 * Created by JanDennis on 17.11.2015.
 */
public class BankTransferResponse {
    private String type = "Tranfer";
    private String name = "Wolfgang";
    private String reason;
    private String resource = "Not Given";
    private Player player;

    public BankTransferResponse() {
    }

    public BankTransferResponse(String reason, Player player) {
        this.reason = reason;
        this.player = player;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
