package com.kgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KGameScreen implements Screen {
    final KGame game;
    Random randomGenerator;
    OrthographicCamera camera;

    HashMap<String, Texture> textureMap = new HashMap<>();
    FileHandle[] levelsList;
    Reader firstMap, secondMap;
    Integer mapPosition, mapMoveSpeed;
    Rectangle cell;

    Music inGameMusic;
    Integer highScore, score;
    Long scoreLastSecond;

    Rectangle player;
    Boolean reversing;
    Integer reverseIncrement;

    public KGameScreen(KGame kgame) throws FileNotFoundException {
        this.game = kgame;
        game.font.getData().setScale(2.5f);
        randomGenerator = new Random();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);

        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("Configuration/configuration.json"));

        for (int index = 0; index < base.getInt("number"); index++)
        {
            textureMap.put(base.get("names").getString(index),
                new Texture(Gdx.files.internal(base.get("paths").getString(index)))
            );
        }

        levelsList = Gdx.files.internal("Maps").list();
        firstMap = Gdx.files.internal("Maps/KMap0.txt").reader();
        secondMap = Gdx.files.internal("Maps/KMap11.txt").reader();
        mapPosition = 0;
        mapMoveSpeed = ScreenSize.iheight / 144;

        cell = new Rectangle();
        cell.width = ScreenSize.height / 12.0f;
        cell.height = ScreenSize.height / 12.0f;

        inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KIngame.mp3"));
        inGameMusic.setLooping(true);
        inGameMusic.play();

        highScore = base.getInt("high-score");
        score = 0;
        scoreLastSecond = TimeUtils.millis();

        player = new Rectangle();
        player.width = ScreenSize.height / 9.0f;
        player.height = ScreenSize.height / 7.5f;
        player.x = ScreenSize.height / 12.0f;
        player.y = player.height;

        reversing = false;
        reverseIncrement = -mapMoveSpeed;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f); // Effacement du fond en noir

        if (Gdx.input.justTouched()) {
            reversing = true;
            if (reverseIncrement > 0) reverseIncrement = -mapMoveSpeed;
            else reverseIncrement = mapMoveSpeed;
        }

        mapPosition += mapMoveSpeed;
        if (mapPosition >= ScreenSize.iwidth)
        {
            mapPosition = 0;
            firstMap = secondMap;
            try {
                secondMap = new FileReader(levelsList[randomGenerator.nextInt(levelsList.length)].file());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (reversing) reversing = applyGravity();

        if (TimeUtils.millis() - scoreLastSecond > 1000) increaseScoreAndSpeed();

        game.batch.begin();

        try {
            drawMap(firstMap, 0.0f);
            drawMap(secondMap, ScreenSize.width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        game.font.draw(game.batch, "Score : " + score, ScreenSize.width * 0.05f, ScreenSize.height * 0.95f);
        if (reverseIncrement < 0) game.batch.draw(textureMap.get("Player"), player.x, player.y, player.width, player.height);
        else game.batch.draw(textureMap.get("Player-reverse"), player.x, player.y, player.width, player.height);

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
        try {
            firstMap.close();
            secondMap.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void increaseScoreAndSpeed()
    {
        score++;
        mapMoveSpeed += ScreenSize.iheight / 720;
        scoreLastSecond = TimeUtils.millis();
    }

    protected Boolean applyGravity() {
        player.y += reverseIncrement;
        if (player.y < player.height)
        {
            player.y = player.height;
            return false;
        }
        if (player.y > ScreenSize.height * 0.9f - player.height)
        {
            player.y = ScreenSize.height * 0.9f - player.height;
            return false;
        }
        return true;
    }

    protected void drawMap(Reader file, Float offset) throws IOException {
        BufferedReader reader = new BufferedReader(file);
        int block, x;
        for (int y = 0; y < 12; y++)
        {
            x = 0;
            for (String blockWord: reader.readLine().split(" "))
            {
                block = Integer.parseInt(blockWord);
                if (block != 0)
                {
                    cell.x = x * ScreenSize.height / 12.0f + offset;
                    cell.y = y * ScreenSize.height / 12.0f;

                    if (block == 1) game.batch.draw(textureMap.get("Wall"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 2) game.batch.draw(textureMap.get("Gold"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 3) game.batch.draw(textureMap.get("Reverse"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 4) game.batch.draw(textureMap.get("Speed"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 5) game.batch.draw(textureMap.get("Void"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 6) game.batch.draw(textureMap.get("Boost"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 7) game.batch.draw(textureMap.get("Benjamin"), cell.x, cell.y, cell.width, cell.height);
                    else if (block == 9) game.batch.draw(textureMap.get("Ground"), cell.x, cell.y, cell.width, cell.height);
                }
                x++;
            }
        }
        reader.close();
    }
}
