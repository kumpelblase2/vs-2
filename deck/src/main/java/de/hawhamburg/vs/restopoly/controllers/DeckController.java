package de.hawhamburg.vs.restopoly.controllers;

import de.hawhamburg.vs.restopoly.data.model.Card;
import de.hawhamburg.vs.restopoly.data.errors.NotFoundException;
import de.hawhamburg.vs.restopoly.manager.DeckManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeckController {
    @Autowired
    private DeckManager deckManager;

    @RequestMapping("/deck/{gameid}/community")
    public Card drawCommunityCard(@PathVariable("gameid") int gameId) {
        return deckManager.drawCommunity(gameId).orElseThrow(NotFoundException::new);
    }

    @RequestMapping("/deck/{gameid}/chance")
    public Card drawChanceCard(@PathVariable("gameid") int gameId) {
        return deckManager.drawChance(gameId).orElseThrow(NotFoundException::new);
    }
}
