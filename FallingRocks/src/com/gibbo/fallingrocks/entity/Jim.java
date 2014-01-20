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

public class Jim extends InputAdapter {

	// Jim Sprites
	public Sprite jim0, jim1, jim2, jim3, jim4, jim5, jim6;

	// Animation
	public Animation walkFrames;
	public TextureRegion currentFrame;

	public Sprite[] sprites;
	float stateTime;
	final float ANIM_DURATION = 0.14f;

	// Rectangles
	public Rectangle head;
	public Rectangle body;

	public Rectangle jim;

	// Jims properties
	public int health = 100;
	public final float MOVE_SPEED = 60 * 2.5f;
	public int score = 0;
	// States
	final public int IDLE = 0;
	final public int MOVING = 1;
	final public int DEAD = 2;

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
	public Jim() {

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
	public Jim(float posX, float posY) {

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
	
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	
	public int getCurrentState() {
		return currentState;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	public Jim getJim() {
		return this;
	}

	public void setPosX(float posX) {
		this.posX = body.x;
	}

	public void setPosY(float posY) {
		this.posY = body.y;
	}

	public float getPosX() {
		return body.x;
	}

	public float getPosY() {
		return body.y;
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
			break;
		case Keys.D:
			velocity.x += MOVE_SPEED;
			currentState = 1;
			facingLeft = false;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {

		switch (keycode) {
		case Keys.A:
			velocity.x = 0;
			currentState = 0;
			facingLeft = true;
			break;
		case Keys.D:
			velocity.x = 0;
			currentState = 0;
			facingLeft = false;
			break;
		}
		return true;

	}

}
