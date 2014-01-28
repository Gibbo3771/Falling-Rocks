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

import net.dermetfan.utils.libgdx.graphics.AnimatedBox2DSprite;
import net.dermetfan.utils.libgdx.graphics.AnimatedSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class Player extends Entity implements Disposable {

	/** For sprite animation in box2d bodies */
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;

	/** Sprites */
	private Sprite jim0, jim1, jim2, jim3, jim4, jim5, jim6;

	// Animation
	private Animation animation;
	private TextureRegion currentFrame;

	private Sprite[] sprites;
	private float stateTime;
	private final float ANIM_DURATION = 0.18f;

	/** Total health */
	private int health = 100;
	/** Total score */
	private int score = 0;

	/** Custom fixtures for more defined body shape */
	private Fixture headFixture;

	/** Direction the player is facing */
	private State facing;
	/**
	 * State of the current player
	 * 
	 * @see State
	 */
	private State currentState;

	/**
	 * Controls the players current state, controls the animation and game
	 * status
	 * 
	 * @author Stephen Gibson
	 * 
	 */
	public enum State {
		IDLE, MOVING, DEAD, FACING_LEFT, FACING_RIGHT;

		private State() {

		}

	}

	/**
	 * Creates a player at a random position between 2 and 18 in world coords
	 */
	public Player(World world) {
		super();

		setCollisionFilters(fd,
				EntityCategory.PLAYER.getValue(),
				EntityCategory.BOUNDARY.getValue()
						| EntityCategory.ROCK.getValue() | EntityCategory.SENSOR.getValue());

		PolygonShape player = new PolygonShape();
		player.setAsBox(0.45f, 0.60f);

		bd.type = BodyType.DynamicBody;
		bd.position.set(MathUtils.random(2, 18), 0.50f);
		bd.fixedRotation = true;
		bd.linearDamping = 0;

		fd.friction = 0.65f;
		fd.restitution = 0f;
		fd.density = 0.15f;
		fd.shape = player;

		body = world.createBody(bd);
		fixture = body.createFixture(fd);
		
		CircleShape head = new CircleShape();
		head.setRadius(0.30f);
		head.setPosition(new Vector2(0, 0.90f));
		fd.shape = head;
		
		fixture = body.createFixture(fd);
		body.setUserData(this);

		jim0 = new Sprite(new Texture(Gdx.files.internal("data/img/jim0.png")));
		jim1 = new Sprite(new Texture(Gdx.files.internal("data/img/jim1.png")));
		jim2 = new Sprite(new Texture(Gdx.files.internal("data/img/jim2.png")));
		jim3 = new Sprite(new Texture(Gdx.files.internal("data/img/jim3.png")));
		jim4 = new Sprite(new Texture(Gdx.files.internal("data/img/jim4.png")));
		jim5 = new Sprite(new Texture(Gdx.files.internal("data/img/jim5.png")));
		jim6 = new Sprite(new Texture(Gdx.files.internal("data/img/jim6.png")));
		sprites = new Sprite[] { jim1, jim2, jim3, jim4, jim5, jim6 };
		

		// Create animations
		animation = new Animation(ANIM_DURATION, sprites);
		stateTime = 0;

		animation.setPlayMode(Animation.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		animatedBox2DSprite.setAdjustSize(false);
		animatedBox2DSprite.setHeight(1.8f);
		animatedBox2DSprite.setWidth(1f);

		currentState = State.IDLE;
		animatedBox2DSprite.setPlaying(false);

	}

	/**
	 * Update the player
	 * <p>
	 * Processes input and keeps track of animation state time
	 * </p>
	 * 
	 * @param delta
	 */
	public void update(float delta) {

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, true);

		if (Gdx.input.isKeyPressed(Keys.A)) {
			setCurrentState(State.MOVING);
			setFacing(State.FACING_LEFT);
			body.setLinearVelocity(-5, 0);
			animatedBox2DSprite.play();
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			setFacing(State.FACING_RIGHT);
			setCurrentState(State.MOVING);
			body.setLinearVelocity(5, 0);
			animatedBox2DSprite.play();
		} else {
			setFacing(getFacing());
			setCurrentState(State.IDLE);
			animatedBox2DSprite.stop();
			body.setLinearVelocity(0, 0);
		}
		

	}

	/**
	 * Checks if Jim is dead or alive
	 * 
	 * @return true if Jim is dead
	 */
	public boolean isDead() {
		if (getHealth() < 0) {
			setHealth(0);
			setCurrentState(State.DEAD);
			return true;
		}
		return false;
	}

	/**
	 * Called when the player gets hit by an entity
	 * 
	 * @param damageDone
	 *            - Do damage if applicable
	 * @param scoreGained
	 *            - Increase score if applicable
	 */
	public void hit(int damageDone, int scoreGained) {
		setHealth(getHealth() - damageDone);
		setScore(getScore() + scoreGained);
	}

	@Override
	public void dispose() {
		jim0.getTexture().dispose();
		jim1.getTexture().dispose();
		jim2.getTexture().dispose();
		jim3.getTexture().dispose();
		jim4.getTexture().dispose();
		jim5.getTexture().dispose();
		jim6.getTexture().dispose();

	}

	public Sprite getJim0() {
		return jim0;
	}

	public Animation getWalkFrames() {
		return animation;
	}

	public void setWalkFrames(Animation walkFrames) {
		this.animation = walkFrames;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public Sprite[] getSprites() {
		return sprites;
	}

	public void setSprites(Sprite[] sprites) {
		this.sprites = sprites;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public State getFacing() {
		return facing;
	}

	public void setFacing(State facing) {
		this.facing = facing;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public float getANIM_DURATION() {
		return ANIM_DURATION;
	}

	public AnimatedBox2DSprite getAnimatedBox2DSprite() {
		return animatedBox2DSprite;
	}
	
	public Fixture getHeadFixture() {
		return headFixture;
	}

}
