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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.engine.Updatable;
import com.gibbo.fallingrocks.engine.WorldRenderer;

/**
 * 
 * @author Stephen Gibson
 */
public class Forcefield extends Powerup implements Updatable {
	

	public Forcefield(Vector2 pos) {
		super();
		
		setType(EntityType.SHIELD);
		setTier(Tier.TIER4);
		
		setCollisionFilters(fd, EntityType.SHIELD.getValue(), EntityType.ROCK.getValue());
		
		bd.type = BodyType.KinematicBody;
		bd.position.set(pos.x -2.5f, pos.y - 1.5f);
		bd.gravityScale = 0;
		
		fd.friction = 0.80f;
		fd.restitution = 0.5f;
		fd.density = 1f;
		
//		setSprite("data/img/powerup/forcefield.png");
		sprite.setSize(5, 5);
		
		body = WorldRenderer.world.createBody(bd);
		
//		setBodyLoader("data/img/powerup/powerups");
		bodyLoader.attachFixture(body, "forcefield", fd, 5, 1, 1);
		
		body.setUserData(this);
		setExpireTimer(TimeUtils.nanoTime());
		setCanExpire(true);
		
	}

}


