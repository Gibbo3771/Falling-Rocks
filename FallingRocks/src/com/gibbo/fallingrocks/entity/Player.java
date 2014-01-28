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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.engine.WorldRenderer;

public class Player extends InputAdapter {

	// Jim Sprites
	private Sprite jim0, jim1, jim2, jim3, jim4, jim5, jim6;

	// Animation
	private Animation walkFrames;
	private TextureRegion currentFrame;

	private Sprite[] sprites;
	private float stateTime;
	private final float ANIM_DURATION = 0.18f;

	// Rectangles
	private Rectangle head;
	private Rectangle body;

	private Rectangle jim;

	// Jims properties
	private int health = 100;
	private final float MOVE_SPEED = 60 * 2.5f;
	private int score = 0;
	// States
	@SuppressWarnings("unused")
	private final int IDLE = 0; // Is actually used
	@SuppressWarnings("unused")
	private final int MOVING = 1; // Is actually used
	private final int DEAD = 2;

	// Jims movement
	public Vector2 velocity = new Vector2();

	float posX, posY;

	/**
	 * True if Jim is facing left}
	 */
	public boolean facingLeft = false;

	/**
	 * This affects Jim's current state he can either be:
	 * 
	 * @param IDLE
	 *            = 0
	 * @param MOVING
	 *            = 1
	 * @param DEAD
	 *            = 2
	 */
	public int currentState = 0;

	/**
	 * Default constructor, only used for debugging something at some point...
	 */
	public Player() {

		// Start creating Jim
		body = new Rectangle();

		body.height = 68;
		body.width = 32;
		body.x = 400 - body.getWidth();
		body.y = 0;

		jim0 = new Sprite(new Texture(Gdx.files.internal("data/img/jim0.png")));
		jim1 = new Sprite(new Texture(Gdx.files.internal("data/img/jim1.png")));
		jim2 = new Sprite(new Texture(Gdx.files.internal("data/img/jim2.png")));
		jim3 = new Sprite(new Texture(Gdx.files.internal("data/img/jim3.png")));
		jim4 = new Sprite(new Texture(Gdx.files.internal("data/img/jim4.png")));
		jim5 = new Sprite(new Texture(Gdx.files.internal("data/img/jim5.png")));
		jim6 = new Sprite(new Texture(Gdx.files.internal("data/img/jim6.png")));

		// Create animations
		sprites = new Sprite[] { jim1, jim2, jim3, jim4, jim5, jim6 };
		walkFrames = new Animation(ANIM_DURATION, sprites);
		stateTime = 0;

	}

	/**
	 * Create Jim at the given X and Y coordinates
	 * 
	 * @param posX
	 * @param posY
	 */
	public Player(float posX, float posY) {

		// Start creating Jim
		head = new Rectangle();
		body = new Rectangle();

		jim = new Rectangle();

		body.height = 43;
		body.width = 32;
		body.x = posX - body.getWidth();
		body.y = posY;

		head.height = 26;
		head.width = 14;
		head.x = posX - 22;
		head.y = posY + 40;

		jim.height = 69;
		jim.width = 46;

		jim.merge(body);
		jim.merge(head);

		jim0 = new Sprite(new Texture(Gdx.files.internal("data/img/jim0.png")));
		jim1 = new Sprite(new Texture(Gdx.files.internal("data/img/jim1.png")));
		jim2 = new Sprite(new Texture(Gdx.files.internal("data/img/jim2.png")));
		jim3 = new Sprite(new Texture(Gdx.files.internal("data/img/jim3.png")));
		jim4 = new Sprite(new Texture(Gdx.files.internal("data/img/jim4.png")));
		jim5 = new Sprite(new Texture(Gdx.files.internal("data/img/jim5.png")));
		jim6 = new Sprite(new Texture(Gdx.files.internal("data/img/jim6.png")));

		// Create animations
		sprites = new Sprite[] { jim1, jim2, jim3, jim4, jim5, jim6 };
		walkFrames = new Animation(ANIM_DURATION, sprites);
		stateTime = 0;

	}

	public void update(float delta) {

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkFrames.getKeyFrame(stateTime, true);

		body.x += velocity.x * delta;
		head.x += velocity.x * delta;

		keepBounds();

	}

	/**
	 * This method keeps Jim within scope of the screen ( within zero and max
	 * width on X and zero on Y )
	 */
	private void keepBounds() {

		// Edge collisions
		if (body.x < 0) {
			body.x = 0;
			head.x = 0 + head.getWidth() - 6;
			velocity.x = 0;
			currentState = 0;
		} else if (body.x > Gdx.graphics.getWidth() - body.getWidth()) {
			body.x = Gdx.graphics.getWidth() - body.getWidth();
			head.x = Gdx.graphics.getWidth() - head.getWidth() - 9;
			velocity.x = 0;
			currentState = 0;
		}
		if (body.y < 0) {
			body.y = 0;
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
			setCurrentState(DEAD);
			return true;
		}
		return false;
	}

	public void hit(int damageDone, int scoreGained) {
		setHealth(getHealth() - damageDone);
		setScore(getScore() + scoreGained);
	}

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
		return walkFrames;
	}

	public void setWalkFrames(Animation walkFrames) {
		this.walkFrames = walkFrames;
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

	public Rectangle getHead() {
		return head;
	}

	public void setHead(Rectangle head) {
		this.head = head;
	}

	public Rectangle getBody() {
		return body;
	}

	public void setBody(Rectangle body) {
		this.body = body;
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

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public float getANIM_DURATION() {
		return ANIM_DURATION;
	}

	public float getMOVE_SPEED() {
		return MOVE_SPEED;
	}

	/********************
	 * Keyboard control *
	 ********************/

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.A:
			velocity.x += -MOVE_SPEED;
			currentState = 1;
			facingLeft = true;
			return true;
		case Keys.D:
			velocity.x += MOVE_SPEED;
			currentState = 1;
			facingLeft = false;
			return true;
		case Keys.T:
			if (!WorldRenderer.isBox2D) {
				WorldRenderer.isBox2D = true;
			} else {
				WorldRenderer.isBox2D = false;
			}
			return true;

		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		switch (keycode) {
		case Keys.A:
			velocity.x = 0;
			currentState = 0;
			facingLeft = true;
			return true;
		case Keys.D:
			velocity.x = 0;
			currentState = 0;
			facingLeft = false;
			return true;
		}
		return false;

	}

}
