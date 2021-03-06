/**
 * Copyright 2014 Stephen Gibson
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
import com.gibbo.fallingrocks.engine.AssetLoader;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.engine.UI.UIManager;

/**
 * 
 * @author Stephen Gibson
 */
public class DeathScreen implements Screen {

	/** Player score at time of death */
	public double score;

	/** UIManager */
	public UIManager manager;

	@SuppressWarnings("static-access")
	public DeathScreen(Level level) {
		this.score = level.player.getScore();

		Gdx.input.setCursorCatched(false);

		AssetLoader.init();
		manager = new UIManager();
		manager.initDeath(Level.difficulty, score);
		Gdx.input.setInputProcessor(manager);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		manager.tweenMan.update(delta);
		manager.act(delta);
		manager.draw();

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

	}

}
