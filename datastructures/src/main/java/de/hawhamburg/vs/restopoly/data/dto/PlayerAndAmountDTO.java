package de.hawhamburg.vs.restopoly.data.dto;

public class PlayerAndAmountDTO {
    private String player;
    private Integer amount;

    public PlayerAndAmountDTO() {
    }

    public PlayerAndAmountDTO(String player, Integer amount) {
        this.player = player;
        this.amount = amount;
    }

    public String getPlayer() {
        return player;
    }

    public Integer getAmount() {
        return amount;
    }
}
