package de.hawhamburg.vs.restopoly.data.responses;

public class ThrowDTO {
    public Roll roll1;
    public Roll roll2;

    public ThrowDTO(Roll roll1, Roll roll2) {
        this.roll1 = roll1;
        this.roll2 = roll2;
    }

    public ThrowDTO() {
    }
}
