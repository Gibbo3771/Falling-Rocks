package com.gibbo.fallingrocks.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class Sapphire extends FallingEntity {
	
	private static Sprite sapphire = new Sprite(new Texture("data/img/sapphire.png"));

	/**
	 * 
	 * @param dmg - The amount of damage the entity does to the player, set by super
	 * @param value - The value worth of the entity, set by super
	 * @param x - The x coordinate
	 * @param y The y coordinate
	 * @param sizeX - The width of the entity, set and random by super
	 * @param sizeY - The height of the entity - set and random by super
	 * @param sprite - The Sprite used by the entity, set by super
	 */
	public Sapphire() {
		super(0, 25, MathUtils.random(0, 608), 500, 32, 32, sapphire);
	}

}
