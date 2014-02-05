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

package com.gibbo.fallingrocks.entity.pickup.treasure;

import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.pickup.Collectible;
import com.gibbo.fallingrocks.entity.pickup.Score;

/**
 * A sub class of {@link Collectible}, anything of value that increases score
 * should be a sub class of this
 * 
 * @author Stephen Gibson
 */
public abstract class Treasure extends Collectible implements Score {

	public Treasure(Vector2 pos) {
		super(pos);

		setType(EntityType.TREASURE);
		setCollisionFilters(fd, EntityType.TREASURE.getValue(),
				EntityType.BOUNDARY.getValue() | EntityType.TREASURE.getValue());
	}

	@Override
	public void addScore(Player player) {
		player.setScore(player.getScore() + getValue());
	}

}
