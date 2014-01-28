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

package com.gibbo.fallingrocks.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.entity.danger.Rock;
import com.gibbo.fallingrocks.entity.pickup.Diamond;
import com.gibbo.fallingrocks.entity.pickup.Emerald;
import com.gibbo.fallingrocks.entity.pickup.Ruby;
import com.gibbo.fallingrocks.entity.pickup.Sapphire;

/**
 * 
 * The level class is exactly what it seems, it controls spawns and entities
 * within the world
 * 
 * @author Stephen Gibson
 */
public class Level implements ContactFilter, ContactListener {

	/** Jim ref */
	private Player player;
	
	private Boundary boundary;

	/** Array of falling entities currently in play */
	private Array<FallingEntity> fallingEntity;

	/** Array of tmp bodies */
	private Array<Body> entitiesToBeRemoved;

	/** Controls if entities should spawn */
	private boolean spawnOn = true;
	/** Timer variable used for controlling entity spawn times */
	private float lastEntity;

	Fixture fixture;

	/**
	 * Initiate a new level or "play session"
	 * 
	 * @param player
	 */
	public Level(Player player) {
		this.player = player;
		
		boundary = new Boundary();

		fallingEntity = new Array<FallingEntity>();
		entitiesToBeRemoved = new Array<Body>();


	}

	public void process(float delta) {

		if (TimeUtils.nanoTime() - lastEntity > 300000000) {
			fallingEntity.add(spawnEntity());
			lastEntity = TimeUtils.nanoTime();
		}

		for (FallingEntity entity : fallingEntity) {
			if (entity != null) {
				if (entity.getBody().getPosition().y < -2) {
					WorldRenderer.world.destroyBody(entity.getBody());
					fallingEntity.removeValue(entity, false);
				}
			}
		}

		for (Body body : entitiesToBeRemoved) {
			if (!WorldRenderer.world.isLocked()) {
				WorldRenderer.world.destroyBody(body);
				entitiesToBeRemoved.removeValue(body, false);
			}
		}

		/** Old collision code */
		// for (FallingEntity entity : fallingEntity) {
		// // entity.update(delta);
		// if (entity.getBounds().overlaps(jim.getBody())
		// || entity.getBounds().overlaps(jim.getHead())) {
		// if (!jim.isDead()) {
		// fallingEntity.removeValue(entity, true);
		// // Damage/increase score
		// jim.hit(entity.getDmg(), entity.getValue());
		//
		// }
		// }
		// if (entity.getBounds().y < 0 - entity.getHeight()) {
		// fallingEntity.removeValue(entity, true);
		// }
		// }

	}

	/**
	 * Spawn an entity, such as a gem or a rock. The chances of a gem spawn is
	 * dependent on a random number generator. If a specific number is not
	 * rolled, a rock spawns instead
	 */
	public FallingEntity spawnEntity() {
		int emeraldProbability = 16;
		int rubyProbability = 8;
		int sapphireProbability = 3;
		int diamondProbability = 1;
		float chance = MathUtils.random(0f, 100f);

		int x = MathUtils.random(1, 19);
		System.out.println(chance);
		if (chance < diamondProbability) {
			System.out.println("New Diamond");
			return new Diamond(new Vector2(x, 20));
		} else if (chance < sapphireProbability) {
			System.out.println("New Sapphire");
			return new Sapphire(new Vector2(x, 20));
		} else if (chance < rubyProbability) {
			System.out.println("New Ruby");
			return new Ruby(new Vector2(x, 20));
		} else if (chance < emeraldProbability) {
			System.out.println("New Emerald");
			return new Emerald(new Vector2(x, 20));
		}
		return new Rock(new Vector2(x, 20));

	}

	public Player getJim() {
		return player;
	}

	public void setJim(Player player) {
		this.player = player;
	}

	public Array<FallingEntity> getFallingEntity() {
		return fallingEntity;
	}

	public void setFallingEntity(Array<FallingEntity> fallingEntity) {
		this.fallingEntity = fallingEntity;
	}

	public boolean isSpawnOn() {
		return spawnOn;
	}

	public void setSpawnOn(boolean spawnOn) {
		this.spawnOn = spawnOn;
	}

	public float getLastEntity() {
		return lastEntity;
	}

	public void setLastEntity(float lastEntity) {
		this.lastEntity = lastEntity;
	}

	public Array<Body> getTmpBodies() {
		return entitiesToBeRemoved;
	}

	@Override
	public void beginContact(Contact contact) {

	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

		for (FallingEntity entity : fallingEntity) {
			if (contact.getFixtureA().getBody().getUserData() == entity && contact.getFixtureB() == boundary.getGroundFixture()) {
				fallingEntity.removeValue(entity, true);
				entitiesToBeRemoved.add(entity.getBody());
			}
			if (contact.getFixtureA() == boundary.getGroundFixture()
					&& contact.getFixtureB().getBody().getUserData() == entity) {
				fallingEntity.removeValue(entity, true);
				entitiesToBeRemoved.add(entity.getBody());

			}
		}

	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		return false;
	}

}
