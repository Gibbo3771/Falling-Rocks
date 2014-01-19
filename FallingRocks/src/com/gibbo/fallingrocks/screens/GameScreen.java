package com.gibbo.fallingrocks.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.fallingrocks.CollisionChecker;
import com.gibbo.fallingrocks.controller.JimController;
import com.gibbo.fallingrocks.model.Diamond;
import com.gibbo.fallingrocks.model.Emerald;
import com.gibbo.fallingrocks.model.FallingEntity;
import com.gibbo.fallingrocks.model.Jim;
import com.gibbo.fallingrocks.model.Rock;
import com.gibbo.fallingrocks.model.Ruby;
import com.gibbo.fallingrocks.model.Sapphire;
import com.gibbo.fallingrocks.view.WorldRenderer;

public class GameScreen implements Screen {

	// Object instances
	private WorldRenderer renderer;
	public static JimController controller;

	// Jim
	public static Jim jim;

	// FallingEntities
	public static Array<FallingEntity> fallingEntity;
	public static Iterator<FallingEntity> iter;

	// Camera and spritebatch
	OrthographicCamera cam;
	SpriteBatch batch;

	// Debugging
	ShapeRenderer debugRender;
	
	//Rectangles, Collision Testing
	public static Array<Rectangle> rectangle;
	public static Rectangle one;
	CollisionChecker collisionCheck;

	// Variables
	public boolean spawnOn = true;
	float lastEntity;
	int emeraldProbability;
	int rubyProbability;
	int sapphireProbability;
	int diamondProbability;

	int emer;
	int ruby;
	int saph;
	int diam;
	int rock;
	int totalGems;
	
	// Jims position
	float oldX, oldY;
	
	public GameScreen() {

		// Instantiate our objects
//		controller = new JimController();
		renderer = new WorldRenderer();
		jim = new Jim(200, 0);

		fallingEntity = new Array<FallingEntity>();
		
		one = new Rectangle();
		
		one.x = 320 - one.getWidth();
		one.y = 0;
		one.width = 32;
		one.height = 32;
		
		Gdx.input.setInputProcessor(jim);


	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		//Checks if Jim is alive, if true set state to DEAD and turn spawns off
		if(jim.getHealth() < 0){
			jim.health = 0;
			jim.currentState = 2;
			spawnOn = false;
			diam = 0;
		}
		
		if(jim.currentState == 2){
			if(totalGems < 404){
			totalGems += 4;
			fallingEntity.add(new Diamond());
			fallingEntity.add(new Sapphire());
			fallingEntity.add(new Ruby());
			fallingEntity.add(new Emerald());
			System.out.println(totalGems);
			}
		}
		
		// TODO Remove? Old Controller, no longer used
		// Update controller
//		controller.update(delta);
		
		// Update our renderer
		renderer.update();
		// Update jim
		jim.update(delta);

		// Checks if spawning allowed, then spawns entity every 2 seconds
		if(spawnOn ){
		if (TimeUtils.nanoTime() - lastEntity > 200000000) {
			spawnEntity();
		}
		}
		
		// Iterate through entities, update them and check for collisions and apply game logic
		iter = fallingEntity.iterator();
		while (iter.hasNext()) {
			FallingEntity entity = iter.next();
			entity.update(delta);
			if (entity.getBounds().overlaps(jim.body) || entity.getBounds().overlaps(jim.head)) {
				if(jim.currentState == 1 || jim.currentState == 0){
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


		// Edge collisions
		if (jim.body.x < 0) {
			jim.body.x = 0;
			jim.head.x = 0 + jim.head.getWidth() - 6;
			jim.velocity.x = 0;
			jim.currentState = 0;
		} else if (jim.body.x > Gdx.graphics.getWidth()
				- jim.body.getWidth()) {
			jim.body.x = Gdx.graphics.getWidth() - jim.body.getWidth();
			jim.head.x = Gdx.graphics.getWidth() - jim.head.getWidth() - 9;
			jim.velocity.x = 0;
			jim.currentState = 0;
		}
		if(jim.body.y < 0){
			jim.body.y = 0;
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
			if(jim.body.x == 0 || jim.body.x == Gdx.graphics.getWidth() - jim.body.getWidth()){
				fallingEntity.add(new Rock());
				
			}
			System.out.println("Creating Rock: " + rock);
		}
		lastEntity = TimeUtils.nanoTime();

	}

}
