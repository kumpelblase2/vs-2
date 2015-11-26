package de.hawhamburg.vs.restopoly.components;

import com.google.gson.Gson;
import de.hawhamburg.vs.restopoly.model.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

@Component
public class DeckFactory {
    private Deck defaultDeck;

    @Autowired
    public DeckFactory(@Value("${deck.location}") String inDeckLocation) throws FileNotFoundException {
        this.defaultDeck = new Gson().fromJson(new FileReader(new File(inDeckLocation)), Deck.class);
        this.defaultDeck = new Deck(new ArrayList<>(), new ArrayList<>());
    }

    public Deck createDeck() {
        return (Deck) defaultDeck.clone();
    }
}
