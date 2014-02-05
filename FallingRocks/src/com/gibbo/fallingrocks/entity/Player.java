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
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.gibbo.fallingrocks.engine.Profile;

public class Player extends Entity implements Disposable {

	/** Profile of the the player */
	private Profile profile;
	/** Json seralization */
	private transient Json json;

	/** Sprites */
	private Sprite jim0, jim1, jim2, jim3, jim4, jim5, jim6;

	/** For sprite animation in box2d bodies */
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;
	private Animation animation;
	private TextureRegion currentFrame;

	private Sprite[] sprites;
	private float stateTime;
	private final float ANIM_DURATION = 0.18f;

	/** Total health */
	private int health;
	/** Current health */
	public int currHealth;
	/** Total score */
	private double score;
	/** God mode, used for performance debugging */
	private boolean isGodMode;

	/** Speed of the player */
	private Vector2 speed;
	/** Velocity the player can move at */
	private float velocity;

	/** Custom fixtures for more defined body shape */
	private CircleShape head;
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

		json = new Json();
		json.setOutputType(OutputType.json);

		if (!Gdx.files.external("FallingRocks/saves/profile.sav")
				.exists()) {
			Gdx.app.log("Warning", "No Profile found, creating new...");
			profile = new Profile();
		} else {
			load();
		}

		setHealth(100);
		setCurrHealth(getHealth());
		setScore(0);
		setVelocity(5);

		setWidth(0.45f);
		setHeight(0.60f);

		setType(EntityType.PLAYER);
		setCollisionFilters(fd, EntityType.PLAYER.getValue(),
				EntityType.BOUNDARY.getValue() | EntityType.ROCK.getValue()
						| EntityType.SENSOR.getValue());

		PolygonShape player = new PolygonShape();
		player.setAsBox(getWidth(), getHeight());

		bd.type = BodyType.DynamicBody;
		bd.position.set(MathUtils.random(2, 18), 0f);
		bd.fixedRotation = true;

		fd.friction = 0.65f;
		fd.restitution = 0f;
		fd.density = 0.15f;
		fd.shape = player;

		body = world.createBody(bd);
		fixture = body.createFixture(fd);

		head = new CircleShape();
		head.setRadius(0.30f);
		head.setPosition(new Vector2(0, 0.90f));
		fd.shape = head;

		headFixture = body.createFixture(fd);
		body.setUserData(this);

		jim0 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim0.png")));
		jim1 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim1.png")));
		jim2 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim2.png")));
		jim3 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim3.png")));
		jim4 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim4.png")));
		jim5 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim5.png")));
		jim6 = new Sprite(new Texture(
				Gdx.files.internal("data/img/player/jim6.png")));
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

		speed = new Vector2();

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

		if (isGodMode)
			currHealth += 100 * delta;

		if (getCurrHealth() > getHealth())
			setCurrHealth(getHealth());

		body.setLinearVelocity(speed);
		if (Gdx.input.isKeyPressed(Keys.A)) {
			setFacing(State.FACING_LEFT);
			setCurrentState(State.MOVING);
			setSpeed(-velocity, 0);
			animatedBox2DSprite.play();
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			setFacing(State.FACING_RIGHT);
			setCurrentState(State.MOVING);
			setSpeed(velocity, 0);
			animatedBox2DSprite.play();
		} else {
			setCurrentState(State.IDLE);
			animatedBox2DSprite.stop();
			setSpeed(0, 0);
		}
		
		
		// Check if player has reached a new high score
		if (getScore() >= getProfile().getHighScore())
			profile.setHighScore(getScore());

	}

	/**
	 * Checks if the player is dead or alive
	 * 
	 * @return true if player is dead
	 */
	public boolean isDead() {
		if (getCurrHealth() <= 0) {
			// Check if player has reached a new high score
			if (getScore() >= getProfile().getHighScore()) {
				profile.setHighScore(getScore());
				save();
			}
			getBody().setLinearVelocity(0, 10);
			setCurrHealth(0);
			setCurrentState(State.DEAD);
			return true;
		}
		return false;
	}

	/** Save the players current profile */
	public void save() {
		Gdx.app.log("Info", "Saving Profile...");
		Gdx.files.external("FallingRocks/saves/profile.sav").writeString(
				Base64Coder.encodeString(json.toJson(profile)), false);

	}

	/** Load a players profile */
	public void load() {
		Gdx.app.log("Info", "Loading profile...");
		String temp = Base64Coder.decodeString(Gdx.files.external(
				"FallingRocks/saves/profile.sav").readString());
		profile = json.fromJson(Profile.class, temp);

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

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
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

	public void setCurrHealth(int currHealth) {
		this.currHealth = currHealth;
	}

	public int getCurrHealth() {
		return currHealth;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
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

	public void setSpeed(float speedX, float speedY) {
		this.speed.x = speedX;
		this.speed.y = speedY;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getVelocity() {
		return velocity;
	}

	public boolean isGodMode() {
		return isGodMode;
	}

	public void setGodMode(boolean isGodMode) {
		this.isGodMode = isGodMode;
	}

}
