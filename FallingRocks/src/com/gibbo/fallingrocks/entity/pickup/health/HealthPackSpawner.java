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

import com.badlogic.gdx.math.MathUtils;
import com.gibbo.fallingrocks.engine.EntityFactory;
import com.gibbo.fallingrocks.engine.EntitySpawner;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.entity.Entity;
import com.gibbo.fallingrocks.entity.danger.Rock;

/**
 * 
 * @author Stephen Gibson
 */
public class HealthPackSpawner extends EntitySpawner {

	public HealthPackSpawner(EntityFactory factory) {
		super(factory);

		setChance(35);

	}

	@Override
	public Entity spawn() {
		float medikitP = 5 / Level.difficulty.getValue();
		float firstAidP = 20 / Level.difficulty.getValue();

		float p = MathUtils.random(0f, 100f);

		if (p < medikitP)
			return new MediKit(generatePos());
		if (p < firstAidP)
			return new FirstAidKit(generatePos());

		return new Rock(generatePos());

	}

}
