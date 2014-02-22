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

package com.gibbo.fallingrocks.entity.pickup.misc;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gibbo.fallingrocks.engine.WorldRenderer;
import com.gibbo.fallingrocks.entity.Entity;

/**
 * 
 * @author Stephen Gibson
 */
public class GlowStick extends Entity {
	
	/** Light for glow */
	private ConeLight light;

	public GlowStick(Vector2 pos) {
		
		light = new ConeLight(WorldRenderer.handler, 300, Color.GREEN, 10, 0, 0, 0, 360);
		light.setColor(0.4f, 1f, 0.4f, 0.60f);
		
		setType(EntityType.MISC);
		setTier(Tier.TIER1);

//		setBodyLoader("data/img/misc/misc");
//		setSprite("data/img/misc/glowstick.png");
		sprite.setSize(0.750f, 0.750f);
		
		setCollisionFilters(fd, EntityType.MISC.getValue(), EntityType.BOUNDARY.getValue());

		bd.type = BodyType.DynamicBody;
		bd.position.add(pos);
		body = WorldRenderer.world.createBody(bd);

		fd.friction = 0.30f;
		fd.restitution = 0.08f;
		fd.density = 0.35f;

		bodyLoader.attachFixture(body, "glowstick", fd, 0.750f, 1, 1);
		light.attachToBody(getBody(), 0, 0);

		body.setUserData(this);
		
	}
	
	public ConeLight getLight() {
		return light;
	}
	
	public void setLight(ConeLight light) {
		this.light = light;
	}

}


