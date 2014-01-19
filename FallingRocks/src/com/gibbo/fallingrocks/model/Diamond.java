package com.gibbo.fallingrocks.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class Diamond extends FallingEntity {
	
	private static Sprite diamond = new Sprite(new Texture(Gdx.files.internal("data/img/diamond.png")));

	/**
	 * 
	 * @param dmg - The amount of damage the entity does to the player, set by super
	 * @param value - The value worth of the entity, set by super
	 * @param x - The x coordinate, set and random by super
	 * @param y The y coordinate, set by super
	 * @param sizeX - The width of the entity, set and random by super
	 * @param sizeY - The height of the entity - set and random by super
	 * @param sprite - The Sprite used by the entity, set by super
	 */
	public Diamond() {
		super(0, 50, MathUtils.random(0, 608), 500, 40, 40, diamond);
	}
}
