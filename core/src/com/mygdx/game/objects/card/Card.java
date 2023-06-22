package com.mygdx.game.objects.card;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Card {
    public final Suit suit;
    public final Pip pip;

    final float[] vertices;
    final short[] indices;

    public final Matrix4 transform = new Matrix4();
    public final Vector3 position = new Vector3();
    public float angle;

    public Card(Suit suit, Pip pip, Sprite back, Sprite front) {
        assert(front.getTexture() == back.getTexture());
        this.suit = suit;
        this.pip = pip;
        front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);
        back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);

        vertices = convert(front.getVertices(), back.getVertices());
        indices = new short[] {0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4 };
    }

    private static float[] convert(float[] front, float[] back) {
        return new float[] {
                front[Batch.X2], front[Batch.Y2], 0.01f, 0, 0, 1, front[Batch.U2], front[Batch.V2],
                front[Batch.X1], front[Batch.Y1], 0.01f, 0, 0, 1, front[Batch.U1], front[Batch.V1],
                front[Batch.X4], front[Batch.Y4], 0.01f, 0, 0, 1, front[Batch.U4], front[Batch.V4],
                front[Batch.X3], front[Batch.Y3], 0.01f, 0, 0, 1, front[Batch.U3], front[Batch.V3],

                back[Batch.X1], back[Batch.Y1], -0.01f, 0, 0, -1, back[Batch.U1], back[Batch.V1],
                back[Batch.X2], back[Batch.Y2], -0.01f, 0, 0, -1, back[Batch.U2], back[Batch.V2],
                back[Batch.X3], back[Batch.Y3], -0.01f, 0, 0, -1, back[Batch.U3], back[Batch.V3],
                back[Batch.X4], back[Batch.Y4], -0.01f, 0, 0, -1, back[Batch.U4], back[Batch.V4]
        };
    }

    public void update() {
        float z = position.z + 0.5f * Math.abs(MathUtils.sinDeg(angle));
        transform.setToRotation(Vector3.Y, angle);
        transform.trn(position.x, position.y, z);
    }
}