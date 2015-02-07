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

package com.gibbo.fallingrocks.engine.UI;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gibbo.fallingrocks.engine.AssetLoader;
import com.gibbo.fallingrocks.engine.Level;
import com.gibbo.fallingrocks.engine.Level.Difficulty;
import com.gibbo.fallingrocks.engine.animation.ActorAccessor;
import com.gibbo.fallingrocks.entity.Player;
import com.gibbo.fallingrocks.screens.DifficultySelectScreen;
import com.gibbo.fallingrocks.screens.GameScreen;
import com.gibbo.fallingrocks.screens.MainMenuScreen;

/**
 * Responsible for handling the stage and user input to navigate menus
 * 
 * @author Stephen Gibson
 */
public class UIManager extends Stage {

	/** Camera */
	public OrthographicCamera cam;

	/** Skin */
	public Skin skin;
	/** Text Button Style */
	public TextButtonStyle buttonStyle = new TextButtonStyle();
	/** Label Style */
	public LabelStyle labelStyle = new LabelStyle();

	/** Main table */
	public Table table;

	/** Player */
	public Player player;

	/** For animation Tweening */
	public TweenManager tweenMan;

	public UIManager() {

		cam = new OrthographicCamera(640, 480);
		cam.setToOrtho(false);
		setCamera(cam);

		skin = new Skin();
		table = new Table(skin);
		table.debug();

		player = new Player(new World(new Vector2(0, 0), true));

		tweenMan = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			AssetLoader.BUTTON_UP.setSize(256, 128);
			AssetLoader.BUTTON_DOWN.setSize(256, 128);
		}

		skin.add("buttondown", AssetLoader.BUTTON_DOWN);
		skin.add("buttonup", AssetLoader.BUTTON_UP);

	}

	/**
	 * Called when you initialise the main menu
	 */
	public void initMain() {

		buttonStyle.font = Gdx.app.getType() == ApplicationType.Desktop ? AssetLoader
				.fontGenerator("data/font/holdyourbreath.ttf", 28)
				: AssetLoader.fontGenerator("data/font/holdyourbreath.ttf", 60);
		buttonStyle.down = skin.getDrawable("buttondown");
		buttonStyle.up = skin.getDrawable("buttonup");
		buttonStyle.unpressedOffsetX = -2;
		buttonStyle.unpressedOffsetY = 2;

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			labelStyle.font = AssetLoader.fontGenerator(
					"data/font/holdyourbreath.ttf", 48);
		} else {
			labelStyle.font = AssetLoader.fontGenerator(
					"data/font/holdyourbreath.ttf", 156);
		}

		final Label heading = new Label("Falling Rocks!", labelStyle);
		final TextButton play = new TextButton("PLAY", buttonStyle);
		final TextButton highscores = new TextButton("HIGHSCORES", buttonStyle);
		final TextButton exit = new TextButton("EXIT", buttonStyle);

		Tween.set(heading, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(play, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(highscores, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(exit, ActorAccessor.ALPHA).target(0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						Tween.to(play, ActorAccessor.ALPHA, 0.25f).target(1)
								.start(tweenMan);
						Tween.to(highscores, ActorAccessor.ALPHA, 0.25f)
								.target(1).start(tweenMan);
						Tween.to(exit, ActorAccessor.ALPHA, 0.25f).target(1)
								.start(tweenMan);
						Tween.to(heading, ActorAccessor.ALPHA, 0.25f).target(1)
								.start(tweenMan);

					}
				}).start(tweenMan);

		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);
				Tween.to(play, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(highscores, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(exit, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(0)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								table.clear();
								((Game) Gdx.app.getApplicationListener())
										.setScreen(new DifficultySelectScreen());
							}

						}).start(tweenMan);
			}
		});

		highscores.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);
//				Swarm.showLeaderboards();
				 highscores.setText("COMING\nSOON!");

			}
		});

		exit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);
				Tween.to(play, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(highscores, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(exit, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(0)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								table.clear();
								player.dispose();
								Gdx.app.exit();
							}

						}).start(tweenMan);
			}
		});

		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.setFillParent(true);

		table.add(heading).row().spaceTop(50);
		table.add(play).row();
		if (Gdx.app.getType() == ApplicationType.Android)
			table.add(highscores).row();
		table.add(exit);
		addActor(table);

	}

	/**
	 * Called when you want to load the difficulty select screen
	 */
	public void initDiffSelect() {

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			AssetLoader.BUTTON_UP.setSize(192, 96);
			AssetLoader.BUTTON_DOWN.setSize(192, 96);
		}

		buttonStyle.font = Gdx.app.getType() == ApplicationType.Desktop ? AssetLoader
				.fontGenerator("data/font/holdyourbreath.ttf", 28)
				: AssetLoader.fontGenerator("data/font/holdyourbreath.ttf", 60);
		buttonStyle.down = skin.getDrawable("buttondown");
		buttonStyle.up = skin.getDrawable("buttonup");
		buttonStyle.unpressedOffsetX = -2;
		buttonStyle.unpressedOffsetY = 2;

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			labelStyle.font = AssetLoader.fontGenerator(
					"data/font/holdyourbreath.ttf", 48);
		} else {
			labelStyle.font = AssetLoader.fontGenerator(
					"data/font/holdyourbreath.ttf", 108);
		}

		final Label heading = new Label("Select Difficulty", labelStyle);
		final TextButton easy = new TextButton("EASY", buttonStyle);
		final TextButton normal = new TextButton("NORMAL", buttonStyle);
		final TextButton hard = new TextButton(
				player.getProfile().isHardUnlocked ? "HARD" : "(LOCKED)",
				buttonStyle);
		final TextButton impossibru = new TextButton(
				player.getProfile().isImpossibruUnlocked ? "IMPOSSIBRU"
						: "(LOCKED)", buttonStyle);

		Tween.set(heading, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(easy, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(normal, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(hard, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(impossibru, ActorAccessor.ALPHA).target(0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {

						Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(1)
								.start(tweenMan);
						Tween.to(easy, ActorAccessor.ALPHA, 0.5f).target(1)
								.start(tweenMan);
						Tween.to(normal, ActorAccessor.ALPHA, 0.5f).target(1)
								.start(tweenMan);
						Tween.to(hard, ActorAccessor.ALPHA, 0.5f).target(1)
								.start(tweenMan);
						Tween.to(impossibru, ActorAccessor.ALPHA, 0.5f)
								.target(1).start(tweenMan);

					}
				}).start(tweenMan);

		ScrollPaneStyle scrollStyle = new ScrollPaneStyle();
		skin.add("default", scrollStyle);
		Table paneTable = new Table();
		paneTable.add(easy).row();
		paneTable.add(normal).row();
		paneTable.add(hard).row();
		paneTable.add(impossibru);
		ScrollPane pane = new ScrollPane(paneTable, skin);
		pane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		easy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);

				Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(easy, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(normal, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(hard, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(impossibru, ActorAccessor.ALPHA, 0.5f).target(0)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								table.clear();
								Level.difficulty = Difficulty.EASY;
								((Game) Gdx.app.getApplicationListener())
										.setScreen(new GameScreen());

							}
						}).start(tweenMan);
			}
		});

		normal.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);

				Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(easy, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(normal, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(hard, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(impossibru, ActorAccessor.ALPHA, 0.5f).target(0)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								table.clear();
								Level.difficulty = Difficulty.NORMAL;
								((Game) Gdx.app.getApplicationListener())
										.setScreen(new GameScreen());

							}
						}).start(tweenMan);
			}
		});

		hard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);

				if (player.getProfile().isHardUnlocked) {
					Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(easy, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(normal, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(hard, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(impossibru, ActorAccessor.ALPHA, 0.5f).target(0)
							.setCallback(new TweenCallback() {

								@Override
								public void onEvent(int type,
										BaseTween<?> source) {
									table.clear();
									Level.difficulty = Difficulty.HARD;
									((Game) Gdx.app.getApplicationListener())
											.setScreen(new GameScreen());

								}
							}).start(tweenMan);
				} else {
					hard.setText("Achieve\n 25,000\n Highscore");
				}

			}
		});

		impossibru.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play(1);

				if (player.getProfile().isImpossibruUnlocked) {
					Tween.to(heading, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(easy, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(normal, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(hard, ActorAccessor.ALPHA, 0.5f).target(0)
							.start(tweenMan);
					Tween.to(impossibru, ActorAccessor.ALPHA, 0.5f).target(0)
							.setCallback(new TweenCallback() {

								@Override
								public void onEvent(int type,
										BaseTween<?> source) {
									table.clear();
									Level.difficulty = Difficulty.IMPOSSIBRU;
									((Game) Gdx.app.getApplicationListener())
											.setScreen(new GameScreen());

								}
							}).start(tweenMan);
				} else {
					impossibru.setText("Achieve\n 100,000\n Highscore");
				}

			}
		});

		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.setFillParent(true);

		table.add(heading).row();
		table.add(pane).expand().fillX();
		addActor(table);

	}

	public void initDeath(final Difficulty difficulty, final double score) {


		buttonStyle.font = Gdx.app.getType() == ApplicationType.Desktop ? AssetLoader
				.fontGenerator("data/font/holdyourbreath.ttf", 28)
				: AssetLoader.fontGenerator("data/font/holdyourbreath.ttf", 60);
		buttonStyle.down = skin.getDrawable("buttondown");
		buttonStyle.up = skin.getDrawable("buttonup");
		buttonStyle.unpressedOffsetX = -2;
		buttonStyle.unpressedOffsetY = 2;

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			labelStyle.font = AssetLoader.fontGenerator(
					"data/font/holdyourbreath.ttf", 48);
		} else {
			labelStyle.font = AssetLoader.fontGenerator(
					"data/font/holdyourbreath.ttf", 138);
		}

		final Label scoreDisplay = new Label("SCORE", labelStyle);
		final Label actualyScore = new Label("" + (int) score, labelStyle);
		final TextButton retry = new TextButton("RETRY", buttonStyle);
		final TextButton submit = new TextButton("SUBMIT", buttonStyle);
		final TextButton menu = new TextButton("MAIN MENU", buttonStyle);
		
		ScrollPaneStyle scrollStyle = new ScrollPaneStyle();
		skin.add("default", scrollStyle);
		Table paneTable = new Table();
		paneTable.add(retry).row();
//		paneTable.add(submit).row();
		paneTable.add(menu).row();
		ScrollPane pane = new ScrollPane(paneTable, skin);
		pane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Tween.set(scoreDisplay, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(actualyScore, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(retry, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(submit, ActorAccessor.ALPHA).target(0).start(tweenMan);
		Tween.set(menu, ActorAccessor.ALPHA).target(0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						Tween.to(scoreDisplay, ActorAccessor.ALPHA, 0.5f)
								.target(1).start(tweenMan);
						Tween.to(retry, ActorAccessor.ALPHA, 0.5f).target(1)
								.start(tweenMan);
						Tween.to(submit, ActorAccessor.ALPHA, 0.5f).target(1)
						.start(tweenMan);
						Tween.to(menu, ActorAccessor.ALPHA, 0.5f).target(1)
								.start(tweenMan);
						Tween.to(actualyScore, ActorAccessor.ALPHA, 0.5f)
								.target(1).start(tweenMan);

					}
				}).start(tweenMan);

		retry.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AssetLoader.CLICK.play();
				Tween.to(scoreDisplay, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(actualyScore, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(retry, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(submit, ActorAccessor.ALPHA, 0.5f).target(0)
				.start(tweenMan);
				Tween.to(menu, ActorAccessor.ALPHA, 0.5f).target(0)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								Level.difficulty = difficulty;
								((Game) Gdx.app.getApplicationListener())
										.setScreen(new GameScreen());

							}
						}).start(tweenMan);
			}

		});

		submit.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//					switch (difficulty) {
//					case EASY:
//						SwarmLeaderboard.submitScore(13704, (int) score);
//						break;
//					case NORMAL:
//						SwarmLeaderboard.submitScore(13706, (int) score);
//						break;
//					case HARD:
//						SwarmLeaderboard.submitScore(13708, (int) score);
//						break;
//					case IMPOSSIBRU:
//						SwarmLeaderboard.submitScore(13710, (int) score);
//						break;
//					default:
//						break;
//				}
//					submit.setText("SUBMITTED");
//					submit.clearListeners();
//				
//				
//			}
		});

		menu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				AssetLoader.CLICK.play();
				Tween.to(scoreDisplay, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(actualyScore, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(retry, ActorAccessor.ALPHA, 0.5f).target(0)
						.start(tweenMan);
				Tween.to(submit, ActorAccessor.ALPHA, 0.5f).target(0)
				.start(tweenMan);
				Tween.to(menu, ActorAccessor.ALPHA, 0.5f).target(0)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) {
								((Game) Gdx.app.getApplicationListener())
										.setScreen(new MainMenuScreen());

							}
						}).start(tweenMan);
			}
		});

		table.setFillParent(true);
		table.add(scoreDisplay).row();
		table.add(actualyScore).row().spaceTop(20);
		table.add(pane);
		addActor(table);

	}

	@Override
	public void dispose() {
		player.dispose();
		super.dispose();
	}

}
