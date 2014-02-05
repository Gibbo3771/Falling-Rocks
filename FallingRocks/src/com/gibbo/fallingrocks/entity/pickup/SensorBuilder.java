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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gibbo.fallingrocks.entity.Entity.EntityType;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.danger.Rock;

/**
 * Sensor builder, used for building a field around an entity that the player or
 * other entites can interact with and trigger events
 * 
 * @author Stephen Gibson
 */
public class SensorBuilder {

	/** Fixture to be attached to body */
	private Fixture sensor;
	/** Def for the sensor */
	private FixtureDef sensorDef;
	/** Shape for the sensor */
	private CircleShape sensorShape;

	public SensorBuilder() {

		sensorDef = new FixtureDef();
		sensorShape = new CircleShape();

	}

	/**
	 * Create a sensor around the entity, the default position is the entity
	 * width and height / 2
	 * 
	 * @param entity
	 */
	public void createSensor(FallingEntity entity) {
		sensorShape.setRadius(entity.getEntitySize() / 2 * 1.25f);
		sensorShape.setPosition(new Vector2(entity.getEntitySize() / 2, entity
				.getEntitySize() / 2));
		sensorDef.isSensor = true;
		sensorDef.density = 0;
		sensorDef.shape = sensorShape;
		try {
			sensor = entity.getBody().createFixture(sensorDef);
		} catch (Exception e) {
			throw new NullPointerException(
					"Error creating sensor, body must not be null ");
		}

	}

	/**
	 * Create a sensor around the entity with the user defined anchor point
	 * 
	 * @param entity
	 * @param anchorX
	 *            - Position x on body
	 * @param anchorY
	 *            - Position y on body
	 */
	public void createSensor(FallingEntity entity, float radius, float anchorX,
			float anchorY) {
		sensorShape.setRadius(radius);
		sensorShape.setPosition(new Vector2(anchorX, anchorY));
		sensorDef.isSensor = true;
		sensorDef.density = 0;
		sensorDef.shape = sensorShape;
		sensor = entity.getBody().createFixture(sensorDef);

	}

	/**
	 * Configure the sensors collision filters, by default the sensor is set to
	 * type Sensor and can only collide with Player
	 * 
	 * @see EntityType
	 * @see Player
	 */
	public void setCollisionFilter() {
		sensorDef.filter.categoryBits = (short) EntityType.SENSOR.getValue();
		sensorDef.filter.maskBits = (short) EntityType.PLAYER.getValue();

	}

	/**
	 * 
	 * Configure the sensors collision filters to a specified criteria that is
	 * used with the {@link Filter} class
	 * 
	 * @param categoryBits
	 *            - The category or type of entity e.g {@link Rock}
	 * @param maskBits
	 *            - The filter data, this is the objects this entity can collide
	 *            with
	 */
	public void setCollisionFilter(short categoryBits, short maskBits) {
		sensorDef.filter.categoryBits = (short) categoryBits;
		sensorDef.filter.maskBits = (short) maskBits;

	}

	/**
	 * Configure the sensors collision filter by supplying new filter data to it<p>
	 * {@link Filter}
	 * 
	 * @param filter
	 */
	public void setCollisionFilter(Filter filter) {
		sensor.setFilterData(filter);
	}

	/**
	 * Get the fixture for this sensor
	 * 
	 * @return
	 */
	public Fixture getSensor() {
		return sensor;
	}

	/**
	 * Set the fixture for the sensor, this is normally not needed but can be
	 * used to create a new fixture
	 * 
	 * @param sensor
	 */
	public void setSensor(Fixture sensor) {
		this.sensor = sensor;
	}

	/**
	 * Get the sensor shape, useful if the sensor anchor point needs to be moved
	 * and re-created
	 * 
	 * @return
	 */
	public CircleShape getSensorShape() {
		return sensorShape;
	}

	/**
	 * 
	 * @param sensorShape
	 */
	public void setSensorShape(CircleShape sensorShape) {
		this.sensorShape = sensorShape;
	}

	/**
	 * Get the sensors fixture def
	 * 
	 * @return
	 */
	public FixtureDef getSensorDef() {
		return sensorDef;
	}

	/**
	 * Set the sensors fixture def
	 * 
	 * @param sensorDef
	 */
	public void setSensorDef(FixtureDef sensorDef) {
		this.sensorDef = sensorDef;
	}

}
