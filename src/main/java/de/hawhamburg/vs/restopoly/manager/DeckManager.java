package de.hawhamburg.vs.restopoly.manager;

import de.hawhamburg.vs.restopoly.components.DeckFactory;
import de.hawhamburg.vs.restopoly.model.Card;
import de.hawhamburg.vs.restopoly.model.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Component
public class DeckManager {

    @Autowired
    private DeckFactory factory;
    private Map<Integer, Deck> decks = new HashMap<>();

    public void createDeckForGame(int inGameID) {
        this.decks.putIfAbsent(inGameID, this.factory.createDeck());
    }

    public Optional<Card> drawCommunity(int inGameID) {
        return getDeck(inGameID).map(Deck::drawCommunityCard);
    }

    private Optional<Deck> getDeck(int inGameID) {
        return Optional.ofNullable(this.decks.get(inGameID));
    }

    public Optional<Card> drawChance(int inGameId) {
        return getDeck(inGameId).map(Deck::drawChangeCard);
    }
}
