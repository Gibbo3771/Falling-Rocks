package com.gibbo.fallingrocks;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "FallingRocks";
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 480;
		cfg.resizable = false;
		
		new LwjglApplication(new FallingRocks(), cfg);
	}
}
