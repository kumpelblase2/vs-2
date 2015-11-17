package de.hawhamburg.vs.restopoly.responses;

import de.hawhamburg.vs.restopoly.model.Player;

/**
 * Created by JanDennis on 17.11.2015.
 */
public class PlayerAndAmountDTO {
    private Player player;
    private Integer amount;

    public PlayerAndAmountDTO() {
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getAmount() {
        return amount;
    }
}
