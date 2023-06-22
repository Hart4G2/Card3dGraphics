package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.card.Card;
import com.mygdx.game.objects.card.CardBatch;
import com.mygdx.game.objects.card.Pip;
import com.mygdx.game.objects.card.Suit;
import com.mygdx.game.objects.card_actions.CardActions;
import com.mygdx.game.objects.carddeck.CardDeck;

public class CardGraphics extends ApplicationAdapter {
	public final static float CARD_WIDTH = 1f;
	public final static float CARD_HEIGHT = CARD_WIDTH * 277f / 200f;
	public final static float MINIMUM_VIEWPORT_SIZE = 7f;

	TextureAtlas atlas;
	PerspectiveCamera cam;
	CardDeck deck;
	CardBatch cards;
	CameraInputController camController;
	ModelBatch modelBatch;
	Model tableTopModel;
	ModelInstance tableTop;
	Environment environment;
	DirectionalShadowLight shadowLight;
	ModelBatch shadowBatch;
	CardActions actions;

	@Override
	public void create() {
		modelBatch = new ModelBatch();
		atlas = new TextureAtlas(Gdx.files.internal("carddeck.atlas"));
		Material material = new Material(
				TextureAttribute.createDiffuse(atlas.getTextures().first()),
				new BlendingAttribute(false, 1f),
				FloatAttribute.createAlphaTest(0.2f));
		cards = new CardBatch(material);

		deck = new CardDeck(atlas, 3);

		Card card = deck.getCard(Suit.Spades, Pip.King);
		card.position.set(3.5f, -2.5f, 0.01f);
		card.angle = 180f;
		card.update();
		cards.add(card);

		cam = new PerspectiveCamera();
		cam.position.set(0, 0, 10);
		cam.lookAt(0, 0, 0);
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		ModelBuilder builder = new ModelBuilder();
		builder.begin();
		builder.node().id = "top";
		builder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
						new Material(ColorAttribute.createDiffuse(new Color(0x63750A))))
				.box(0f, 0f, -0.5f, 20f, 20f, 1f);
		tableTopModel = builder.end();
		tableTop = new ModelInstance(tableTopModel);

		shadowBatch = new ModelBatch(new DepthShaderProvider());
		shadowLight = new DirectionalShadowLight(1024, 1024, 10f, 10f, 1f, 20f);
		shadowLight.set(.8f, .8f, .8f, -.4f, -.4f, -.4f);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		environment.add(shadowLight);
		environment.shadowMap = shadowLight;

		actions = new CardActions();
	}

	@Override
	public void resize(int width, int height) {
		float halfHeight = MINIMUM_VIEWPORT_SIZE * 0.5f;
		if (height > width)
			halfHeight *= (float)height / (float)width;
		float halfFovRadians = MathUtils.degreesToRadians * cam.fieldOfView * 0.5f;
		float distance = halfHeight / (float)Math.tan(halfFovRadians);

		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.position.set(0, 0, distance);
		cam.lookAt(0, 0, 0);
		cam.update();
	}

	private float spawnTimer = -1f;
	@Override
	public void render() {
		final float delta = Math.min(1/30f, Gdx.graphics.getDeltaTime());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camController.update();

		if (spawnTimer < 0) {
			if (Gdx.input.justTouched())
				spawnTimer = 1f;
		} else if ((spawnTimer -= delta) <= 0f) {
			spawnTimer = 0.25f;
			spawn();
		}

		actions.update(delta);

		shadowLight.begin(Vector3.Zero, Vector3.Zero);
		shadowBatch.begin(shadowLight.getCamera());
		shadowBatch.render(cards);
		shadowBatch.end();
		shadowLight.end();

		modelBatch.begin(cam);
		modelBatch.render(tableTop, environment);
		modelBatch.render(cards, environment);
		modelBatch.end();
	}

	int pipIdx = -1;
	int suitIdx = 0;
	float spawnX = -0.5f;
	float spawnY = 0f;
	float spawnZ = 0f;
	public void spawn() {
		if (++pipIdx >= Pip.values().length) {
			pipIdx = 0;
			suitIdx = (suitIdx + 1) % Suit.values().length;
		}
		Suit suit = Suit.values()[suitIdx];
		Pip pip = Pip.values()[pipIdx];
		Gdx.app.log("Spawn", suit + " - " + pip);
		Card card = deck.getCard(suit, pip);
		card.position.set(3.5f, -2.5f, 0.01f);
		card.angle = 180;
		if (!cards.contains(card))
			cards.add(card);
		spawnX = (spawnX + 0.5f);
		if (spawnX > 6f) {
			spawnX = 0f;
			spawnY = (spawnY + 0.5f) % 2f;
		}
		spawnZ += 0.001f;
		actions.animate(card, -3.5f + spawnX, 2.5f - spawnY, 0.01f + spawnZ, 0f, 1f);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		atlas.dispose();
		cards.dispose();
		tableTopModel.dispose();
		shadowBatch.dispose();
		shadowLight.dispose();
	}
}