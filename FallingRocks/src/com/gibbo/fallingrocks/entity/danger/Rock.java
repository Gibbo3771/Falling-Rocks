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

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.engine.Math;
import com.gibbo.fallingrocks.engine.WorldRenderer;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.pickup.Damage;

/**
 * Tier1 dangerous entity, damages the player
 * 
 * @author Stephen Gibson
 * 
 */
public class Rock extends FallingEntity implements Damage {

	/**
	 * Control if the rock has hit the player, prevents multiple health hits and
	 * hp reduction spam
	 */
	private boolean alreadyHit;

	public Rock(Vector2 pos) {
		super(pos);

		setType(EntityType.ROCK);
		setTier(Tier.TIER1);

		setCollisionFilters(fd, EntityType.ROCK.getValue(),
				EntityType.PLAYER.getValue());

		setEntitySize(MathUtils.random(1.5f, 2.125f));
		setDmg((int) (getEntitySize() * 5));
		setValue(0);

		/** ID to determine fixture and sprite */
		Integer ID = MathUtils.random(1, 5);

		setBodyLoader("data/img/rocks/rocks");
		switch (ID) {
		case 1:
			setSprite("data/img/rocks/rock1.png");
			break;
		case 2:
			setSprite("data/img/rocks/rock2.png");
			break;
		case 3:
			setSprite("data/img/rocks/rock3.png");
			break;
		case 4:
			setSprite("data/img/rocks/rock4.png");
			break;
		case 5:
			setSprite("data/img/rocks/rock5.png");
			break;
		default:
			break;
		}

		bd.type = BodyType.DynamicBody;

		body = WorldRenderer.world.createBody(bd);

		fd.friction = 0.80f;
		fd.restitution = 0.f;
		fd.density = 0.80f;

		bodyLoader.attachFixture(body, "rock" + ID.toString(), fd,
				getEntitySize(), 1, 1);

		sprite.setSize(getEntitySize(), getEntitySize());
		RandMovement();
		body.setUserData(this);
	}

	@Override
	public void update(float delta) {

		if (alreadyHit) {
			setDmg(getDmg());
		} else {
			setDmg((int) (getEntitySize() * -getBody().getLinearVelocity().y) / 2);
		}

		sprite.getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		if (deleteTimerStart) {
			if (TimeUtils.nanoTime() - deleteTimer > Math.secondToNano(0.50f)) {
				setIndicator(null);
			}
		}
	}

	@Override
	public void damage(Player player) {
		player.setCurrHealth(player.getCurrHealth() - getDmg());
		setDeleteTimer(TimeUtils.nanoTime());
		deleteTimerStart = true;

	}

	@Override
	public void RandMovement() {
		body.applyTorque(MathUtils.random(-18, 18), true);
		body.applyForceToCenter(new Vector2(MathUtils.random(-30, 30), 0), true);
	}

	/**
	 * Return true if this rock has already struck the player
	 * 
	 * @return
	 */
	public boolean isAlreadyHit() {
		return alreadyHit;
	}

	/**
	 * Set wither or not this rock has struck the player
	 * 
	 * @param alreadyHit
	 */
	public void setAlreadyHit(boolean alreadyHit) {
		this.alreadyHit = alreadyHit;
	}

}
