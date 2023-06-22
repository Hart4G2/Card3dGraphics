package com.mygdx.game.objects.card;

public enum Suit {
    Clubs("clubs", 0), Diamonds("diamonds", 1), Hearts("hearts", 2), Spades("spades", 3);
    public final String name;
    public final int index;
    Suit(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
