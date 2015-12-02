package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.components.DiceComponent;
import de.hawhamburg.vs.restopoly.data.responses.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiceController {
    @Autowired
    private DiceComponent dice;

    @RequestMapping("/dice")
    public Roll getRoll() {
        return new Roll(dice.roll());
    }
}
