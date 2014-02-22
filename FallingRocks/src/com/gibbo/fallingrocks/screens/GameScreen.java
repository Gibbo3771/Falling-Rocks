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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.gibbo.fallingrocks.engine.AssetLoader;
import com.gibbo.fallingrocks.engine.Level;

public class GameScreen implements Screen {

	/** Level */
	private Level level;

	/** Determines the game is over or not */
	public static boolean gameOver;

	public GameScreen() {
		AssetLoader.init();
		level = new Level();
		

		AssetLoader.THEME.play();
		AssetLoader.THEME.setVolume(0.75f);
		AssetLoader.THEME.setLooping(true);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (gameOver) {
			Gdx.input.setInputProcessor(null);
			gameOver = false;
			AssetLoader.THEME.stop();
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new DeathScreen(level));
		} else {
			level.update(delta);
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
		Level.getPlayer().save();
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
		AssetLoader.dispose();
	}

}
