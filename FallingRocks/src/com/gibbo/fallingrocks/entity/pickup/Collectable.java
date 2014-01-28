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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gibbo.fallingrocks.entity.FallingEntity;

/**
 * Gem class, all gems inherit from here
 * 
 * @author Stephen Gibson
 */
public abstract class Collectable extends FallingEntity{

	/** Used for picking up the collectible */
	protected Fixture sensor;
	/** Def for the sensor */
	protected FixtureDef sensorDef;
	/** Shape for the sensor */
	protected CircleShape sensorShape;

	public Collectable(Vector2 pos) {
		super(pos);		

		sensorDef = new FixtureDef();
		sensorShape = new CircleShape();
		sensorShape.setRadius(0.65f);
		
		setCollisionFilters(fd,
				EntityCategory.GEM.getValue(),
				EntityCategory.BOUNDARY.getValue()
						| EntityCategory.GEM.getValue());
		setCollisionFilters(sensorDef, EntityCategory.SENSOR.getValue(), EntityCategory.PLAYER.getValue());
		
		setDmg(0);

	}
	
	/**
	 *  Create the sensor that surrounds the collectible, used to collect the entity
	 */
	public void CreateCollectSensor(){
		sensorShape.setPosition(new Vector2(0.50f, 0.50f));
		sensorDef.isSensor = true;
		sensorDef.density = 0;
		sensorDef.shape = sensorShape;
		sensor = body.createFixture(sensorDef);
	}
	
	public Fixture getSensor() {
		return sensor;
	}

	public CircleShape getSensorShape() {
		return sensorShape;
	}
	


}
