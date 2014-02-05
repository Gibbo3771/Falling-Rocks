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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gibbo.fallingrocks.engine.WorldRenderer;

/**
 * 
 * @author Stephen Gibson
 */
public class MediKit extends HealthPack {

	public MediKit(Vector2 pos) {
		super(pos);

		setTier(Tier.TIER2);
		setEntitySize(0.90f);
		setHpAmount(100);

		setBodyLoader("data/img/health/health");
		setSprite("data/img/health/medikit.png");
		sprite.setSize(getEntitySize(), getEntitySize());

		bd.type = BodyType.DynamicBody;
		body = WorldRenderer.world.createBody(bd);

		fd.friction = 1.50f;
		fd.restitution = 0.01f;
		fd.density = 0.10f;

		bodyLoader.attachFixture(body, "medikit", fd, getEntitySize(), 1f, 1f);
		sensorBuilder.createSensor(this);

		RandMovement();
		body.setUserData(this);
	}

}
