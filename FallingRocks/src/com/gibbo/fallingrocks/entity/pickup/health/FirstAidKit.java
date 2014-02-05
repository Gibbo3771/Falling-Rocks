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
public class FirstAidKit extends HealthPack {

	public FirstAidKit(Vector2 pos) {
		super(pos);
		
		setTier(Tier.TIER1);
		setEntitySize(0.80f);
		setHpAmount(20);
		
		setBodyLoader("data/img/health/health");
		setSprite("data/img/health/firstaidkit.png");
		sprite.setSize(1.5f, 0.50f);
		
		bd.type = BodyType.DynamicBody;
		body = WorldRenderer.world.createBody(bd);
		
		fd.friction = 1.50f;
		fd.restitution = 0.01f;
		fd.density = 0.30f;
		
		bodyLoader.attachFixture(body, "firstaidkit", fd, 1f, 1.5f, 1.5f);
		sensorBuilder.createSensor(this, getEntitySize() * 1.10f, 0.75f, 0.25f);
		
		RandMovement();
		body.setUserData(this);
		
	}
	

}


