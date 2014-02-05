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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
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
 * The level class is exactly what it seems, it controls spawns and entities
 * within the world
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
		EASY(0.50f), NORMAL(1f), HARD(1.25f), IMPOSSIBRU(2f);

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
	private Player player;
	/** Level boundary */
	private Boundary boundary;

	/** Entity factory */
	private EntityFactory factory;

	/** Camera shake timers */
	private float shakeTime, currentShakeTime;
	/** Camera shake amount */
	private float shakePower, currentShakePower;
	/** Movement variables */
	private float shakeX, shakeY;

	/**
	 * Initiate a new level or "play session"
	 * 
	 * @param player
	 */
	public Level(EntityFactory factory) {
		this.factory = factory;
		this.player = factory.getPlayer();

		setDifficulty(Difficulty.NORMAL);

		boundary = new Boundary();
		EntityFactory.spawnOn = true;

	}

	@Override
	public void update(float delta) {

		/** Update all entites present in the level */
		factory.update(delta);

		if (player.isDead()) {
			EntityFactory.spawnOn = false;
			boundary.getLight().setColor(1, 1, 1,
					boundary.getLight().getColor().a - 0.20f * delta);
			if (boundary.getLight().getColor().a < 0) {
				boundary.getLight().setColor(1, 1, 1, 0);
				GameScreen.gameOver = true;
			}
		}

		if (currentShakeTime <= shakeTime) {
			currentShakePower = shakePower
					* ((shakeTime - currentShakeTime) / shakeTime);

			shakeX = MathUtils.random(-0.5f, 0.5f) * currentShakePower;
			shakeY = MathUtils.random(-0.5f, 0.5f) * currentShakePower;

			WorldRenderer.box2dCam.translate(-shakeX, -shakeY);
			currentShakeTime += delta;

		} else {
			WorldRenderer.box2dCam.setToOrtho(false, 20, 15);

		}

		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			setDifficulty(Difficulty.EASY);
		} else if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			setDifficulty(Difficulty.NORMAL);
		} else if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			setDifficulty(Difficulty.HARD);
		} else if (Gdx.input.isKeyPressed(Keys.NUM_4)) {
			setDifficulty(Difficulty.IMPOSSIBRU);
		}

	}

	/**
	 * Shake the camera
	 * 
	 * @param power
	 * @param time
	 */
	public void shakeCamera(float power, float time) {
		shakePower = power;
		shakeTime = time;
		currentShakeTime = 0;
	}

	@Override
	public void beginContact(Contact contact) {
		for (Entity entity : factory.getEntities()) {
			switch (entity.getType()) {
			case ROCK:
				Rock rock = (Rock) entity;
				if (didCollide(contact, entity, player)) {
					if (!rock.isAlreadyHit()) {
						Indicator indicator = new Indicator(player.getBody()
								.getPosition().x - player.getWidth(), 2.5f);
						indicator.setColor(1, 0, 0);
						rock.setIndicator(indicator);
						rock.damage(player);
						rock.setAlreadyHit(true);
						shakeCamera(.10f, 1f);
					}
					break;
				}
				break;
			case TREASURE:
				Treasure treasure = (Treasure) entity;
				if (didCollide(contact, entity, player)) {
					if (!treasure.isAlreadyCollected()) {
						Indicator indicator = new Indicator(player.getBody()
								.getPosition().x, 2.35f);
						indicator.setColor(1, 1, 0);
						treasure.setIndicator(indicator);
						treasure.addScore(player);
						treasure.collect();
						treasure.setAlreadyCollected(true);
					}
					break;
				}
				break;
			case HEALTH_PACK:
				HealthPack healthPack = (HealthPack) entity;
				if (didCollide(contact, entity, player)) {
					if (!healthPack.isAlreadyCollected()) {
						Indicator indicator = new Indicator(player.getBody()
								.getPosition().x, 2.35f);
						indicator.setColor(0, 1, 0);
						healthPack.setIndicator(indicator);
						healthPack.heal(player);
						healthPack.collect();
						healthPack.setAlreadyCollected(true);
					}
				}
			default:
				break;
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

	public Player getPlayer() {
		return player;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setJim(Player player) {
		this.player = player;
	}

	public float getShakeTime() {
		return shakeTime;
	}

	public void setShakeTime(float shakeTime) {
		this.shakeTime = shakeTime;
	}

	public float getCurrentShakeTime() {
		return currentShakeTime;
	}

	public void setCurrentShakeTime(float currentShakeTime) {
		this.currentShakeTime = currentShakeTime;
	}

}
