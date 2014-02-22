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

package com.gibbo.fallingrocks.entity;

import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.entity.pickup.Indicator;

/**
 * 
 * An abstract Falling Entity class
 * 
 * @author Stephen Gibson
 * 
 */
public abstract class FallingEntity extends Entity {

	/**
	 * Indicator used to draw information to the player, such as damage taken or
	 * score gained
	 */
	protected Indicator indicator;
	/** The value of the entity */
	protected float value;
	/** The damage the entity can do */
	protected int dmg;
	/** The size of the entity, this determines body and sprite size and value */
	protected float entitySize;

	public FallingEntity(){
		
	}
	
	/**
	 * Constructor used when created a new entity using Box2D
	 * 
	 * @param pos
	 *            - Where the entity should spawn
	 */
	public FallingEntity(Vector2 pos) {
		super();

		bd.position.set(pos);

	}


	/**
	 * Create a new indicator for this entity, this should be called when a
	 * player collides with an entity
	 * 
	 * @param indicator
	 */
	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	/**
	 * Get the value of the entity, used for increasing player score
	 * 
	 * @return value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Set the value of the entity, used for increasing the player score
	 * 
	 * @param value
	 */
	public void setValue(float value) {
		this.value = value;
	}

	public int getDmg() {
		return dmg;
	}

	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

	public float getEntitySize() {
		return entitySize;
	}

	public void setEntitySize(float size) {
		this.entitySize = size;
	}

}
