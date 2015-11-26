package de.hawhamburg.vs.restopoly.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck implements Cloneable {
    private List<Card> community;
    private List<Card> chance;

    public Deck() {}

    public Deck(List<Card> communityDeck, List<Card> changeDeck) {
        this.community = communityDeck;
        this.chance = changeDeck;
    }

    public List<Card> getCommunity() {
        return community;
    }

    public List<Card> getChance() {
        return chance;
    }

    public void setCommunity(List<Card> community) {
        this.community = community;
    }

    public void setChance(List<Card> chance) {
        this.chance = chance;
    }

    public Card drawCommunityCard() {
        return community.get(new Random().nextInt(community.size()));
    }

    public Card drawChangeCard() {
        return chance.get(new Random().nextInt(chance.size()));
    }

    @Override
    public Object clone() {
        List<Card> community = new ArrayList<>(this.community);
        List<Card> chance = new ArrayList<>(this.chance);
        return new Deck(community, chance);
    }
}
