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

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;

/**
 * 
 * @author Stephen Gibson
 */
public abstract class Entity implements Disposable{

	/** Tier of the entity */
	private Tier tier;

	/** The entities position */
	protected Vector2 pos;

	/* Sprite */
	protected Sprite sprite;

	/* Box2D */

	protected Body body;
	protected BodyDef bd;
	protected FixtureDef fd;
	protected Fixture fixture;
	protected BodyEditorLoader bodyLoader;

	/**
	 * The category of the {@link}Entity controls what an entity can collide with
	 * @author Stephen Gibson
	 *
	 */
	public enum EntityCategory {
		BOUNDARY(0x0001), ROCK(0x0002), GEM(0x0004), PLAYER(0x0006), SENSOR(0x0008);

		private int value;

		private EntityCategory(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	/**
	 * The Tier system is used to keep an easy to understand hierarchy, Tier
	 * level helps to balance the entities to assign value, damage and spawn
	 * chance
	 * 
	 * @author Stephen Gibson
	 * 
	 */
	public enum Tier {
		TIER1(1), TIER2(2), TIER3(3), TIER4(4);

		private int tier;

		private Tier(int tier) {

		}

		public int getTier() {
			return tier;
		}

	}

	public void setTier(Tier Tier) {
		this.tier = Tier;
	}

	public Tier getTier() {
		return tier;
	}
	
	/**
	 * Set the collision filters to control what this entity collides with
	 * 
	 * @param fixtureDef - The fixture definition to apply bits
	 * @param categoryBits - Category of the entity
	 * @param maskBits - What entity should collide with
	 */
	public void setCollisionFilters(FixtureDef fixtureDef, int categoryBits, int maskBits){
		fixtureDef.filter.categoryBits = (short) categoryBits;
		fixtureDef.filter.maskBits = (short) maskBits;
		
	}
	
	public Entity(){
		bd = new BodyDef();
		fd = new FixtureDef();
		
	}


	public void setSprite(String fileLoc) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(fileLoc)));
		sprite.setOrigin(0, 0);
		this.sprite = sprite;
	}


	public Sprite getSprite() {
		return sprite;
	}


	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getPos() {
		return pos;
	}


	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public BodyDef getBd() {
		return bd;
	}

	public void setBd(BodyDef bd) {
		this.bd = bd;
	}

	public FixtureDef getFd() {
		return fd;
	}

	public void setFd(FixtureDef fd) {
		this.fd = fd;
	}

	public Fixture getFixture() {
		return fixture;
	}

	public void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	public BodyEditorLoader getBodyLoader() {
		return bodyLoader;
	}

	public void setBodyLoader(String file) {
		bodyLoader = new BodyEditorLoader(Gdx.files.internal(file));
	}
	
	@Override
	public void dispose() {
		sprite.getTexture().dispose();
	}


}
