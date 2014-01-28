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

package com.gibbo.fallingrocks.entity.danger;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gibbo.fallingrocks.engine.WorldRenderer;
import com.gibbo.fallingrocks.entity.FallingEntity;

/**
 * Tier1 dangerous entity, damages the player
 * @author Stephen Gibson
 *
 */
public class Rock extends FallingEntity {


	public Rock(Vector2 pos) {
		super(pos);

		setTier(Tier.TIER1);
		
		setCollisionFilters(fd,
				EntityCategory.ROCK.getValue(),
				EntityCategory.ROCK.getValue()
						| EntityCategory.PLAYER.getValue()
						| EntityCategory.GEM.getValue());

		setDmg(MathUtils.random(10, 20));
		setValue(0);

		/** ID to determine fixture and sprite */
		Integer ID = MathUtils.random(1, 5);

		setBodyLoader("data/img/bodyeditor/rocks");
		switch (ID) {
		case 1:
			setSprite("data/img/bodyeditor/rock1.png");
			break;
		case 2:
			setSprite("data/img/bodyeditor/rock2.png");
			break;
		case 3:
			setSprite("data/img/bodyeditor/rock3.png");
			break;
		case 4:
			setSprite("data/img/bodyeditor/rock4.png");
			break;
		case 5:
			setSprite("data/img/bodyeditor/rock5.png");
			break;
		default:
			break;
		}

		bd.type = BodyType.DynamicBody;
		
		body = WorldRenderer.world.createBody(bd);
		
		fd.friction = 0.80f;
		fd.restitution = 0.f;
		fd.density = body.getMass();

		bodyLoader.attachFixture(body, "rock" + ID.toString(), fd, 1.5f, 1, 1);


		sprite.setSize(1, 1);
		sprite.setScale(1.55f);
		body.applyTorque(MathUtils.random(-15, 15), true);
		body.applyForceToCenter(new Vector2(MathUtils.random(-25, 25), 0), true);
		body.setUserData(this);
	}

}
