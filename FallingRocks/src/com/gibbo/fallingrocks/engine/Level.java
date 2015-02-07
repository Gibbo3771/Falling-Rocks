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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.gibbo.fallingrocks.entity.Entity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.danger.Rock;
import com.gibbo.fallingrocks.entity.pickup.Indicator;
import com.gibbo.fallingrocks.entity.pickup.health.HealthPack;
import com.gibbo.fallingrocks.entity.pickup.treasure.Treasure;
import com.gibbo.fallingrocks.screens.GameScreen;

/**
 * 
 * The level class is exactly what it seems, holds the level status as well as
 * the player and entities present in the game
 * 
 * @author Stephen Gibson
 */
public class Level implements ContactListener, Disposable, Updatable {

	/**
	 * Difficulty setting, used to increase/decrease spawn rate and chance
	 * 
	 * @author Stephen Gibson
	 * 
	 */
	public enum Difficulty {
		EASY(0.75f), NORMAL(1f), HARD(1.20f), IMPOSSIBRU(1.40f);

		private float value;

		private Difficulty(float difficulty) {
			this.value = difficulty;

		}

		public float getValue() {
			return value;
		}

	}

	/** The difficulty of the level */
	public static Difficulty difficulty;
	/** Player */
	public static Player player;
	/** Level boundary */
	private Boundary boundary;

	/** Entity factory */
	private EntityFactory factory;

	/** Asset loader */
	public AssetLoader assetLoader;

	/** The renderer for the world */
	private WorldRenderer renderer;
	/** Box2D World */
	public World world;

	/**
	 * Initiate a new level or "play session"
	 * 
	 */
	public Level() {

		assetLoader = new AssetLoader();

		world = new World(new Vector2(0, -9.81f), true);

		player = new Player(world);
		factory = new EntityFactory();

		renderer = new WorldRenderer(factory, world);
		boundary = new Boundary();

		EntityFactory.spawnOn = true;
		factory.setNewSpawn(0.20f / Level.difficulty.getValue());

		Gdx.input.setInputProcessor(player);
		Gdx.input.setCursorCatched(true);
		world.setContactListener(this);

	}

	@Override
	public void update(float delta) {

		/* Update all entites present in the level */
		factory.update(delta);

		/* Update the player */
		player.update(delta);

		/* Update the rendering */
		renderer.update(delta);

		if (player.isDead()) {
			EntityFactory.spawnOn = false;
			boundary.getLight().setColor(1, 1, 1,
					boundary.getLight().getColor().a - 0.20f * delta);
			if (boundary.getLight().getColor().a < 0) {
				boundary.getLight().setColor(1, 1, 1, 0);
				GameScreen.gameOver = true;
			}
		}

	}

	@Override
	public void beginContact(Contact contact) {
		
		/* Check if player is dead before filtering collisions */
		if (!player.isDead()) {
			for (Entity entity : factory.getEntities()) {
				switch (entity.getType()) {
				case ROCK:
					Rock rock = (Rock) entity;
					if (didCollide(contact, entity, player)) {
						if (!rock.isAlreadyHit()) {
							Indicator indicator = new Indicator(player
									.getBody().getPosition().x
									- player.getWidth(), 2.35f);
							indicator.setColor(1, 0, 0);
							rock.setIndicator(indicator);
							rock.damage(player);
							rock.setAlreadyHit(true);
							WorldRenderer.box2dCam.startShake(
									rock.getBody().getLinearVelocity().y
											* rock.getEntitySize() / 150, 0.5f,
									0.125f);
							int sound = MathUtils.random(1, 2);
							switch (sound) {
							case 1:
								AssetLoader.HURT1.play(1f);
								break;
							case 2:
								AssetLoader.HURT2.play(1f);
								break;
							default:
								break;
							}
							Gdx.input.vibrate(125);
						}
						break;
					}
					break;
				case TREASURE:
					Treasure treasure = (Treasure) entity;
					if (didCollide(contact, entity, player)) {
						if (!treasure.isAlreadyCollected()) {
							Indicator indicator = new Indicator(player
									.getBody().getPosition().x, 2.35f);
							indicator.setColor(1, 1, 0);
							treasure.setIndicator(indicator);
							treasure.addScore(player);
							treasure.collect();
							treasure.setAlreadyCollected(true);
							AssetLoader.PICKUP2.play(0.15f);
						}
						break;
					}
					break;
				case HEALTH_PACK:
					HealthPack healthPack = (HealthPack) entity;
					if (didCollide(contact, entity, player)) {
						if (!healthPack.isAlreadyCollected()) {
							Indicator indicator = new Indicator(player
									.getBody().getPosition().x, 2.35f);
							indicator.setColor(0, 1, 0);
							healthPack.setIndicator(indicator);
							healthPack.heal(player);
							healthPack.collect();
							healthPack.setAlreadyCollected(true);
							AssetLoader.PICKUP3.play(0.60f);
						}
					}
				default:
					break;
				}

			}
		}

	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	/**
	 * Checks wether an entity collided with another, removed boilerplate code
	 * from the {@link}ContactListener methods
	 * 
	 * @param contact
	 * @param entityA
	 * @param entityB
	 * @return
	 */
	private boolean didCollide(Contact contact, Object entityA, Object entityB) {
		Entity entity1 = (Entity) entityA;
		Entity entity2 = (Entity) entityB;
		if (contact.getFixtureA().getBody().getUserData() == entity2.getBody()
				.getUserData()
				&& contact.getFixtureB().getBody().getUserData() == entity1
						.getBody().getUserData())
			return true;
		if (contact.getFixtureB().getBody().getUserData() == entity2.getBody()
				.getUserData()
				&& contact.getFixtureA().getBody().getUserData() == entity1
						.getBody().getUserData()) {
			return true;

		}

		return false;
	}

	@Override
	public void dispose() {
		factory.dispose();
		player.dispose();
		boundary.dispose();
	}

	public void setDifficulty(Difficulty difficulty) {
		Level.difficulty = difficulty;
	}

	public float getDifficulty() {
		return difficulty.getValue();
	}

	public static Player getPlayer() {
		return player;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setPlayer(Player player) {
		Level.player = player;
	}

	public WorldRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(WorldRenderer renderer) {
		this.renderer = renderer;
	}

}
