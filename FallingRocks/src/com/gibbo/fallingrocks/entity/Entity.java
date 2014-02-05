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
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.engine.Math;
import com.gibbo.fallingrocks.engine.Updatable;
import com.gibbo.fallingrocks.engine.WorldRenderer;

/**
 * 
 * @author Stephen Gibson
 */
public abstract class Entity implements Disposable, Updatable {

	/** World reference */
	protected World world;

	/**
	 * Type of entity
	 * 
	 * @see EntityType
	 */
	protected EntityType type;
	/** Tier of the entity */
	private Tier tier;
	/** The entities position */
	protected Vector2 pos;
	/** Width and height of the entity */
	protected float width, height;

	/** Used for deleting the entity after it has done a pick up animation */
	protected boolean canDelete;
	protected boolean deleteTimerStart;
	protected double deleteTimer;

	/** If the entity can expire */
	protected boolean canExpire;
	protected boolean expireTimerStart;
	protected double expireTimer;

	/* Sprite */
	protected Sprite sprite;

	/* Box2D */
	protected Body body;
	protected BodyDef bd;
	protected FixtureDef fd;
	protected Fixture fixture;
	protected BodyEditorLoader bodyLoader;

	/**
	 * The category of the {@link}Entity controls what an entity can collide
	 * with
	 * <p>
	 * This is also used for decision based logic, every entity must have a type
	 * </p>
	 * 
	 * @author Stephen Gibson
	 * 
	 */
	public enum EntityType {
		BOUNDARY(0x0001), SENSOR(0x0002), ROCK(0x0003), COLLECTIBLE(0x0004), TREASURE(
				0x0005), HEALTH_PACK(0x0006), MISC(0x0007), PLAYER(0x0010);

		private int value;

		private EntityType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	/**
	 * The Tier system is used to keep an easy to understand hierarchy, Tier
	 * level helps to balance the entities to assign value, damage and spawn
	 * chance
	 * 
	 * @author Stephen Gibson
	 * 
	 */
	public enum Tier {
		TIER1(1), TIER2(2), TIER3(3), TIER4(4);

		private int tier;

		private Tier(int tier) {

		}

		public int getTier() {
			return tier;
		}

	}

	public Entity() {
		world = WorldRenderer.world;
		bd = new BodyDef();
		fd = new FixtureDef();

	}

	@Override
	public void update(float delta) {
		sprite.getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		if (deleteTimerStart) {
			fadeSprite(1.80f);
			if (TimeUtils.nanoTime() - deleteTimer > Math.secondToNano(0.50))
				setCanDelete(true);
		}

		if (canExpire && !deleteTimerStart) {
			if (TimeUtils.nanoTime() - expireTimer > Math.secondToNano(5)) {
				fadeSprite(5);
				if (TimeUtils.nanoTime() - expireTimer > Math.secondToNano(7)) {
					setCanDelete(true);

				}
			}
		}

	}

	

	/**
	 * Apply a random torque and force to the entity
	 */
	public void RandMovement() {
		body.applyTorque(MathUtils.random(-2, 2), true);
		body.applyForceToCenter(new Vector2(MathUtils.random(-10, 10), 0), true);

	}

	/** Fade the entities sprite */
	public void fadeSprite(float fadeTime) {
		sprite.setColor(sprite.getColor().r, sprite.getColor().g,
				sprite.getColor().b, sprite.getColor().a - fadeTime
						* Gdx.graphics.getDeltaTime());
	}

	public void setTier(Tier Tier) {
		this.tier = Tier;
	}

	public Tier getTier() {
		return tier;
	}

	/**
	 * Set the collision filters to control what this entity collides with
	 * 
	 * @param fixtureDef
	 *            - The fixture definition to apply bits
	 * @param categoryBits
	 *            - Category of the entity
	 * @param maskBits
	 *            - What entity should collide with
	 */
	public void setCollisionFilters(FixtureDef fixtureDef, int categoryBits,
			int maskBits) {
		fixtureDef.filter.categoryBits = (short) categoryBits;
		fixtureDef.filter.maskBits = (short) maskBits;

	}

	public EntityType getType() throws NullPointerException {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public void setSprite(String fileLoc) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(fileLoc)));
		sprite.setOrigin(0, 0);
		this.sprite = sprite;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getPos() {
		return pos;
	}

	/**
	 * Set the half width of the entity
	 * 
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	public float getWidth() {
		return width;
	}

	/**
	 * Set the half height of the entity
	 * 
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	public float getHeight() {
		return height;
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

	public void setDeleteTimer(double timer) {
		this.deleteTimer = timer;
	}

	public double getDeleteTimer() {
		return deleteTimer;
	}

	public boolean canDelete() {
		return canDelete;
	}

	public boolean isDeleteTimerStart() {
		return deleteTimerStart;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public void setExpireTimer(double expireTimer) {
		this.expireTimer = expireTimer;
	}

	public void setCanExpire(boolean canExpire) {
		this.canExpire = canExpire;

	}

	@Override
	public void dispose() {
		sprite.getTexture().dispose();
	}

}
