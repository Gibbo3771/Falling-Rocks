package com.gibbo.fallingrocks.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class FallingEntity {

	// Variables
	int value, dmg;
	int x, y;
	int width, height;
	float gravity = 100;

	public static long dropTime;
	
	// Rectangles
	Rectangle bounds;

	// Sprites
	Sprite sprite;

	public FallingEntity(int dmg, int value, int x, int y, int sizeX,
			int sizeY, Sprite sprite) {
		this.dmg = dmg;
		this.value = value;
		this.x = x;
		this.y = y;
		this.width = sizeX;
		this.height = sizeY;
		this.sprite = sprite;

		bounds = new Rectangle();
		bounds.height = sizeY;
		bounds.width = sizeX;
		bounds.x = x;
		bounds.y = y;

		sprite.setSize(bounds.getWidth(), bounds.getHeight());
		sprite.setBounds(bounds.x, bounds.y, width, height);

	}

	public void setDmg(int newDmgValue) {
		this.dmg = newDmgValue;
	}

	public void setValue(int newValueValue) {
		this.value = newValueValue;
	}

	public void setSprite(Sprite newSprite) {
		this.sprite = newSprite;
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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

	public void update(float delta) {

		bounds.y -= gravity * Gdx.graphics.getDeltaTime();
		gravity += MathUtils.random(2.5f, 5f);
		if (this instanceof Emerald || this instanceof Ruby
				|| this instanceof Sapphire || this instanceof Diamond) {
			if (this.bounds.y < -1) {
				this.bounds.y = 0;
			}
		}

	}
}
