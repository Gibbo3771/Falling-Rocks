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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.entity.Entity;
import com.gibbo.fallingrocks.entity.Entity.EntityType;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.danger.Rock;
import com.gibbo.fallingrocks.entity.pickup.health.HealthPackSpawner;
import com.gibbo.fallingrocks.entity.pickup.treasure.GemSpawner;

/**
 * An entity holding class, holds all active entities in play and controls spawn
 * logic using classes derived from the {@link EntitySpawner} class
 * 
 * @author Stephen Gibson
 */
public class EntityFactory implements Disposable, Updatable {

	/** Player */
	private Player player;
	/** Gem spawner */
	private GemSpawner gemSpawner;
	/** HealthPack Spawner */
	private HealthPackSpawner healthSpawner;

	/** Spawns on */
	public static boolean spawnOn;
	/** Timer for the last entity spawned */
	private double lastSpawn;
	/** Timer for a new entity */
	private double newSpawn;
	/** Chance */
	private float chance;
	/** Array of randomly generated floats */
	private float[] rolls;
	/** Set if the glowsticks have been spawned */
	@SuppressWarnings("unused")
	private boolean glowSticks;

	/** Array of entities currently spawned */
	private Array<Entity> entities;
	/** Array of entities to be removed */
	private Array<Entity> entityKill;

	public EntityFactory() {

		gemSpawner = new GemSpawner(this);
		healthSpawner = new HealthPackSpawner(this);

		rolls = new float[3];
		entities = new Array<Entity>();
		entityKill = new Array<Entity>();

		glowSticks = false;

	}

	/**
	 * Update the entity factory, this keeps the timers in sync. Must call to
	 * have spawns
	 * 
	 * @param delta
	 */
	@Override
	public void update(float delta) {

		// if (!glowSticks) {
		// entities.add(new GlowStick(new Vector2(MathUtils.random(3, 17),
		// 17)));
		// glowSticks = true;
		// }

		if (spawnOn) {
			if (TimeUtils.nanoTime() - getLastSpawn() > MathUtility
					.secondToNano(newSpawn) / Level.difficulty.getValue()) {
				spawn();
				setLastSpawn(TimeUtils.nanoTime());
			}
		}

		healthSpawner.update(delta);
		gemSpawner.update(delta);

		for (Entity entity : entities) {
			entity.update(delta);
			if (entity.getType() == EntityType.ROCK) {
				if (entity.getBody().getPosition().y < -3) {
					queueForDelete(entity);
				}
			}
			if (entity.canDelete())
				queueForDelete(entity);
		}

		/* If world is not in physic step, start deleted queued entities */
		if (!WorldRenderer.world.isLocked())
			delete();
		

	}

	/**
	 * Spawn an entity based on a roll of 3 die, the highest rolled number is
	 * chosen to keep higher tier items spawn rates lower
	 */
	public void spawn() {
		for (int i = 0; i < rolls.length; i++) {
			rolls[i] = MathUtils.random(0, 100);
		}

		setChance(MathUtility.max(rolls));

		if (getChance() < healthSpawner.chance) {
			healthSpawner.setToSpawn(true);
		} else if (getChance() < gemSpawner.getChance()) {
			gemSpawner.setToSpawn(true);
		} else {
			entities.add(new Rock(new Vector2(MathUtils.random(0, 19),
					MathUtils.random(16, 24))));

		}

	}

	/**
	 * Remove the entity from the visible world and add it to the delete queue
	 * 
	 * @param entity
	 */
	public void queueForDelete(Entity entity) {
		entities.removeValue(entity, true);
		entityKill.add(entity);
	}

	/** Delete entites in the queue */
	public void delete() {
		for (Entity entity : entityKill) {
			if (entity != null) {
				WorldRenderer.world.destroyBody(entity.getBody());
				entityKill.removeValue(entity, true);
			}
		}
	}

	@Override
	public void dispose() {
		for (Entity entity : entities) {
			entity.dispose();
		}
		for (Entity entity : entityKill) {
			entity.dispose();
		}

	}

	public Player getPlayer() {
		return player;
	}

	public double getLastSpawn() {
		return lastSpawn;
	}

	public void setLastSpawn(double lastSpawn) {
		this.lastSpawn = lastSpawn;
	}

	public double getNewSpawn() {
		return newSpawn;
	}

	public void setNewSpawn(double newSpawn) {
		this.newSpawn = newSpawn;
	}

	public float getChance() {
		return chance;
	}

	public void setChance(float chance) {
		this.chance = chance;
	}

	public Array<Entity> getEntities() {
		return entities;
	}

	public Array<Entity> getEntityKill() {
		return entityKill;
	}

	public GemSpawner getGemSpawner() {
		return gemSpawner;
	}

	public void setGemSpawner(GemSpawner gemSpawner) {
		this.gemSpawner = gemSpawner;
	}

	public HealthPackSpawner getHealthSpawner() {
		return healthSpawner;
	}

	public void setHealthSpawner(HealthPackSpawner healthSpawner) {
		this.healthSpawner = healthSpawner;
	}

}
