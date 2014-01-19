package com.gibbo.fallingrocks;

/**
 * @author Stephen Gibson
 * 
 * Description:
 * Falling Rocks is a simple game where the player controls a character entity via the keyboard, navigating them
 * left and right and avoiding any rocks. The player can increase there score for every rock they avoid but also
 * get extra points by collecting gems and perhaps power ups
 * 
 * TODO:
 * Fix animations so they flip correctly when character turns left - DONE
 * Create a working state machine for Jims movement - DONE
 * 
 * Work on getting the rocks/gems automatically spawn 	- Rocks done
 * 														-
 * Sort out collisions next maybe 	- Done
 * 									-
 * 
 * Sort out death, healthbar and scoring
 *  
 * 
 */

import com.badlogic.gdx.Game;
import com.gibbo.fallingrocks.screens.GameScreen;

public class FallingRocks extends Game {

	
	@Override
	public void create() {	
		setScreen(new GameScreen());
		System.out.println("Initiating, changing screen");
		
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
