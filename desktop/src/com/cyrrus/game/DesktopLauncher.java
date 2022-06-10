package com.cyrrus.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cyrrus.game.BoxGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(800, 540);
//		config.setResizable(false);
		config.useVsync(true);
		config.setTitle("Clash of Cars : 8 bit warfare");
		new Lwjgl3Application(new BoxGame("Desktop"), config);
	}
}
