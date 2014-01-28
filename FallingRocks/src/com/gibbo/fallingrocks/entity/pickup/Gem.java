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

package com.gibbo.fallingrocks.entity.pickup;

import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.entity.Entity;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;

/**
 * Gem class, all gems inherit from here
 * 
 * @author Stephen Gibson
 */
public abstract class Gem extends FallingEntity implements ICollectable {

	public Gem(int dmg, int value, Vector2 pos, int sizeX, int sizeY) {
		super(dmg, value, pos, sizeX, sizeY);

	}

	public Gem(Vector2 pos) {
		super(pos);

		setCollisionFilters(
				EntityCategory.GEM.getValue(),
				EntityCategory.BOUNDARY.getValue()
						| EntityCategory.GEM.getValue());
		
		setDmg(0);

	}

	@Override
	public void collect(Player player, Entity entity) {

	}

}
