package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Player;

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
