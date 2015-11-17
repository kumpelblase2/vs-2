package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.Player;

/**
 * Created by JanDennis on 17.11.2015.
 */
public class BankTransferResponce {
    private String type = "Tranfer";
    private String name = "Wolfgang";
    private String reason;
    private String resource = "Not Given";
    private Player player;

    public BankTransferResponce(String reason, Player player) {
        this.reason = reason;
        this.player = player;
    }
}
