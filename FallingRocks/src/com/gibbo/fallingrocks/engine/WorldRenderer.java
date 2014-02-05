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
import com.badlogic.gdx.Input.Keys;
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
import com.gibbo.fallingrocks.entity.Player.State;
import com.gibbo.fallingrocks.entity.danger.Rock;
import com.gibbo.fallingrocks.entity.pickup.health.HealthPack;
import com.gibbo.fallingrocks.entity.pickup.treasure.Treasure;

public class WorldRenderer extends InputAdapter implements Disposable,
		Updatable {

	/** Player */
	private Player player;
	/** Enity factory */
	private EntityFactory factory;

	/** Camera for UI */
	private OrthographicCamera UICam;
	/** Camera used for Box2D */
	public static OrthographicCamera box2dCam;
	/** Spritebatch instance */
	private SpriteBatch batch;

	/** Box2D */
	public static World world;
	private final int SCALE = 32;
	private float worldStep = 60f;
	public static RayHandler handler;

	// Debugging
	private Box2DDebugRenderer box2dDebug;
	private ShapeRenderer debugRender;
	private boolean debug = true;
	private boolean drawBG = true;
	private boolean drawFPS = false;

	// UI
	private Sprite healthbar;
	private Sprite healthbarBG;
	private Sprite bg;
	private BitmapFont font;
	private boolean drawDiff = true;

	private float healthbarTotal;

	public WorldRenderer(EntityFactory factory, World world) {
		this.factory = factory;
		this.player = factory.getPlayer();
		WorldRenderer.world = world;

		/* Create old cam */
		UICam = new OrthographicCamera();
		UICam.setToOrtho(false, 640, 480);

		/* Create Box2D cam and world */
		box2dCam = new OrthographicCamera(20, 15);
		box2dCam.setToOrtho(false, Gdx.graphics.getWidth() / SCALE,
				Gdx.graphics.getHeight() / SCALE);
		world = new World(new Vector2(0, -9.81f), true);
		RayHandler.useDiffuseLight(true);
		handler = new RayHandler(world);

		batch = new SpriteBatch();

		// For debugging
		debugRender = new ShapeRenderer();
		box2dDebug = new Box2DDebugRenderer();
		debugRender.setProjectionMatrix(box2dCam.combined);

		// UI elements
		try {
			healthbar = new Sprite(new Texture(
					Gdx.files.internal("data/img/ui/healthbar.png")));
			font = new BitmapFont(
					Gdx.files.internal("data/font/PriceDown38-White.fnt"));
			font.setScale(0.50f);
			healthbarBG = new Sprite(new Texture(
					Gdx.files.internal("data/img/ui/healthbarBG.png")));
			bg = new Sprite(new Texture(
					Gdx.files.internal("data/img/backgrounds/bg3.png")));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(float delta) {

		healthbarTotal = player.getHealth() / 100 * player.getCurrHealth();

		batch.setProjectionMatrix(box2dCam.combined);
		batch.begin();
		if (drawBG) {
			/* Draw background in 3x3 grid */
			batch.draw(bg, 0, 15, bg.getWidth() / SCALE, bg.getHeight() / SCALE);
			batch.draw(bg, 0, 15, -bg.getWidth() / SCALE, bg.getHeight()
					/ SCALE);
			batch.draw(bg, 20, 15, bg.getWidth() / SCALE, bg.getHeight()
					/ SCALE);

			batch.draw(bg, 0, 0, bg.getWidth() / SCALE, bg.getHeight() / SCALE);
			batch.draw(bg, 0, 0, -bg.getWidth() / SCALE, bg.getHeight() / SCALE);
			batch.draw(bg, 20, 0, bg.getWidth() / SCALE, bg.getHeight() / SCALE);

			batch.draw(bg, 0, 0, bg.getWidth() / SCALE, -bg.getHeight() / SCALE);
			batch.draw(bg, 0, 0, -bg.getWidth() / SCALE, -bg.getHeight()
					/ SCALE);
			batch.draw(bg, 20, 0, -bg.getWidth() / SCALE, -bg.getHeight()
					/ SCALE);
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

		drawPlayer();

		batch.end();

		handler.setCombinedMatrix(box2dCam.combined);
		handler.updateAndRender();

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
			healthbar.setColor(1, 1, 1, healthbar.getColor().a - .025f * delta);
			healthbarBG.setColor(1, 1, 1, healthbarBG.getColor().a - .025f
					* delta);
			System.out.println(font.getColor().a);
			if (font.getColor().a <= 0) {
				font.setColor(1, 1, 1, 0);
				healthbarBG.setColor(1, 1, 1, 0);
				healthbar.setColor(1, 1, 1, 0);

			}
		}
		batch.draw(healthbarBG,
				Gdx.graphics.getWidth() - healthbarBG.getWidth() - 10,
				Gdx.graphics.getHeight() - healthbar.getHeight() - 5,
				healthbarTotal, healthbar.getHeight() - 5);
		batch.draw(healthbar, Gdx.graphics.getWidth() - healthbar.getWidth()
				- 10, Gdx.graphics.getHeight() - healthbar.getHeight() - 5,
				healthbarTotal * 2, healthbar.getHeight() - 5);

		// Draw all fonts
		font.draw(batch, "Health    " + healthbarTotal + "%",
				Gdx.graphics.getWidth() - healthbar.getWidth() / 2 - 60,
				Gdx.graphics.getHeight() - 15.75f);
		font.draw(batch, "Score:              " + (int) player.getScore(),
				Gdx.graphics.getWidth() - healthbar.getWidth(),
				Gdx.graphics.getHeight() - 40);
		font.draw(batch, "Highscore:   "
				+ (int) player.getProfile().getHighScore(),
				Gdx.graphics.getWidth() - healthbar.getWidth(),
				Gdx.graphics.getHeight() - 60);
		if (drawDiff)
			font.draw(batch, "Difficulty: " + Level.difficulty, 15,
					Gdx.graphics.getHeight() - 15);
		if (drawFPS)
			font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 15,
					15);

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

		box2dCam.update();
		UICam.update();

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			debug = true;
		} else {
			debug = false;
		}

	}

	/**
	 * Draw the player, must call inside a SpriteBatch begin and end
	 */
	public void drawPlayer() {
		if (player.getCurrentState() == State.MOVING) {
			if (player.getFacing() == State.FACING_LEFT
					&& !player.getAnimatedBox2DSprite().isFlipX()) {
				player.getAnimatedBox2DSprite().flipFrames(true, false);
			} else if (player.getFacing() == State.FACING_RIGHT
					&& player.getAnimatedBox2DSprite().isFlipX()) {
				player.getAnimatedBox2DSprite().flipFrames(true, false);
			}
			player.getAnimatedBox2DSprite().draw(batch, player.getBody());
		} else {
			if (player.getCurrentState() == State.IDLE
					|| player.getCurrentState() == State.DEAD) {
				if (player.getFacing() == State.FACING_LEFT
						&& !player.getJim0().isFlipX()) {
					player.getJim0().flip(true, false);
				} else if (player.getFacing() == State.FACING_RIGHT
						&& player.getJim0().isFlipX()) {
					player.getJim0().flip(true, false);
				}
			}
			batch.draw(player.getJim0(),
					player.getBody().getPosition().x - 0.50f, player.getBody()
							.getPosition().y - 0.60f, 1f, 1.80f);
			player.getJim0().setRotation(
					player.getBody().getAngle() * MathUtils.radiansToDegrees);

		}
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.B:
			setDrawBG(isDrawBG() ? false : true);
			break;
		case Keys.G:
			player.setGodMode(player.isGodMode() ? false : true);
		case Keys.F:
			setDrawFPS(isDrawFPS() ? false : true);
			break;
		case Keys.L:
			setDrawDiff(isDrawDiff() ? false : true);
			break;
		case Keys.ALT_LEFT:
			Gdx.input.setCursorCatched(Gdx.input.isCursorCatched() ? false
					: true);
			return true;
		case Keys.R:
			Gdx.files.external("FallingRocks/saves/profile.sav").delete();
			player.getProfile().setHighScore(0);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void dispose() {
		batch.dispose();
		debugRender.dispose();
	}

	public void setDrawBG(boolean drawBG) {
		this.drawBG = drawBG;
	}

	public boolean isDrawBG() {
		return drawBG;
	}

	public void setDrawFPS(boolean drawFPS) {
		this.drawFPS = drawFPS;
	}

	public boolean isDrawFPS() {
		return drawFPS;
	}

	public void setDrawDiff(boolean drawDiff) {
		this.drawDiff = drawDiff;
	}

	public boolean isDrawDiff() {
		return drawDiff;
	}
}
