package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.components.DiceComponent;
import de.hawhamburg.vs.restopoly.data.dto.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DiceController {
    @Autowired
    private DiceComponent dice;

    @RequestMapping(method = RequestMethod.GET, value = "/dice")
    public Roll getRoll() {
        return new Roll(dice.roll());
    }
}
