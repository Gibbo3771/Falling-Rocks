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

package com.gibbo.fallingrocks.engine;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.gibbo.fallingrocks.entity.Entity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.danger.Rock;
import com.gibbo.fallingrocks.entity.pickup.health.HealthPack;
import com.gibbo.fallingrocks.entity.pickup.treasure.Treasure;
import com.gibbo.gameutil.camera.ExtendedOrthographicCamera;

public class WorldRenderer extends InputAdapter implements Disposable,
		Updatable {

	/** Player */
	private Player player;
	/** Enity factory */
	private EntityFactory factory;

	/** Camera for UI */
	public static OrthographicCamera UICam;
	public float width = 640, height = 480;
	/** Camera used for Box2D */
	public static ExtendedOrthographicCamera box2dCam;
	/** Spritebatch instance */
	private SpriteBatch batch;

	/** Box2D */
	public static World world;
	public static final int SCALE = 32;
	private float worldStep = 60f;
	public static RayHandler handler;

	// Debugging
	private Box2DDebugRenderer box2dDebug;
	private ShapeRenderer debugRender;
	private boolean debug;

	// UI
	private Sprite bg;
	private BitmapFont font;

	public WorldRenderer(EntityFactory factory, World world) {
		this.factory = factory;
		this.player = Level.getPlayer();
		WorldRenderer.world = world;

		/* Create old cam */
		UICam = new OrthographicCamera();
		UICam.setToOrtho(false, width, height);

		/* Create Box2D cam and world */
		box2dCam = new ExtendedOrthographicCamera(20, 15,
				new Vector2(10, 7.5f), false);
		box2dCam.shakeEnabled = true;
		box2dCam.smoothing = 0.6f;
		box2dCam.isFixed = true;

		RayHandler.useDiffuseLight(true);
		handler = new RayHandler(world);

		batch = new SpriteBatch();

		// For debugging
		debugRender = new ShapeRenderer();
		box2dDebug = new Box2DDebugRenderer();
		debugRender.setProjectionMatrix(box2dCam.combined);

		// UI elements
		try {
			font = new BitmapFont(
					Gdx.files.internal("data/font/PriceDown38-White.fnt"));
			font.setScale(0.50f);
			bg = new Sprite(new Texture(
					Gdx.files.internal("data/img/backgrounds/bg3.png")));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(float delta) {

		box2dCam.center(delta);

		batch.setProjectionMatrix(box2dCam.combined);
		batch.begin();
		if (player.getProfile().drawBG) {
			for (int x = -20; x < 21; x += 20) {
				for (int y = -15; y < 16; y += 15) {
					batch.draw(bg, x, y, bg.getWidth() / SCALE, bg.getHeight()
							/ SCALE);

				}
			}

		}

		/* Draw the Box2D sprites associated with the body */
		for (Entity entity : factory.getEntities()) {
			Sprite sprite = entity.getSprite() != null ? entity.getSprite()
					: null;
			Body body = entity.getBody();
			if (sprite != null && body != null) {
				sprite.setOrigin(0, 0);
				sprite.setPosition(body.getPosition().x, body.getPosition().y);
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				sprite.draw(batch);
			}

		}

		player.draw(delta, batch);

		batch.end();

		handler.setCombinedMatrix(box2dCam.combined);
		handler.updateAndRender();
		box2dCam.update();

		batch.begin();

		if (debug) {
			box2dDebug.render(world, box2dCam.combined);

		}

		/***********************************************
		 * IMPORTANT - Draw things in order of z-index *
		 **********************************************/
		batch.setProjectionMatrix(UICam.combined);
		// Draw healthbar and the underlay
		if (player.isDead() && font.getColor().a != 0) {
			font.setColor(1, 1, 1, font.getColor().a - .025f * delta);
			player.getHealthbar().setColor(1, 1, 1,
					player.getHealthbar().getColor().a - .025f * delta);
			System.out.println(font.getColor().a);
			if (font.getColor().a <= 0) {
				font.setColor(1, 1, 1, 0);
				player.getHealthbar().setColor(1, 1, 1, 0);

			}
		}
		batch.draw(player.getHealthbar(), width
				- player.getHealthbar().getWidth() - 10, height
				- player.getHealthbar().getHeight() - 5, player
				.getHealthbarSize() * 2, player.getHealthbar().getHeight() - 5);

		// Draw all fonts
		font.draw(batch, "Health    " + (int) player.getHealthbarSize() + "%",
				width - player.getHealthbar().getWidth() / 2 - 60,
				height - 15.75f);
		font.draw(batch, "Score:              " + (int) player.getScore(),
				width - player.getHealthbar().getWidth(), height - 40);
		font.draw(batch, "Highscore:   " + (int) player.getProfile().highScore,
				width - player.getHealthbar().getWidth(), height - 60);
		if (player.getProfile().drawDiffOverlay)
			font.draw(batch, "Difficulty: " + Level.difficulty, 15, height - 15);
		if (player.getProfile().drawFPSCounter)
			font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 15,

			15);

		/* Draw score and damage indicators */
		if (EntityFactory.spawnOn) {
			for (Entity entity : factory.getEntities()) {
				switch (entity.getType()) {
				case TREASURE: {
					Treasure treasure = (Treasure) entity;
					Integer tmpValue = (Integer) MathUtils.round(treasure
							.getValue());
					if (treasure.isDeleteTimerStart()) {
						treasure.getIndicator().update(delta);
						treasure.getIndicator()
								.draw(batch, tmpValue.toString());
					}
				}
					break;
				case ROCK: {
					Rock rock = (Rock) entity;
					Integer tmpValue = (Integer) rock.getDmg();
					if (rock.isDeleteTimerStart()) {
						if (rock.getIndicator() != null) {
							rock.getIndicator().update(delta);
							rock.getIndicator().draw(batch,
									"-" + tmpValue.toString());
						}
					}
				}
					break;
				case HEALTH_PACK: {
					HealthPack healthpack = (HealthPack) entity;
					Integer tmpValue = (Integer) MathUtils.round(healthpack
							.getHpAmount());
					if (healthpack.isDeleteTimerStart()) {
						healthpack.getIndicator().update(delta);
						healthpack.getIndicator().draw(batch,
								"+" + tmpValue.toString());
					}

				}
				default:
					break;
				}
			}
		}

		batch.end();

		world.step(1f / worldStep, 8, 5);

		UICam.update();


	}

	@Override
	public void dispose() {
		batch.dispose();
		debugRender.dispose();
	}

}
