package com.kgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final KGame game;
    OrthographicCamera camera;

    Music menuMusic;

    public MainMenuScreen(KGame kGame) {
        this.game = kGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KMenu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    @Override
    public void show() {
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f); // Effacement du fond en noir

        camera.update(); // Actualisation de la caméra
        game.batch.setProjectionMatrix(camera.combined); // Établissement de la matrice de projection

        game.batch.begin();
        game.font.getData().setScale(4.0f);
        game.font.draw(game.batch, "Bienvenue sur le KGame !", ScreenSize.width / 5.0f, ScreenSize.height - ScreenSize.height / 5.0f);
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, "Créé par Damien Boizard en 2020", ScreenSize.width / 4.0f, ScreenSize.height - ScreenSize.height / 3.5f);
        game.font.getData().setScale(3.0f);
        game.font.draw(game.batch, "Cliquez pour jouer", ScreenSize.width / 4.5f, ScreenSize.height / 2.0f);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            menuMusic.stop();
            game.setScreen(new KGameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        menuMusic.stop();
        menuMusic.dispose();
    }
}
