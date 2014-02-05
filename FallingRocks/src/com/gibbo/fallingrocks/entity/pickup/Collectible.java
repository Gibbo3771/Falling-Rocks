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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.entity.FallingEntity;

/**
 * Collectible abstract class, all items that are collectible by the player
 * should be a sub class of this
 * 
 * @author Stephen Gibson
 */
public abstract class Collectible extends FallingEntity {

	/** Sensor Builder */
	protected SensorBuilder sensorBuilder;
	/** Use this boolean to prevent continuous collision */
	protected boolean alreadyCollected;

	/**
	 * Create a new instance of collectible
	 * 
	 * @param pos
	 *            - Position to create the collectible
	 */
	public Collectible(Vector2 pos) {
		super(pos);

		sensorBuilder = new SensorBuilder();
		setDmg(0);
		setCanExpire(true);
		setAlreadyCollected(false);
		setExpireTimer(TimeUtils.nanoTime());

	}
	
	/**
	 * Set the bodies linear and angular velocty to 0 and apply a small force
	 * upwards to create a "collect" effect
	 */
	public void collect() {
		getBody().setLinearVelocity(0, 0);
		getBody().setAngularVelocity(0);
		getBody().setLinearVelocity(0, 7.5f);
//		getBody().applyLinearImpulse(new Vector2(0, 1.5f), getBody().getWorldCenter(), true);
//		getBody().applyForceToCenter(0, 60, true);

		setDeleteTimer(TimeUtils.nanoTime());
		deleteTimerStart = true;
	}

	/**
	 * Get the sensor builder associated with his collectible
	 * 
	 * @return
	 */
	public SensorBuilder getSensorBuilder() {
		return sensorBuilder;
	}

	/**
	 * Check if the item has already been collected, this is to prevent double
	 * picks during collision pass overs
	 * 
	 * @return
	 */
	public boolean isAlreadyCollected() {
		return alreadyCollected;
	}

	/**
	 * Set if this item is already collect or not
	 * 
	 * @param alreadyCollected
	 */
	public void setAlreadyCollected(boolean alreadyCollected) {
		this.alreadyCollected = alreadyCollected;
	}

}
