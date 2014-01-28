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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.engine.WorldRenderer;
import com.gibbo.fallingrocks.entity.FallingEntity;
import com.gibbo.fallingrocks.entity.Player;

public class GameScreen implements Screen {

	// Jim
	private Player player;

	/** Level */
	private Level level;

	// World rendering
	private WorldRenderer renderer;

	// FallingEntities
	public static Array<FallingEntity> fallingEntity;

	public GameScreen() {

		// Instantiate our objects
		player = new Player(200, 0);
		renderer = new WorldRenderer(player);
		level = new Level(player);

		fallingEntity = new Array<FallingEntity>();

		Gdx.input.setInputProcessor(player);
		WorldRenderer.world.setContactListener(level);
		

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw the world and render sprites
		renderer.update(delta, level.getFallingEntity(), level.getTmpBodies());

		// Check if jim is dead, if not update him
		if (player.isDead()) {
			level.setSpawnOn(false);
			level.getFallingEntity().clear();
		} else {
			// Update jim
			player.update(delta);
		}

		// Update the status of the level, add/remove entites and update score
		// etc etc
		level.process(delta);

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
		player.dispose();
		renderer.dispose();
	}

}
