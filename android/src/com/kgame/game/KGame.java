package com.kgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.FileNotFoundException;

public class KGame extends Game {
    SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Utilisation de la font Arial de libGDX's par défaut
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        try {
            this.setScreen(new KGameScreen(this));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
