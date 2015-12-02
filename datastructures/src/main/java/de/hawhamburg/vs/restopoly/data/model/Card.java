package de.hawhamburg.vs.restopoly.data.model;

public class Card {
    private String title;
    private String text;

    public Card() {
    }

    public Card(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
}
