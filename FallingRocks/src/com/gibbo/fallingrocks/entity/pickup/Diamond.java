
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
 * Tier4 collectible, increase the player score
 * 
 * @author Stephen Gibson
 * 
 */
public class Diamond extends Gem {
	

	/**
	 * 
	 * @param dmg - The amount of damage the entity does to the player
	 * @param value - The value worth of the entity
	 * @param pos - The positon of the entity
	 * @param sizeX - The width of the entity
	 * @param sizeY - The height of the entity
	 */
	public Diamond() {
		super(0, 500, new Vector2(MathUtils.random(0, 608), 500), 40, 40);
		setSprite("data/img/diamond.png");
	}

	public Diamond(Vector2 pos) {
		super(pos);
		
		
		setValue(1000);
		
		setBodyLoader("data/img/bodyeditor/gems");
		setSprite("data/img/bodyeditor/diamond.png");
		sprite.setSize(1, 1);
		sprite.setScale(1, 1);
		
		fd.friction = 0.80f;
		fd.restitution = 0.08f;
		fd.density = 0.35f;
		
		bd.type = BodyType.DynamicBody;
		
		body = WorldRenderer.world.createBody(bd);
		
		body.setUserData(getSprite());
		bodyLoader.attachFixture(body, "diamond", fd, 1, 1, 1);
		body.applyTorque(MathUtils.random(-2, 2), true);
		body.applyForceToCenter(new Vector2(MathUtils.random(-10, 10), 0), true);
		body.setUserData(this);	
		
		
	}
	
	
}
