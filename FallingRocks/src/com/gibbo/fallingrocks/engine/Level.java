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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.danger.Rock;
import com.gibbo.fallingrocks.entity.pickup.Collectable;
import com.gibbo.fallingrocks.entity.pickup.Diamond;
import com.gibbo.fallingrocks.entity.pickup.Emerald;
import com.gibbo.fallingrocks.entity.pickup.Ruby;
import com.gibbo.fallingrocks.entity.pickup.Sapphire;

/**
 * 
 * The level class is exactly what it seems, it controls spawns and entities
 * within the world
 * 
 * @author Stephen Gibson
 */
public class Level implements ContactListener {

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
	private Difficulty difficulty;
	/** Player */
	private Player player;
	/** Level boundary */
	private Boundary boundary;

	/** Array of falling entities currently in play */
	private Array<FallingEntity> fallingEntity;

	/** Bodies to be removed */
	private Array<Body> entitiesToBeRemoved;

	/** Controls if entities should spawn */
	private boolean spawnOn = true;
	/** Timer variable used for controlling entity spawn times */
	private float lastEntity;

	float test = 150000000;

	/**
	 * Initiate a new level or "play session"
	 * 
	 * @param player
	 */
	public Level(Player player) {
		this.player = player;

		setDifficulty(Difficulty.NORMAL);

		boundary = new Boundary();

		fallingEntity = new Array<FallingEntity>();
		entitiesToBeRemoved = new Array<Body>();

	}

	public void process(float delta) {

		if (spawnOn) {
			if (TimeUtils.nanoTime() - lastEntity > 150000000 / getDifficulty()) {
				fallingEntity.add(spawnEntity());
				lastEntity = TimeUtils.nanoTime();
			}
		}

		for (FallingEntity entity : fallingEntity) {
			if (entity != null && !WorldRenderer.world.isLocked()) {
				if (entity.getBody().getPosition().y < -2) {
					entitiesToBeRemoved.add(entity.getBody());
					fallingEntity.removeValue(entity, true);
				}
			}
		}

		for (Body body : entitiesToBeRemoved) {
			if (body != null && !WorldRenderer.world.isLocked()) {
				WorldRenderer.world.destroyBody(body);
				entitiesToBeRemoved.removeValue(body, true);
			}
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
	 * Spawn an entity, such as a gem or a rock. The chances of a gem spawn is
	 * dependent on a random number generator. If a specific number is not
	 * rolled, a rock spawns instead
	 */
	public FallingEntity spawnEntity() {
		float emeraldProbability = 16 / getDifficulty();
		float rubyProbability = 8 / getDifficulty();
		float sapphireProbability = 3 / getDifficulty();
		float diamondProbability = 1 / getDifficulty();
		float chance = MathUtils.random(0f, 100f);

		System.out.println(diamondProbability);

		int x = MathUtils.random(1, 19);
		if (chance < diamondProbability) {
			System.out.println("New Diamond");
			return new Diamond(new Vector2(x, 20));
		} else if (chance < sapphireProbability) {
			System.out.println("New Sapphire");
			return new Sapphire(new Vector2(x, 20));
		} else if (chance < rubyProbability) {
			System.out.println("New Ruby");
			return new Ruby(new Vector2(x, 20));
		} else if (chance < emeraldProbability) {
			System.out.println("New Emerald");
			return new Emerald(new Vector2(x, 20));
		}
		return new Rock(new Vector2(x, 20));

	}

	@Override
	public void beginContact(Contact contact) {

		for (FallingEntity entity : fallingEntity) {
			if (entity instanceof Rock
					&& contact.getFixtureA().getBody().getUserData() == entity
							.getBody().getUserData()
					&& contact.getFixtureB().getBody().getUserData() == player
							.getBody().getUserData()) {
				player.hit(entity.getDmg(), entity.getValue());
				entity.setDmg(0);
			}
			if (entity instanceof Rock
					&& contact.getFixtureB().getBody().getUserData() == entity
							.getBody().getUserData()
					&& contact.getFixtureA().getBody().getUserData() == player
							.getBody().getUserData()) {
				player.hit(entity.getDmg(), entity.getValue());
				entity.setDmg(0);
			}
			if (entity instanceof Collectable
					&& contact.getFixtureA().getBody().getUserData() == player
							.getBody().getUserData()
					&& contact.getFixtureB().getBody().getUserData() == entity
							.getBody().getUserData()) {
				player.hit(entity.getDmg(), entity.getValue());
				entitiesToBeRemoved.add(entity.getBody());
				fallingEntity.removeValue(entity, true);
				entity.setValue(0);
			}
			if (entity instanceof Collectable
					&& contact.getFixtureB().getBody().getUserData() == player
							.getBody().getUserData()
					&& contact.getFixtureA().getBody().getUserData() == entity
							.getBody().getUserData()) {
				player.hit(entity.getDmg(), entity.getValue());
				entity.getBody().setTransform(entity.getBody().getPosition().x,
						entity.getBody().getPosition().y + 1, 0);
				entitiesToBeRemoved.add(entity.getBody());
				fallingEntity.removeValue(entity, true);
				entity.setValue(0);
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

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public float getDifficulty() {
		return difficulty.getValue();
	}

	public Player getJim() {
		return player;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setJim(Player player) {
		this.player = player;
	}

	public Array<FallingEntity> getFallingEntity() {
		return fallingEntity;
	}

	public void setFallingEntity(Array<FallingEntity> fallingEntity) {
		this.fallingEntity = fallingEntity;
	}

	public boolean isSpawnOn() {
		return spawnOn;
	}

	public void setSpawnOn(boolean spawnOn) {
		this.spawnOn = spawnOn;
	}

	public float getLastEntity() {
		return lastEntity;
	}

	public void setLastEntity(float lastEntity) {
		this.lastEntity = lastEntity;
	}

	public Array<Body> getTmpBodies() {
		return entitiesToBeRemoved;
	}

}
