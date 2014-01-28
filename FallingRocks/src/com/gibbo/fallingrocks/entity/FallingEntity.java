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

package com.gibbo.fallingrocks.entity;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gibbo.fallingrocks.entity.danger.Rock;

/**
 * 
 * An abstract Falling Entity class
 * 
 * @author Stephen Gibson
 * 
 */
public abstract class FallingEntity extends Entity {
	
	

	/** The value of the entity */
	protected int value;
	/** The damage the entity can do */
	protected int dmg;
	/** The entities position */
	protected Vector2 pos;
	/** Rocks spawn point */
	protected int spawnPoint;
	/** Starting position of the entity, to stop overlapping */
	protected int startingPos;
	/** The width of the entity */
	protected int width;
	/** The height of the entity */
	protected int height;
	/** The gravity, the rate at which the entity should fall at */
	protected float gravity = -9.81f;

	/** Used for old basic collision detection */
	protected Rectangle bounds;

	// Sprites
	protected Sprite sprite;

	/* Box2D */
	protected Body body;
	protected BodyDef bd;
	protected FixtureDef fd;
	protected Fixture fixture;
	protected BodyEditorLoader bodyLoader;

	/**
	 * Constructor used with old basic collision detection, use this for a non
	 * physics version of the game
	 * 
	 * @param dmg
	 *            - The amount of damage the entity does to the player
	 * @param value
	 *            - The value worth of the entity
	 * @param pos
	 *            - The positon of the entity
	 * @param sizeX
	 *            - The width of the entity
	 * @param sizeY
	 *            - The height of the entity
	 */
	public FallingEntity(int dmg, int value, Vector2 pos, int sizeX, int sizeY) {
		this.dmg = dmg;
		this.value = value;
		this.pos = pos;
		this.width = sizeX;
		this.height = sizeY;

		bounds = new Rectangle();
		bounds.height = sizeY;
		bounds.width = sizeX;
		bounds.x = pos.x;
		bounds.y = pos.y;

	}

	public FallingEntity(Vector2 pos) {

		bd = new BodyDef();
		fd = new FixtureDef();
		bd.position.set(pos);

	}

	/**
	 * Update the entity, apply gravity and control bounds
	 * 
	 * @param delta
	 */
	public void update(float delta) {

		bounds.y -= gravity * (delta);
		gravity += bounds.getWidth() / 9.5f;
		if (!Rock.class.isInstance(this)) {
			if (this.bounds.y < -1) {
				this.bounds.y = 0;
			}
		}

	}
	
	/**
	 * Set the collision filters to control what this entity collides with
	 * 
	 * @param categoryBits - Category of the entity
	 * @param maskBits - What entity should collide with
	 */
	public void setCollisionFilters(int categoryBits, int maskBits){
		fd.filter.categoryBits = (short) categoryBits;
		fd.filter.maskBits = (short) maskBits;
		
	}

	public void setDmg(int newDmgValue) {
		this.dmg = newDmgValue;
	}

	public void setValue(int newValueValue) {
		this.value = newValueValue;
	}

	public void setSprite(String fileLoc) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(fileLoc)));
		sprite.setOrigin(0, 0);
		this.sprite = sprite;
	}

	public int getDmg() {
		return dmg;
	}

	public int getValue() {
		return value;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getPos() {
		return pos;
	}

	public int getSpawnPoint() {
		return spawnPoint;
	}

	public void setSpawnPoint(int spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public void setStartingPos(int startingPos) {
		this.startingPos = startingPos;
	}

	public int getStartingPos() {
		return startingPos;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public BodyDef getBd() {
		return bd;
	}

	public void setBd(BodyDef bd) {
		this.bd = bd;
	}

	public FixtureDef getFd() {
		return fd;
	}

	public void setFd(FixtureDef fd) {
		this.fd = fd;
	}

	public Fixture getFixture() {
		return fixture;
	}

	public void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	public BodyEditorLoader getBodyLoader() {
		return bodyLoader;
	}

	public void setBodyLoader(String file) {
		bodyLoader = new BodyEditorLoader(Gdx.files.internal(file));
	}

}
