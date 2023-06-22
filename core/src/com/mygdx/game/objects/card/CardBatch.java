package com.mygdx.game.objects.card;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

import java.util.Date;

public class CardBatch extends ObjectSet<Card> implements RenderableProvider, Disposable {
    MeshBuilder meshBuilder;
    Mesh mesh;
    Renderable renderable;

    public CardBatch(Material material) {
        final int maxNumberOfCards = 52;
        final int maxNumberOfVertices = maxNumberOfCards * 8;
        final int maxNumberOfIndices = maxNumberOfCards * 12;
        mesh = new Mesh(false, maxNumberOfVertices, maxNumberOfIndices,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        meshBuilder = new MeshBuilder();

        renderable = new Renderable();
        renderable.material = material;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        meshBuilder.begin(mesh.getVertexAttributes());
        meshBuilder.part("cards", GL20.GL_TRIANGLES, renderable.meshPart);
        for (Card card : this) {
            meshBuilder.setVertexTransform(card.transform);
            meshBuilder.addMesh(card.vertices, card.indices);
        }
        meshBuilder.end(mesh);

        renderable.shader = null;

        renderables.add(renderable);
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}