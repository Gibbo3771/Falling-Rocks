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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gibbo.fallingrocks.engine.WorldRenderer;

/**
 * Tier2 collectible, increase the player score
 * 
 * @author Stephen Gibson
 * 
 */
public class Ruby extends Collectable {


	public Ruby(Vector2 pos) {
		super(pos);

		setTier(Tier.TIER2);
		setValue(100);

		setBodyLoader("data/img/bodyeditor/gems");
		setSprite("data/img/bodyeditor/ruby.png");
		sprite.setSize(1, 1);
		sprite.setScale(1, 1);

		bd.type = BodyType.DynamicBody;
		body = WorldRenderer.world.createBody(bd);
		
		fd.friction = 0.80f;
		fd.restitution = 0.05f;
		fd.density = 0.40f;
		
		bodyLoader.attachFixture(body, "ruby", fd, 1, 1, 1);
		CreateCollectSensor();

		body.applyTorque(MathUtils.random(-2, 2), true);
		body.applyForceToCenter(new Vector2(MathUtils.random(-10, 10), 0), true);
		sensor.setUserData(this);
		body.setUserData(this);

	}

}
