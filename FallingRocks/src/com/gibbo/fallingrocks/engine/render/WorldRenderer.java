/**
 * Copyright 2013 Stephen Gibson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gibbo.fallingrocks.engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Jim;
import com.gibbo.fallingrocks.entity.Rock;
import com.gibbo.fallingrocks.screens.GameScreen;

public class WorldRenderer {

	// Reference to Jim
	private Jim jim;

	// Camera and spritebatch
	private OrthographicCamera cam;
	private SpriteBatch batch;

	// Debugging
	private ShapeRenderer debugRender;
	private boolean debug = false;

	// Animation
	private float stateTime;
	@SuppressWarnings("unused")
	private TextureRegion currentFrame;

	// UI
	private Sprite healthbar;
	private Sprite healthbarBG;
	private BitmapFont healthFont;
	private BitmapFont scoreFont;

	private int healthbarTotal;
	private int healthbarTotalUnder = 200;

	public WorldRenderer(Jim jim) {
		this.jim = jim;

		// Create camera and SpriteBatch
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 640, 480);
		batch = new SpriteBatch();

		// For debugging
		debugRender = new ShapeRenderer();
		debugRender.setProjectionMatrix(cam.combined);

		// UI elements
		try {
			healthbar = new Sprite(new Texture(
					Gdx.files.internal("data/img/healthbar.png")));
			healthFont = new BitmapFont();
			scoreFont = new BitmapFont();
			healthbarBG = new Sprite(new Texture(
					Gdx.files.internal("data/img/healthbarBG.png")));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can not locate UI Textures");
		}

	}

	public void update() {

		if (debug)
			drawDebug();

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = jim.walkFrames.getKeyFrame(stateTime, true);
		healthbarTotal = jim.getHealth() * 2;

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		/*
		 * IMPORTANT - Draw things in order of z-index
		 */

		// Draw textures/sprites
		if (jim.currentState == 1) {
			if (jim.facingLeft && !jim.sprites[1].isFlipX()) {
				for (int i = 0; i < jim.sprites.length; i++) {
					jim.sprites[i].flip(true, false);
				}
			} else if (!jim.facingLeft && jim.sprites[1].isFlipX()) {
				for (int i = 0; i < jim.sprites.length; i++) {
					jim.sprites[i].flip(true, false);
				}

			}
			batch.draw(jim.currentFrame, jim.body.x, jim.body.y,
					jim.body.getWidth(), jim.body.getHeight() + 25);
		}
		if (jim.currentState == 0 || jim.currentState == 2) {
			if (jim.facingLeft && !jim.jim0.isFlipX()) {
				jim.jim0.flip(true, false);
			} else if (!jim.facingLeft && jim.jim0.isFlipX()) {
				jim.jim0.flip(true, false);
			}
			batch.draw(jim.jim0, jim.body.x, jim.body.y, jim.body.getWidth(),
					jim.body.getHeight() + 25);
		}
		for (FallingEntity entity : GameScreen.fallingEntity) {
			if (entity instanceof Rock) {
				batch.draw(entity.getSprite(), entity.getBounds().x - 5f,
						entity.getBounds().y - 5f, entity.getWidth() + 10,
						entity.getHeight() + 10);
			} else {
				batch.draw(entity.getSprite(), entity.getBounds().x,
						entity.getBounds().y, entity.getWidth(),
						entity.getHeight());
			}
		}
		// Draw healthbar and the underlay
		batch.draw(healthbarBG,
				Gdx.graphics.getWidth() - healthbarBG.getWidth() - 5,
				Gdx.graphics.getHeight() - healthbar.getHeight() + 5,
				healthbarTotalUnder, healthbar.getHeight() - 10);
		batch.draw(healthbar, Gdx.graphics.getWidth() - healthbar.getWidth()
				- 5, Gdx.graphics.getHeight() - healthbar.getHeight() + 5,
				healthbarTotal, healthbar.getHeight() - 10);

		// Draw all fonts
		healthFont.draw(batch, "Health",
				Gdx.graphics.getWidth() - healthbar.getWidth() / 2 - 20,
				Gdx.graphics.getHeight() - 5);
		scoreFont.draw(batch, "Score:             " + jim.score,
				Gdx.graphics.getWidth() - healthbar.getWidth(),
				Gdx.graphics.getHeight() - 25);

		batch.end();

		cam.update();

	}

	/**
	 * Draws rectangle debug lines to show the AABB collision boxes
	 */
	private void drawDebug() {

		// draw debug lines
		debugRender.begin(ShapeType.Line);
		debugRender.setColor(Color.RED);
		if (debug) {
			debugRender.rect(jim.body.x, jim.body.y, jim.body.getWidth(),
					jim.body.getHeight());
			debugRender.rect(jim.head.x, jim.head.y, jim.head.getWidth(),
					jim.head.getHeight());
			for (FallingEntity entity : GameScreen.fallingEntity)
				debugRender.rect(entity.getBounds().x, entity.getBounds().y,
						entity.getWidth(), entity.getHeight());
		}

		debugRender.end();

	}

	public void dispose() {
		batch.dispose();
		debugRender.dispose();
	}
}