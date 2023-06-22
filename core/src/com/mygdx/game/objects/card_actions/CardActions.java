package com.mygdx.game.objects.card_actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.objects.card.Card;

public class CardActions {
    Pool<CardAction> actionPool = new Pool<CardAction>() {
        protected CardAction newObject() {
            return new CardAction(CardActions.this);
        }
    };
    Array<CardAction> actions = new Array<>();

    public void actionComplete(CardAction action) {
        actions.removeValue(action, true);
        actionPool.free(action);
    }

    public void update(float delta) {
        for (CardAction action : actions) {
            action.update(delta);
        }
    }

    public void animate(Card card, float x, float y, float z, float angle, float speed) {
        CardAction action = actionPool.obtain();
        action.reset(card);
        action.toPosition.set(x, y, z);
        action.toAngle = angle;
        action.speed = speed;
        actions.add(action);
    }
}