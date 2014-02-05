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

package com.gibbo.fallingrocks.entity.pickup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gibbo.fallingrocks.engine.Updatable;
import com.gibbo.fallingrocks.engine.WorldRenderer;

/**
 * An indicator class used to draw information such as score gained or damage
 * recieved on screen
 * 
 * @author Stephen Gibson
 */
public class Indicator implements Updatable, Disposable {

	/** The font used to draw */
	private BitmapFont font;
	/** Position to draw font */
	private Vector3 pos;
	/** Colors for font */
	private float r, g, b, a;

	/**
	 * Create a new indicator at the given x and y coordinates
	 * 
	 * @param x
	 * @param y
	 */
	public Indicator(float x, float y) {
		this.pos = new Vector3();
		pos.set(x, y, 0);
		WorldRenderer.box2dCam.project(getPos());
		font = new BitmapFont(Gdx.files.internal("data/font/PriceDown38-White.fnt"));
		font.setScale(0.750f);
		setRed(1);
		setGreen(1);
		setBlue(1);

	}

	/**
	 * /** Create a new indicator at the given vector2 position
	 * 
	 * @param pos
	 */
	public Indicator(Vector2 pos) {
		this.pos = new Vector3();
		this.pos.set(pos, 0);
		WorldRenderer.box2dCam.project(getPos());
		font = new BitmapFont();

		setRed(1);
		setGreen(1);
		setBlue(1);
	}

	@Override
	public void update(float delta) {
		pos.add(0, 15f * delta, 0);
		font.setColor(getRed(), getGreen(), getBlue(), font.getColor().a
				- 1.80f * delta);

	}

	/**
	 * Draw the indicator
	 * 
	 * @param batch
	 * @param text
	 */
	public void draw(SpriteBatch batch, String text) {
		font.draw(batch, "" + text, pos.x, pos.y);
	}

	@Override
	public void dispose() {
		font.dispose();
	}

	/**
	 * Get the font used to draw the indicator
	 * 
	 * @return
	 */
	public BitmapFont getFont() {
		return font;
	}

	/**
	 * Set the position at which the indicator should be drawn
	 * 
	 * @param pos
	 */
	public void setPos(Vector3 pos) {
		this.pos = pos;
	}

	/**
	 * Get the position of the drawn indicator
	 * 
	 * @return
	 */
	public Vector3 getPos() {
		return pos;
	}

	/********************************************
	 * Getters and setters for the font colors *
	 ********************************************/

	/**
	 * Set the color of the font
	 * 
	 * @param r
	 *            - Red
	 * @param g
	 *            - Green
	 * @param b
	 *            - Blue
	 */
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void setRed(float r) {
		this.r = r;
	}

	public void setGreen(float g) {
		this.g = g;
	}

	public void setBlue(float b) {
		this.b = b;
	}

	public void setAlpha(float a) {
		this.a = a;
	}

	public float getRed() {
		return r;
	}

	public float getGreen() {
		return g;
	}

	public float getBlue() {
		return b;
	}

	public float getAlpha() {
		return a;
	}

}
