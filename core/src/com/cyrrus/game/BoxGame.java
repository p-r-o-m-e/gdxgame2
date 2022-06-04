package com.cyrrus.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.cyrrus.game.screens.PlayScreen;

public class BoxGame extends Game {
	public final String Device_platform;

	public BoxGame(String platform){
		this.Device_platform = platform;
	}
	
	@Override
	public void create () {
		this.setScreen(new PlayScreen());
	}

	@Override
	public void render () {
		super.render();
		ScreenUtils.clear(0.1f, 0.1f, .1f, 1);

	}
	
	@Override
	public void dispose () {
		super.dispose();
		this.getScreen().dispose();
	}
}
