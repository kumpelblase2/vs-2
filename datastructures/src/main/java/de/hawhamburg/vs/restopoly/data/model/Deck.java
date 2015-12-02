package de.hawhamburg.vs.restopoly.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck implements Cloneable {
    private List<Card> community;
    private List<Card> chance;

    private List<Card> usedCommunity = new ArrayList<>();
    private List<Card> usedChance = new ArrayList<>();

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
        if(this.community.size() == 0) {
            this.community.addAll(this.usedCommunity);
            this.usedCommunity.clear();
        }
        Card drawn = community.remove(new Random().nextInt(community.size()));
        this.usedCommunity.add(drawn);
        return drawn;
    }

    public Card drawChangeCard() {
        if(this.chance.size() == 0) {
            this.chance.addAll(this.usedChance);
            this.usedChance.clear();
        }

        Card drawn = chance.remove(new Random().nextInt(chance.size()));
        this.usedChance.add(drawn);
        return drawn;
    }

    @Override
    public Object clone() {
        List<Card> community = new ArrayList<>(this.community);
        List<Card> chance = new ArrayList<>(this.chance);
        return new Deck(community, chance);
    }
}
