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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.Player.State;

public class WorldRenderer implements Disposable{

	// Reference to Jim
	private Player player;

	/** Camera for UI */
	private OrthographicCamera UICam;
	/** Camera used for Box2D */
	private OrthographicCamera box2dCam;
	/** Spritebatch instance */
	private SpriteBatch batch;

	/** Box2D World */
	public static World world;
	private final int SCALE = 32;
	private float worldStep = 60f;

	// Debugging
	private Box2DDebugRenderer box2dDebug;
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

	public WorldRenderer(Player player, World world) {
		this.player = player;
		WorldRenderer.world = world;

		/* Create old cam */
		UICam = new OrthographicCamera();
		UICam.setToOrtho(false, 640, 480);

		/* Create Box2D cam and world */
		box2dCam = new OrthographicCamera(20, 15);
		box2dCam.setToOrtho(false, Gdx.graphics.getWidth() / SCALE,
				Gdx.graphics.getHeight() / SCALE);
		world = new World(new Vector2(0, -9.81f), true);

		batch = new SpriteBatch();

		// For debugging
		debugRender = new ShapeRenderer();
		box2dDebug = new Box2DDebugRenderer();
		debugRender.setProjectionMatrix(box2dCam.combined);

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

	/**
	 * Update our graphical representation of the world
	 * 
	 * @param entities
	 *            - Array of entities in play to be drawn to screen
	 */
	public void update(float delta, Array<FallingEntity> entities,
			Array<Body> tmpBodies) {

		stateTime += delta;
		currentFrame = player.getWalkFrames().getKeyFrame(stateTime, true);
		healthbarTotal = player.getHealth() * 2;

		batch.setProjectionMatrix(box2dCam.combined);
		batch.begin();

		/* Draw the Box2D sprites associated with the body */

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

		}

		for (FallingEntity entity : entities) {
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

		if (debug) {
			box2dDebug.render(world, box2dCam.combined);

		}
		/***********************************************
		 * IMPORTANT - Draw things in order of z-index *
		 **********************************************/
		batch.setProjectionMatrix(UICam.combined);
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
		scoreFont.draw(batch, "Score:             " + player.getScore(),
				Gdx.graphics.getWidth() - healthbar.getWidth(),
				Gdx.graphics.getHeight() - 25);

		batch.end();

		world.step(1f / worldStep, 8, 5);

		box2dCam.update();
		UICam.update();

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			worldStep += 5;
			if (worldStep > 500) {
				worldStep = 500;
				System.out.println("Can not make time any slower");
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			worldStep -= 5;
			if (worldStep < 61) {
				worldStep = 60;
				System.out.println("Can not make time any faster");
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			debug = true;
		}else{
			debug = false;
		}
		
		


	}

	@Override
	public void dispose() {
		batch.dispose();
		debugRender.dispose();
	}
}
