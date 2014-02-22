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

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.engine.Level.Difficulty;
import com.gibbo.fallingrocks.engine.Profile;
import com.gibbo.fallingrocks.engine.WorldRenderer;
import com.gibbo.fallingrocks.entity.pickup.powerup.Powerup;

public class Player extends Entity implements Disposable, InputProcessor {

	/** Profile of the the player */
	private Profile profile;
	/** Json seralization */
	private transient Json json;

	/**
	 * The players animation sprites, when the player is not moving idle should
	 * be drawn, if moving walk1-6 should be drawn in order
	 */
	private Sprite idle, frame;

	/** For sprite animation in box2d bodies */
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;
	private Animation animation;
	private TextureRegion currentFrame;

	/** Array of sprites to be used with the animation */
	private Array<Sprite> sprites;
	private float stateTime;
	private final float ANIM_DURATION = 0.18f;

	/** Healthbar Sprite */
	private Sprite healthbar;
	/** Healthbar Size */
	private float healthbarSize;

	/** Total health */
	private int health;
	/** Current health */
	public int currHealth;
	/** Active power of for the player */
	private Powerup powerup;
	/** Total score */
	private double score;
	/** God mode, used for performance debugging */
	private boolean isGodMode;

	/** Speed of the player */
	private Vector2 speed;
	/** Velocity the player can move at */
	private float velocity;

	/** Moving left */
	public boolean mLeft;
	/** Moving right */
	public boolean mRight;

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

		/* Create Json serializer */
		json = new Json();
		json.setOutputType(OutputType.json);

		try {
			/* Attempt to load a profile, if none exists just create a new one */
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				if (!Gdx.files.external("FallingRocks/saves/profile.sav")
						.exists()) {
					Gdx.app.log("Warning", "No Profile found, creating new...");
					profile = new Profile(true, true, false);
				} else {
					load();
				}
			} else if (Gdx.files.isExternalStorageAvailable()
					&& !Gdx.files.external("FallingRocks/saves/profile.sav")
							.exists()) {
				Gdx.app.log("Warning", "No Profile found, creating new...");
				profile = new Profile(true, true, false);
			} else {
				load();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.files.external("FallingRocks/saves/profile.sav").delete();
		}

		speed = new Vector2();
		sprites = new Array<Sprite>();

		/* Construct the player */
		setHealth(100);
		setCurrHealth(getHealth());
		setScore(0);
		setVelocity(5);

		/* Load in the sprites */
		try {
			/* User interface sprites */
			healthbar = new Sprite(new Texture(
					Gdx.files.internal("data/img/ui/healthbar.png")));
			/* Player sprites */
			idle = new Sprite(new Texture(
					Gdx.files.internal("data/img/player/jim0.png")));
			for (int spriteNum = 1; spriteNum < 7; spriteNum++) {
				frame = new Sprite(new Texture(
						Gdx.files.internal("data/img/player/jim" + spriteNum
								+ ".png")));
				sprites.add(frame);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Create animations */
		animation = new Animation(ANIM_DURATION, sprites);

		animation.setPlayMode(Animation.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		animatedBox2DSprite.setAdjustSize(false);
		animatedBox2DSprite.setHeight(1.8f);
		animatedBox2DSprite.setWidth(1f);

		/* Setup collision filter and the Box2D body */
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

		setCurrentState(State.IDLE);

	}

	/**
	 * Update the player
	 * <p>
	 * Processes input and keeps track of animation
	 * </p>
	 * 
	 * @param delta
	 */
	public void update(float delta) {

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, true);

		/* Keep the healthbar sprite in sync with the total health in % */
		healthbarSize = getHealth() / 100 * getCurrHealth();

		// TODO Cheat mode, remove later
		if (isGodMode)
			currHealth += 100 * delta;

		/* Keep the players health in scope */
		if (getCurrHealth() > getHealth())
			setCurrHealth(getHealth());

		/* Set the bodies linear velocity to update with the speed */
		body.setLinearVelocity(speed);

		// TODO implement somewhere appropriate, used mainly for testing
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			Level.difficulty = Difficulty.EASY;
			getProfile().settingsChanged = true;
		} else if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			getProfile().settingsChanged = true;
			Level.difficulty = Difficulty.NORMAL;
		} else if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			getProfile().settingsChanged = true;
			Level.difficulty = Difficulty.HARD;
		} else if (Gdx.input.isKeyPressed(Keys.NUM_4)) {
			getProfile().settingsChanged = true;
			Level.difficulty = Difficulty.IMPOSSIBRU;
		}

		/* Handle input for desktop */
		if ((Gdx.input.isKeyPressed(Keys.A))
				|| (Gdx.input.isTouched() && Gdx.input.getX() < Gdx.graphics
						.getWidth() / 2)) {
			setFacing(State.FACING_LEFT);
			setCurrentState(State.MOVING);
			setSpeed(-velocity, 0);
			animatedBox2DSprite.play();
		} else if ((Gdx.input.isKeyPressed(Keys.D))
				|| (Gdx.input.isTouched() && Gdx.input.getX() > Gdx.graphics
						.getWidth() / 2)) {
			setFacing(State.FACING_RIGHT);
			setCurrentState(State.MOVING);
			setSpeed(velocity, 0);
			animatedBox2DSprite.play();
		} else {
			setCurrentState(State.IDLE);
			animatedBox2DSprite.stop();
			setSpeed(0, 0);
		}

		/* Check if player has reached a new high score */
		if (getScore() >= getProfile().highScore)
			profile.highScore = getScore();

		if (getProfile().highScore > 25000)
			getProfile().isHardUnlocked = true;
		if (getProfile().highScore > 100000)
			getProfile().isImpossibruUnlocked = true;

		/* Update all power ups */
		if (powerup != null) {
			powerup.getBody().setLinearVelocity(speed);
			powerup.update(delta);
		}

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			setScore(getScore() + 1000 * delta);
		}

	}

	/**
	 * Draw the player, call inbetween the {@link SpriteBatch} begin and end
	 * calls
	 * 
	 * @param delta
	 *            time since last frame
	 * @param batch
	 *            the sprite batch to use for drawing
	 */
	public void draw(float delta, SpriteBatch batch) {
		if (getCurrentState() == State.MOVING) {
			if (getFacing() == State.FACING_LEFT
					&& !getAnimatedBox2DSprite().isFlipX()) {
				getAnimatedBox2DSprite().flipFrames(true, false);
			} else if (getFacing() == State.FACING_RIGHT
					&& getAnimatedBox2DSprite().isFlipX()) {
				getAnimatedBox2DSprite().flipFrames(true, false);
			}
			getAnimatedBox2DSprite().draw(batch, getBody());
		} else {
			if (getCurrentState() == State.IDLE
					|| getCurrentState() == State.DEAD) {
				if (getFacing() == State.FACING_LEFT && !getIdle().isFlipX()) {
					getIdle().flip(true, false);
				} else if (getFacing() == State.FACING_RIGHT
						&& getIdle().isFlipX()) {
					getIdle().flip(true, false);
				}
			}
			batch.draw(getIdle(), getBody().getPosition().x - 0.50f, getBody()
					.getPosition().y - 0.60f, 1f, 1.80f);
			getIdle().setRotation(
					getBody().getAngle() * MathUtils.radiansToDegrees);

		}

		if (powerup != null) {
			Sprite sprite = getPowerup().getSprite();
			sprite.setPosition(getPowerup().getBody().getPosition().x,
					getPowerup().getBody().getPosition().y);
			sprite.setRotation(getPowerup().getBody().getAngle()
					* MathUtils.radDeg);
			sprite.draw(batch);
		}

	}

	/**
	 * Checks if the player is dead or alive
	 * 
	 * @return true if player is dead
	 */
	public boolean isDead() {
		if (getCurrHealth() <= 0) {
			// Check if player has reached a new high score
			if (getScore() > getProfile().highScore || profile.settingsChanged) {
				profile.highScore = getScore();
				save();
				profile.settingsChanged = false;
			}
//			AssetLoader.THEME.setVolume(AssetLoader.THEME.getVolume() - 0.125f
//					* Gdx.graphics.getDeltaTime());
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
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.B:
			profile.drawBG = profile.drawBG ? false : true;
			profile.settingsChanged = true;
			break;
		case Keys.G:
			setGodMode(isGodMode() ? false : true);
			break;
		case Keys.F:
			profile.drawFPSCounter = profile.drawFPSCounter ? false : true;
			profile.settingsChanged = true;
			break;
		case Keys.L:
			profile.drawDiffOverlay = profile.drawDiffOverlay ? false : true;
			profile.settingsChanged = true;
			break;
		case Keys.ALT_LEFT:
			Gdx.input.setCursorCatched(Gdx.input.isCursorCatched() ? false
					: true);
			return true;
		case Keys.R:
			Gdx.files.external("FallingRocks/saves/profile.sav").delete();
			profile.reset();
			break;
		case Keys.V:
			WorldRenderer.box2dCam.startShake(0.50f, 1, 0.1f);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX < Gdx.graphics.getWidth() / 2) {
			mRight = false;
			mLeft = true;
		} else if (screenX > Gdx.graphics.getWidth() / 2) {
			mLeft = false;
			mRight = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mLeft = false;
		mRight = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void dispose() {
		idle.getTexture().dispose();
		for (Sprite sprite : sprites) {
			sprite.getTexture().dispose();
		}

	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
	}

	public Sprite getHealthbar() {
		return healthbar;
	}

	public float getHealthbarSize() {
		return healthbarSize;
	}

	public Sprite getIdle() {
		return idle;
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

	public Array<Sprite> getSprites() {
		return sprites;
	}

	public void setSprites(Array<Sprite> sprites) {
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

	public Powerup getPowerup() {
		return powerup;
	}

	public void setPowerup(Powerup powerup) {
		this.powerup = powerup;
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

	/**
	 * Set the current state of the player, this affects the entire games
	 * rendering and logic
	 * 
	 * @param currentState
	 */
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
