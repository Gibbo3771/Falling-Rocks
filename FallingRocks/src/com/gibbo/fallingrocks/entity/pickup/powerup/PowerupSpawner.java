/**
* Copyright 2014 Stephen Gibson
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

package com.gibbo.fallingrocks.entity.pickup.powerup;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.engine.EntityFactory;
import com.gibbo.fallingrocks.engine.EntitySpawner;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.entity.Entity;

/**
 * 
 * @author Stephen Gibson
 */
public class PowerupSpawner extends EntitySpawner {

	public PowerupSpawner(EntityFactory factory) {
		super(factory);
		
		setChance(90);
		
	}
	
	@Override
	public Entity spawn() {
		float ffP = 75 / Level.difficulty.getValue();
		
		float p = MathUtils.random(0, 100);
		
		if(p < ffP)
			return new Forcefield(Vector2.Zero);
		
		return null;
	}

}


