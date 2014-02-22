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

package com.gibbo.fallingrocks.entity.pickup.health;

import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.engine.AssetLoader;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.pickup.Collectible;

/**
 * 
 * @author Stephen Gibson
 */
public abstract class HealthPack extends Collectible implements Heal {

	/** The total HP this health pack can restore */
	protected int hpAmount;

	public HealthPack(Vector2 pos) {
		super(pos);

		setBodyLoader(AssetLoader.HEALTH);
		
		setType(EntityType.HEALTH_PACK);
		setCollisionFilters(fd, EntityType.HEALTH_PACK.getValue(),
				EntityType.BOUNDARY.getValue());
	}

	@Override
	public void heal(Player player) {
		player.setCurrHealth(player.getCurrHealth() + getHpAmount());

	}

	/**
	 * Get the HP that this healthpack can restore
	 * 
	 * @return
	 */
	public int getHpAmount() {
		return hpAmount;
	}

	/**
	 * Set how much HP this healthpack can restore
	 * 
	 * @param hpAmount
	 */
	public void setHpAmount(int hpAmount) {
		this.hpAmount = hpAmount;
	}

}
