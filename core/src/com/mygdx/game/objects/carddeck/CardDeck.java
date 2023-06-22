package com.mygdx.game.objects.carddeck;

import static com.mygdx.game.CardGraphics.CARD_HEIGHT;
import static com.mygdx.game.CardGraphics.CARD_WIDTH;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.objects.card.Card;
import com.mygdx.game.objects.card.Pip;
import com.mygdx.game.objects.card.Suit;

public class CardDeck {
    private final Card[][] cards;

    public CardDeck(TextureAtlas atlas, int backIndex) {
        cards = new Card[Suit.values().length][];
        for (Suit suit : Suit.values()) {
            cards[suit.index] = new Card[Pip.values().length];
            for (Pip pip : Pip.values()) {
                Sprite front = atlas.createSprite(suit.name, pip.value);
                front.setSize(CARD_WIDTH, CARD_HEIGHT);
                Sprite back = atlas.createSprite("back", backIndex);
                back.setSize(CARD_WIDTH, CARD_HEIGHT);
                cards[suit.index][pip.index] = new Card(suit, pip, back, front);
            }
        }
    }

    public Card getCard(Suit suit, Pip pip) {
        return cards[suit.index][pip.index];
    }
}