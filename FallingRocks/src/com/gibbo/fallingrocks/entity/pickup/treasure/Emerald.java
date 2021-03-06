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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gibbo.fallingrocks.engine.AssetLoader;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.engine.WorldRenderer;

/**
 * Tier1 collectible, increase the player score
 * 
 * @author Stephen Gibson
 * 
 */
public class Emerald extends Treasure {

	public Emerald(Vector2 pos) {
		super(pos);

		setTier(Tier.TIER1);
		setEntitySize(MathUtils.random(0.8f, 1.25f));
		setValue((75 * getEntitySize()) * Level.difficulty.getValue());

		setSprite(AssetLoader.EMERALD);
		sprite.setSize(getEntitySize(), getEntitySize());

		bd.type = BodyType.DynamicBody;
		body = WorldRenderer.world.createBody(bd);
		
		fd.friction = 0.80f;
		fd.restitution = 0.08f;
		fd.density = 0.35f;

		bodyLoader.attachFixture(body, "emerald", fd, getEntitySize(), 1, 1);
		sensorBuilder.createSensor(this);

		RandMovement();
		body.setUserData(this);

	}

}
