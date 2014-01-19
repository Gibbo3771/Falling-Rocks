package com.gibbo.fallingrocks.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gibbo.fallingrocks.controller.JimController;
import com.gibbo.fallingrocks.view.WorldRenderer;

public class Jim implements InputProcessor {

	// Object instances
	WorldRenderer renderer;
	JimController controller;

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

	// Default constructor
	public Jim() {

		// Instantiate objects
		renderer = new WorldRenderer();
		controller = new JimController();

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

	public Jim(float posX, float posY) {

		// Instantiate objects
		renderer = new WorldRenderer();
		controller = new JimController();

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

	public void update(float delta) {

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkFrames.getKeyFrame(stateTime, true);
		
		body.x += velocity.x * delta;
		head.x += velocity.x * delta;
		
		
		

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

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

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

}
