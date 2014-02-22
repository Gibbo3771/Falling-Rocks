
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
package com.gibbo.fallingrocks;

/**
 * @author Stephen Gibson
 * 
 * Description:
 * Falling Rocks is a simple game where the player controls a character entity via the keyboard, navigating them
 * left and right and avoiding any rocks. The player can increase there score for every rock they avoid but also
 * get extra points by collecting gems and perhaps power ups
 * 
 * 
 */

import com.badlogic.gdx.Game;
import com.gibbo.fallingrocks.screens.MainMenuScreen;

public class FallingRocks extends Game {
	

	
	@Override
	public void create() {	
		setScreen(new MainMenuScreen());
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
