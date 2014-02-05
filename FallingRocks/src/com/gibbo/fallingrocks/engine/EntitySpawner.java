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
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.entity.Entity;

/**
 * An abstract entity spawner, use his class to create spawners for certain
 * entities
 * 
 * @author Stephen Gibson
 */
public abstract class EntitySpawner implements Spawn, Updatable {

	/** Entity factory instance */
	protected EntityFactory factory;

	/** Used for controlling spawn times */
	protected double lastSpawn;
	/** Timer used for spawning a new entity */
	protected double newSpawn;
	/** Percentage chance of entity spawning */
	protected float chance;
	/** Probabality of a spawn */
	protected float probability;
	/** The position at which the entity will spawn */
	protected Vector2 pos;
	/** Controls if the spawner is due to spawn an entity */
	protected boolean toSpawn;

	public EntitySpawner(EntityFactory factory) {
		this.factory = factory;
		pos = new Vector2();

	}

	@Override
	public void update(float delta) {
		if (toSpawn) {
			if (TimeUtils.nanoTime() - lastSpawn > Math.secondToNano(newSpawn)) {
				factory.getEntities().add(spawn());
				setLastSpawn(TimeUtils.nanoTime());
				setToSpawn(false);
			}
		}

	}

	@Override
	public Entity spawn() {
		return null;
	}

	public Vector2 generatePos() {
		pos.x = MathUtils.random(0, 19);
		pos.y = MathUtils.random(16, 24);
		return pos;
	}

	@Override
	public void delete(Entity entity) {

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
		this.chance = chance / Level.difficulty.getValue();
	}

	public boolean isToSpawn() {
		return toSpawn;
	}

	public void setToSpawn(boolean toSpawn) {
		this.toSpawn = toSpawn;
	}

	public void setPos(float x, float y) {
		this.pos.x = x;
		this.pos.y = y;
	}

}
