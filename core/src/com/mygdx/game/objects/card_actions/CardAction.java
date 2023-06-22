package com.mygdx.game.objects.card_actions;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.card.Card;

public class CardAction {
    public CardActions parent;
    public Card card;
    public final Vector3 fromPosition = new Vector3();
    public float fromAngle;
    public final Vector3 toPosition = new Vector3();
    public float toAngle;
    public float speed;
    public float alpha;

    public CardAction(CardActions parent) {
        this.parent = parent;
    }

    public void reset(Card card) {
        this.card = card;
        fromPosition.set(card.position);
        fromAngle = card.angle;
        alpha = 0f;
    }

    public void update(float delta) {
        alpha += delta * speed;
        if (alpha >= 1f) {
            alpha = 1f;
            parent.actionComplete(this);
        }
        card.position.set(fromPosition).lerp(toPosition, alpha);
        card.angle = fromAngle + alpha * (toAngle - fromAngle);
        card.update();
    }
}
