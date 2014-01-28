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

package com.gibbo.fallingrocks.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gibbo.fallingrocks.entity.Entity.EntityCategory;

/**
 * The boundary of the level is built off this class, later I will expand and
 * have bigger levels with obstacles
 * 
 * @author Stephen Gibson
 */
public class Boundary {

	private BodyDef bd;
	private FixtureDef fd;
	private Fixture groundFixture;
	private Fixture leftWallFixture;
	private Fixture rightWallFixture;

	private PolygonShape ground;
	private PolygonShape leftWall;
	private PolygonShape rightWall;

	public Boundary() {

		bd = new BodyDef();
		fd = new FixtureDef();
		ground = new PolygonShape();
		leftWall = new PolygonShape();
		rightWall = new PolygonShape();
		ground.setAsBox(20, 0.001f);
		leftWall.setAsBox(0.001f, 20f, new Vector2(0, 0), 0);
		rightWall.setAsBox(0.001f, 20f, new Vector2(20, 0), 0);

		bd.type = BodyType.StaticBody;
		bd.position.set(new Vector2(0, 0f));

		// Create the ground
		fd.shape = ground;
		fd.restitution = 0.001f;
		fd.friction = 0.15f;
		fd.filter.categoryBits = (short) EntityCategory.BOUNDARY.getValue();
		fd.filter.maskBits = (short) (EntityCategory.GEM.getValue() | EntityCategory.PLAYER.getValue());

		Body body = WorldRenderer.world.createBody(bd);

		groundFixture = body.createFixture(fd);

		// Create left wall
		fd.shape = leftWall;
		fd.restitution = 0.001f;
		fd.friction = 0.15f;
		fd.filter.categoryBits = (short) EntityCategory.BOUNDARY.getValue();
		fd.filter.maskBits = (short) (EntityCategory.GEM.getValue() | EntityCategory.PLAYER.getValue());

		leftWallFixture = body.createFixture(fd);

		// Create right wall
		fd.shape = rightWall;
		fd.restitution = 0.001f;
		fd.friction = 0.15f;
		fd.filter.categoryBits = (short) EntityCategory.BOUNDARY.getValue();
		fd.filter.maskBits = (short) (EntityCategory.GEM.getValue() | EntityCategory.PLAYER.getValue());

		rightWallFixture = body.createFixture(fd);

	}

	public Fixture getGroundFixture() {
		return groundFixture;
	}

	public Fixture getLeftWallFixture() {
		return leftWallFixture;
	}

	public Fixture getRightWallFixture() {
		return rightWallFixture;
	}

}
