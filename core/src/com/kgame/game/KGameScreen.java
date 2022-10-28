package com.kgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;

public class KGameScreen implements Screen {
    final KGame game;
    OrthographicCamera camera;

    HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
    FileHandle[] levelsList;
    File firstMap, secondMap;

    Music inGameMusic;
    Integer highScore, score;
    Long scoreLastSecond;
    Rectangle player;
    Boolean reversed;

    public KGameScreen(KGame kgame) {
        this.game = kgame;
        game.font.getData().setScale(2.5f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);

        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("Json/configuration.json"));

        for (int index = 0; index < base.getInt("number"); index++)
        {
            textureMap.put(base.get("names").getString(index),
                new Texture(Gdx.files.internal(base.get("paths").getString(index)))
            );
        }

        levelsList = Gdx.files.internal("maps").list();
        firstMap = Gdx.files.internal("maps/KMap0.txt").file();
        secondMap = Gdx.files.internal("maps/KMap11.txt").file();

        inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KIngame.mp3"));
        inGameMusic.setLooping(true);
        inGameMusic.play();

        highScore = base.getInt("high-score");
        score = 0;
        reversed = false;

        player = new Rectangle();
        player.width = ScreenSize.height / 12.0f;
        player.height = ScreenSize.height / 10.0f;
        player.x = ScreenSize.height / 12.0f;
        player.y = player.height;

        increaseScore();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f); // Effacement du fond en noir

        if (Gdx.input.isTouched()) {
            reversed = !reversed;
        }

        if (TimeUtils.millis() - scoreLastSecond > 1000) increaseScore();

        game.batch.begin();

        game.font.draw(game.batch, "Score : " + score, ScreenSize.width * 0.05f, ScreenSize.height * 0.95f);
        if (!reversed) game.batch.draw(textureMap.get("Player"), player.x, player.y, player.width, player.height);
        if (reversed) game.batch.draw(textureMap.get("Player-reverse"), player.x, ScreenSize.height - player.y - player.height*1.5f, player.width, player.height);

        game.batch.end();
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
        for (Map.Entry<String, Texture> texture: textureMap.entrySet())
        {
            if (texture.getKey() != null && texture.getValue() != null) texture.getValue().dispose();
        }

        inGameMusic.stop();
        inGameMusic.dispose();
    }

    protected void increaseScore()
    {
        score++;
        scoreLastSecond = TimeUtils.millis();
    }
}
