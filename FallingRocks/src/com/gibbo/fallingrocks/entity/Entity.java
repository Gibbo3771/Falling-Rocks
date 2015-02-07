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

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
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
import com.gibbo.fallingrocks.engine.MathUtility;
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
	/** Width and height of the entity */
	protected float width, height;

	/** Used for deleting the entity after it has done a pick up animation */
	protected boolean canDelete;
	/**
	 * Used to start the countdown for deletion, is normally set to true upon
	 * the entity being picked up so an animation can be played
	 */
	protected boolean deleteTimerStart;
	/** The actual timer for the deletion process */
	protected double deleteTimer;

	/** If the entity can expire, this gets set after an entity gets spawned */
	protected boolean canExpire;
	/**
	 * Starts the expiration timer, once starter the entity will disappear from
	 * the ground after xxx amount of time
	 */
	protected boolean expireTimerStart;
	/** The actual timer for the expiration process */
	protected double expireTimer;
	/** How long the entity will stay active before expiring */
	protected float expireLife;

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
				0x0005), HEALTH_PACK(0x0006), MISC(0x0007), SHIELD(0x0008), PLAYER(
				0x0010);

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
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				fadeSprite(1.80f);
			} else {
				fadeSprite(1f);
			}
			if (TimeUtils.nanoTime() - deleteTimer > MathUtility
					.secondToNano(0.50))
				setCanDelete(true);
		}

		if (canExpire && !deleteTimerStart) {
			if (TimeUtils.nanoTime() - expireTimer > MathUtility
					.secondToNano(5)) {
				fadeSprite(5);
				if (TimeUtils.nanoTime() - expireTimer > MathUtility
						.secondToNano(7)) {
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

	/**
	 * Set the entity tier, used for balancing of entity power
	 * 
	 * @param Tier
	 */
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

	/**
	 * Set the type of this entity, this is required for collision filtering, if
	 * nothing is set Box2D will not know what should collide with what
	 * 
	 * @param type
	 */
	public void setType(EntityType type) {
		this.type = type;
	}

	/**
	 * Set the sprite for this entity, handled with a string, converting texture
	 * to sprite is done automatically by the method
	 * 
	 * @param fileLoc
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = new Sprite(sprite);
	}

	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Set the half width of the entity
	 * 
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Get the half width of the entity, if used in methods MUST MULTIPLY BY 2
	 * FOR REAL SIZEs
	 * 
	 * @return
	 */
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

	/**
	 * Get the half height of this entity, if used in methods MUST MULTIPLY BY 2
	 * FOR REAL SIZE
	 * 
	 * @return
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Get the Box2D body for this entity
	 * 
	 * @return
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Get the fixture for this entity, currently no entites have multiple
	 * fixtures besides the {@link Player}
	 * 
	 * @return
	 */
	public Fixture getFixture() {
		return fixture;
	}

	public BodyEditorLoader getBodyLoader() {
		return bodyLoader;
	}

	public void setBodyLoader(BodyEditorLoader bodyLoader) {
		this.bodyLoader = bodyLoader;
	}

	/**
	 * Set the timer for the deleted process, normally set right after
	 * {@link #canDelete} is true
	 * 
	 * @param timer
	 */
	public void setDeleteTimer(double timer) {
		this.deleteTimer = timer;
	}

	/**
	 * True if this entity can be deleted, usually set to true after pickup
	 * animation or boundary violation
	 * 
	 * @return
	 */
	public boolean canDelete() {
		return canDelete;
	}

	/**
	 * True if the delete timer has started, used for control within the loop
	 * 
	 * @return
	 */
	public boolean isDeleteTimerStart() {
		return deleteTimerStart;
	}

	/**
	 * Set if this entity can be deleted, usually set after an entity passes a
	 * boundary or has completed a pickup animation
	 * 
	 * @param canDelete
	 */
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	/**
	 * Set the expire timer, normally called after {@link #canExpire} is set
	 * 
	 * @param expireTimer
	 */
	public void setExpireTimer(double expireTimer) {
		this.expireTimer = expireTimer;
	}

	/**
	 * Set if the expire timer can start, normally set to true upon entity spawn
	 * 
	 * @param canExpire
	 */
	public void setCanExpire(boolean canExpire) {
		this.canExpire = canExpire;

	}

	@Override
	public void dispose() {
		// sprite.getTexture().dispose();
	}

}
