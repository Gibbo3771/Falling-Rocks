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

/**
 * 
 * An abstract Falling Entity class
 * 
 * @author Stephen Gibson
 * 
 */
public abstract class FallingEntity extends Entity {

	/** The value of the entity */
	protected int value;
	/** The damage the entity can do */
	protected int dmg;

	/**
	 * @deprecated Constructor used with old basic collision detection, use this
	 *             for a non physics version of the game
	 * 
	 * @param dmg
	 *            - The amount of damage the entity does to the player
	 * @param value
	 *            - The value worth of the entity
	 * @param pos
	 *            - The positon of the entity
	 * @param sizeX
	 *            - The width of the entity
	 * @param sizeY
	 *            - The height of the entity
	 */
	public FallingEntity(int dmg, int value, Vector2 pos, int sizeX, int sizeY) {
		this.dmg = dmg;
		this.value = value;
		this.pos = pos;

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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getDmg() {
		return dmg;
	}

	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

}
