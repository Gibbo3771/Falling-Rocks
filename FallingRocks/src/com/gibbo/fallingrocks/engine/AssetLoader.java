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

package com.gibbo.fallingrocks.engine;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Basic assets loader, used to load in textures or polygon information for
 * box2d
 * 
 * @author Stephen Gibson
 */
public class AssetLoader {

	/* Menu UI */
	public static Sprite BUTTON_DOWN = spriteLoader("data/img/ui/buttondown.png");
	public static Sprite BUTTON_UP = spriteLoader("data/img/ui/buttonup.png");

	/* Gems */
	public static Sprite EMERALD = spriteLoader("data/img/gems/emerald.png");
	public static Sprite RUBY = spriteLoader("data/img/gems/ruby.png");
	public static Sprite SHAPPIRE = spriteLoader("data/img/gems/sapphire.png");
	public static Sprite DIAMOND = spriteLoader("data/img/gems/diamond.png");

	/* Rocks */
	public static Sprite ROCK_1 = spriteLoader("data/img/rocks/rock1.png");
	public static Sprite ROCK_2 = spriteLoader("data/img/rocks/rock2.png");
	public static Sprite ROCK_3 = spriteLoader("data/img/rocks/rock3.png");
	public static Sprite ROCK_4 = spriteLoader("data/img/rocks/rock4.png");
	public static Sprite ROCK_5 = spriteLoader("data/img/rocks/rock5.png");

	/* Health pickups */
	public static Sprite FIRST_AID_KIT = spriteLoader("data/img/health/firstaidkit.png");
	public static Sprite MEDI_KIT = spriteLoader("data/img/health/medikit.png");

	/* Fonts */
	public static BitmapFont INDICATOR_FONT = fontGenerator(
			"data/font/pricedown.ttf", 38);
	public static BitmapFont MENU_FONT;

	/* Poly data */
	public static BodyEditorLoader GEMS = bodyLoader("data/img/gems/gems");
	public static BodyEditorLoader ROCKS = bodyLoader("data/img/rocks/rocks");
	public static BodyEditorLoader HEALTH = bodyLoader("data/img/health/health");

	/* Sounds */
	/* Effects */
	public static Sound CLICK = soundLoader("data/sound/effects/click3.wav");
	public static Sound HURT1 = soundLoader("data/sound/effects/hurt1.ogg");
	public static Sound HURT2 = soundLoader("data/sound/effects/hurt2.ogg");
	public static Sound PICKUP = soundLoader("data/sound/effects/pickup.wav");
	public static Sound PICKUP2 = soundLoader("data/sound/effects/pickup2.wav");
	public static Sound PICKUP3 = soundLoader("data/sound/effects/pickup3.wav");
	public static Sound ROCK_PASS = soundLoader("data/sound/effects/rockpass.ogg");

	/* Music */
	public static Music THEME = musicLoader("data/sound/music/theme.mp3");
	
	public AssetLoader() {

	}

	/**
	 * Re-load all assets, call when the game is re-opened from background
	 */
	public static void init() {
		/* Menu UI */
		BUTTON_DOWN = spriteLoader("data/img/ui/buttondown.png");
		BUTTON_UP = spriteLoader("data/img/ui/buttonup.png");

		/* Gems */
		EMERALD = spriteLoader("data/img/gems/emerald.png");
		RUBY = spriteLoader("data/img/gems/ruby.png");
		SHAPPIRE = spriteLoader("data/img/gems/sapphire.png");
		DIAMOND = spriteLoader("data/img/gems/diamond.png");

		/* Rocks */
		ROCK_1 = spriteLoader("data/img/rocks/rock1.png");
		ROCK_2 = spriteLoader("data/img/rocks/rock2.png");
		ROCK_3 = spriteLoader("data/img/rocks/rock3.png");
		ROCK_4 = spriteLoader("data/img/rocks/rock4.png");
		ROCK_5 = spriteLoader("data/img/rocks/rock5.png");

		/* Health pickups */
		FIRST_AID_KIT = spriteLoader("data/img/health/firstaidkit.png");
		MEDI_KIT = spriteLoader("data/img/health/medikit.png");

		/* Fonts */
		INDICATOR_FONT = fontGenerator("data/font/pricedown.ttf", 38);

		/* Poly data */
		GEMS = bodyLoader("data/img/gems/gems");
		ROCKS = bodyLoader("data/img/rocks/rocks");
		HEALTH = bodyLoader("data/img/health/health");

		/* Sounds */
		/* Effects */
		CLICK = soundLoader("data/sound/effects/click3.wav");
		HURT1 = soundLoader("data/sound/effects/hurt1.ogg");
		HURT2 = soundLoader("data/sound/effects/hurt2.ogg");
		PICKUP = soundLoader("data/sound/effects/pickup.wav");
		PICKUP2 = soundLoader("data/sound/effects/pickup2.wav");
		PICKUP3 = soundLoader("data/sound/effects/pickup3.wav");
		ROCK_PASS = soundLoader("data/sound/effects/rockpass.ogg");

		/* Music */
		THEME = musicLoader("data/sound/music/theme.mp3");
	}

	/**
	 * Dispose of all assets
	 */
	public static void dispose() {
		BUTTON_DOWN.getTexture().dispose();
		BUTTON_UP.getTexture().dispose();

		EMERALD.getTexture().dispose();
		RUBY.getTexture().dispose();
		SHAPPIRE.getTexture().dispose();
		DIAMOND.getTexture().dispose();

		ROCK_1.getTexture().dispose();
		ROCK_2.getTexture().dispose();
		ROCK_3.getTexture().dispose();
		ROCK_4.getTexture().dispose();
		ROCK_5.getTexture().dispose();

		FIRST_AID_KIT.getTexture().dispose();
		MEDI_KIT.getTexture().dispose();

		INDICATOR_FONT.dispose();

		CLICK.dispose();
		HURT1.dispose();
		HURT2.dispose();
		PICKUP.dispose();
		ROCK_PASS.dispose();
		
//		THEME.dispose();
		
		
	}

	/**
	 * Generate a new font using free font generator
	 * 
	 * @param file
	 *            file in storage
	 * @param size
	 *            size of font in pixels
	 * @return
	 */
	public static BitmapFont fontGenerator(String file, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal(file));
		BitmapFont font = generator.generateFont(size);
		generator.dispose();
		return font;
	}

	/**
	 * Load a sprite from a file
	 * 
	 * @param file
	 *            the file location in storage
	 * @return
	 */
	private static Sprite spriteLoader(String file) {
		return new Sprite(new Texture(Gdx.files.internal(file)));

	}

	/**
	 * Load a font from a file
	 * 
	 * @param file
	 *            the file location in storage
	 * @return
	 */
	@SuppressWarnings("unused")
	private static BitmapFont fontLoader(String file) {
		return new BitmapFont(Gdx.files.internal(file));
	}

	/**
	 * Load body information from a file
	 * 
	 * @param file
	 *            the file location in storage
	 * @return
	 */
	private static BodyEditorLoader bodyLoader(String file) {
		return new BodyEditorLoader(Gdx.files.internal(file));
	}

	/**
	 * Load a sound from a file
	 * 
	 * @param file
	 *            the file location in storage
	 * @return
	 */
	private static Sound soundLoader(String file) {
		return Gdx.audio.newSound(Gdx.files.internal(file));

	}

	/**
	 * Load music from file
	 * 
	 * @param file
	 *            the file location in storage
	 * @return
	 */
	private static Music musicLoader(String file) {
		return Gdx.audio.newMusic(Gdx.files.internal(file));
	}

}
