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

package com.gibbo.fallingrocks.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.engine.render.WorldRenderer;
import com.gibbo.fallingrocks.entity.Diamond;
import com.gibbo.fallingrocks.entity.Emerald;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Jim;
import com.gibbo.fallingrocks.entity.Rock;
import com.gibbo.fallingrocks.entity.Ruby;
import com.gibbo.fallingrocks.entity.Sapphire;

public class GameScreen implements Screen {

	// Jim
	private Jim jim;

	// Object instances
	private WorldRenderer renderer;

	// FallingEntities
	public static Array<FallingEntity> fallingEntity;
	public static Iterator<FallingEntity> iter;

	// Variables
	private boolean spawnOn = true;
	private float lastEntity;
	private int emeraldProbability;
	private int rubyProbability;
	private int sapphireProbability;
	private int diamondProbability;

	// Total number of each in play
	private int emer;
	private int ruby;
	private int saph;
	private int diam;
	private int rock;

	public GameScreen() {

		// Instantiate our objects
		jim = new Jim(200, 0);
		renderer = new WorldRenderer(jim);

		fallingEntity = new Array<FallingEntity>();

		Gdx.input.setInputProcessor(jim);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Placeholder for death
		if (jim.isDead()) {
			spawnOn = false;
		}

		// Update our renderer
		renderer.update();
		// Update jim
		jim.update(delta);

		// Checks if spawning allowed, then spawns entity every 2 seconds
		if (spawnOn) {
			if (TimeUtils.nanoTime() - lastEntity > 200000000) {
				spawnEntity();
			}
		}

		// Iterate through entities, update them and check for collisions and
		// apply game logic
		iter = fallingEntity.iterator();
		while (iter.hasNext()) {
			FallingEntity entity = iter.next();
			entity.update(delta);
			if (entity.getBounds().overlaps(jim.body)
					|| entity.getBounds().overlaps(jim.head)) {
				if (jim.currentState == 1 || jim.currentState == 0) {
					iter.remove();
					// Damage/increase score
					jim.health -= entity.getDmg();
					jim.score += entity.getValue();
				}
				System.out.println(jim.health);
			}
			if (entity.getBounds().y < 0 - entity.getHeight()) {
				iter.remove();
			}
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		jim.dispose();
		renderer.dispose();
	}

	public void spawnEntity() {
		emeraldProbability = MathUtils.random(1, 20);
		rubyProbability = MathUtils.random(1, 40);
		sapphireProbability = MathUtils.random(1, 70);
		diamondProbability = MathUtils.random(1, 120);

		if (emeraldProbability == 10) {
			fallingEntity.add(new Emerald());
			emer += 1;
			System.out.println("Creating Emerald: " + emer);
		} else if (rubyProbability == 20) {
			fallingEntity.add(new Ruby());
			ruby += 1;
			System.out.println("Creating Ruby: " + ruby);
		} else if (sapphireProbability == 35) {
			fallingEntity.add(new Sapphire());
			saph += 1;
			System.out.println("Creating Sapphire: " + saph);
		} else if (diamondProbability == 60) {
			fallingEntity.add(new Diamond());
			diam += 1;
			System.out.println("Creating Diamond: " + diam);
		} else {
			fallingEntity.add(new Rock());
			rock += 1;
			if (jim.body.x == 0
					|| jim.body.x == Gdx.graphics.getWidth()
							- jim.body.getWidth()) {
				fallingEntity.add(new Rock());

			}
			System.out.println("Creating Rock: " + rock);
		}
		lastEntity = TimeUtils.nanoTime();

	}

}
