package de.hawhamburg.vs.restopoly.components;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DiceComponent {
    private final Random random = new Random();

    public int roll() {
        return random.nextInt(6) + 1;
    }
}
