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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gibbo.fallingrocks.engine.EntityFactory;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.engine.Level.Difficulty;
import com.gibbo.fallingrocks.engine.WorldRenderer;
import com.gibbo.fallingrocks.entity.Player;

public class GameScreen implements Screen {

	/** Level */
	private Level level;

	/** World renderer */
	private WorldRenderer renderer;
	
	public static boolean gameOver;

	public GameScreen() {
		Level.difficulty = Difficulty.NORMAL;

		// Instantiate our objects
		World world = new World(new Vector2(0, -9.81f), true);
		Player player = new Player(world);
		EntityFactory factory = new EntityFactory(player);

		renderer = new WorldRenderer(factory, world);
		level = new Level(factory);
		factory.setNewSpawn(0.20f / Level.difficulty.getValue());
//		player.attachTorch();

		WorldRenderer.world.setContactListener(level);
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(renderer);
		Gdx.input.setInputProcessor(multi);
		Gdx.input.setCursorCatched(true);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		if (gameOver) {
			dispose();
			Gdx.input.setInputProcessor(null);
			gameOver = false;
			((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
		}else{
			level.update(delta);
			renderer.update(delta);
			
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
		level.dispose();
		renderer.dispose();
	}

}
