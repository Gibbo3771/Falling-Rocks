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

import com.badlogic.gdx.math.MathUtils;

public class Sapphire extends FallingEntity {
	

	/**
	 * 
	 * @param dmg - The amount of damage the entity does to the player
	 * @param value - The value worth of the entity
	 * @param x - The x coordinate
	 * @param y The y coordinate
	 * @param sizeX - The width of the entity
	 * @param sizeY - The height of the entity
	 * @param sprite - The Sprite used by the entity
	 */
	public Sapphire() {
		super(0, 25, MathUtils.random(0, 608), 500, 32, 32);
		setSprite("data/img/sapphire.png");
	}

}
