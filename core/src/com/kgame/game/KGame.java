package com.kgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class KGame extends Game {
	SpriteBatch batch, saveBatch = null;
	public BitmapFont font, saveFont = null;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(); // Utilisation de la font Arial de libGDX's par défaut
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}
}
