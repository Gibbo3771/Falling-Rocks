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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * An abstract Falling Entity class
 * 
 * @author Stephen Gibson
 * 
 */
public abstract class FallingEntity {

	// Variables
	private int value, dmg;
	private int x, y;
	private int width, height;
	private float gravity = 100;

	// Rectangles
	private Rectangle bounds;

	// Sprites
	private Sprite sprite;

	public FallingEntity(int dmg, int value, int x, int y, int sizeX,
			int sizeY) {
		this.dmg = dmg;
		this.value = value;
		this.x = x;
		this.y = y;
		this.width = sizeX;
		this.height = sizeY;

		bounds = new Rectangle();
		bounds.height = sizeY;
		bounds.width = sizeX;
		bounds.x = x;
		bounds.y = y;


	}

	public void setDmg(int newDmgValue) {
		this.dmg = newDmgValue;
	}

	public void setValue(int newValueValue) {
		this.value = newValueValue;
	}

	public void setSprite(String fileLoc) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(fileLoc)));
		sprite.setSize(bounds.getWidth(), bounds.getHeight());
		sprite.setBounds(bounds.x, bounds.y, width, height);
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
		gravity += bounds.getWidth() / 9.5f;
		if (!Rock.class.isInstance(this)) {
			if (this.bounds.y < -1) {
				this.bounds.y = 0;
			}
		}

	}
}
