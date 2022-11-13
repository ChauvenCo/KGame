package com.kgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.FileNotFoundException;

// TODO: Revoir l'écran de fin de partie | Faire en sorte de pouvoir quitter l'appli
public class EndGameScreen implements Screen {
    final KGame game;
    OrthographicCamera camera;
    Integer score, highScore;

    Music menuMusic;

    public EndGameScreen(KGame kGame, Integer score, Integer highScore) {

        this.game = kGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KMenu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.play();

        this.score = score;
        this.highScore = highScore;
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
        game.font.draw(game.batch, "Fin de la partie !", ScreenSize.width / 5.0f, ScreenSize.height - ScreenSize.height / 5.0f);
        game.font.getData().setScale(3.0f);
        game.font.draw(game.batch, "Score : " + score, ScreenSize.width / 4.5f, ScreenSize.height / 2.0f + ScreenSize.height / 10.0f);
        game.font.draw(game.batch, "Meilleur score : " + highScore, ScreenSize.width / 4.5f, ScreenSize.height / 2.0f);
        game.font.draw(game.batch, "Cliquez pour rejouer", ScreenSize.width / 4.5f, ScreenSize.height / 2.0f - ScreenSize.height / 10.0f);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            menuMusic.stop();
            try {
                game.setScreen(new KGameScreen(game));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
