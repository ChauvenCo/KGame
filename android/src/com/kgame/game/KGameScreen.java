package com.kgame.game;

import android.content.Intent;

import androidx.core.content.ContextCompat;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KGameScreen implements Screen {
    final KGame game;
    Random randomGenerator;
    OrthographicCamera camera;

    HashMap<String, Texture> textureMap;
    Integer highScore, score;
    Long scoreLastSecond;

    FileHandle[] levelsFileList;
    HashMap<String, Level> levelsMap;
    Level firstMap, secondMap;

    Music inGameMusic;
    Float mapPosition, mapMoveSpeed;
    Rectangle cell;

    Rectangle player;
    Boolean reversing, noJoke;
    Float reverseIncrement;

    Boolean boostActivated, voidActivated;

    public KGameScreen(KGame kgame) throws FileNotFoundException {
        this.game = kgame;
        game.font.getData().setScale(2.5f);
        randomGenerator = new Random();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);

        textureMap = new HashMap<>();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("Configuration/configuration.json"));

        for (int index = 0; index < base.getInt("number"); index++)
        {
            textureMap.put(base.get("names").getString(index),
                    new Texture(Gdx.files.internal(base.get("paths").getString(index)))
            );
        }

        highScore = base.getInt("high-score");
        score = 0;
        scoreLastSecond = TimeUtils.millis();

        levelsMap = new HashMap<>();
        Level level;
        levelsFileList = Gdx.files.internal("Maps").list();

        for (FileHandle levelFile: levelsFileList)
        {
            base = json.parse(levelFile);
            level = new Level(base.getInt("width"), base.getInt("height"));
            for (int index = 0; index < level.GetWidth() * level.GetHeight(); index++) {
                level.AddCell(base.get("blocks").getInt(index));
            }
            levelsMap.put(base.getString("name"), level);
        }

        firstMap = levelsMap.get("Premium-Empty");
        secondMap = levelsMap.get("Premium-BoostDefault");
        mapPosition = 0.0f;
        mapMoveSpeed = ScreenSize.height / 240.0f;

        cell = new Rectangle();
        cell.width = ScreenSize.height / 12.0f;
        cell.height = ScreenSize.height / 12.0f;

        inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KIngame.mp3"));
        inGameMusic.setLooping(true);
        inGameMusic.play();

        player = new Rectangle();
        player.width = ScreenSize.height / 12.0f;
        player.height = ScreenSize.height / 10.0f;
        player.x = ScreenSize.height / 12.0f;
        player.y = cell.height;

        reversing = false;
        reverseIncrement = -ScreenSize.height / 72.0f - mapMoveSpeed;
        noJoke = true;

        boostActivated = false;
        voidActivated = false;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f); // Effacement du fond en noir

        if (Gdx.input.justTouched()) {
            reversing = true;
            if (reverseIncrement > 0) reverseIncrement = -(ScreenSize.height / 72.0f + mapMoveSpeed);
            else reverseIncrement = ScreenSize.height / 72.0f + mapMoveSpeed;
        }

        mapPosition += mapMoveSpeed;
        if (mapPosition >= firstMap.GetWidth() * cell.width)
        {
            //mapPosition = firstMap.GetWidth() * cell.width - ScreenSize.width;
            mapPosition = 0.0f;
            firstMap = secondMap;
            secondMap = (Level)levelsMap.values().toArray()[randomGenerator.nextInt(levelsMap.values().toArray().length)];
        }

        if (reversing) reversing = applyGravity();

        if (TimeUtils.millis() - scoreLastSecond > 1000) increaseScoreAndSpeed();

        game.batch.begin();

        try {
            drawMap(firstMap, 0.0f - mapPosition);
            drawMap(secondMap, firstMap.GetWidth() * cell.width - mapPosition);
        } catch (IOException e) {
            e.printStackTrace();
        }

        game.font.draw(game.batch, "Score : " + score, ScreenSize.width * 0.05f, ScreenSize.height * 0.97f);
        if (reverseIncrement < 0.0f) game.batch.draw(textureMap.get("Player"), player.x, player.y, player.width, player.height);
        else game.batch.draw(textureMap.get("Player-reverse"), player.x, player.y, player.width, player.height);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        ScreenSize.height = height;
        ScreenSize.width = width;
        ScreenSize.iheight = height;
        ScreenSize.iwidth = width;
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

    protected void increaseScoreAndSpeed() {
        score++;
        if (boostActivated) score++;
        mapMoveSpeed += ScreenSize.height / 1480.0f;
        scoreLastSecond = TimeUtils.millis();
    }

    protected Boolean applyGravity() {
        player.y += reverseIncrement;
        if (player.y < cell.height)
        {
            player.y = cell.height;
            return false;
        }
        if (player.y > ScreenSize.height - player.height - cell.height)
        {
            player.y = ScreenSize.height - player.height - cell.height;
            return false;
        }
        return true;
    }

    protected void applyReverse() {
        if (reversing)
        {
            if (reverseIncrement > 0.0f) player.y = ScreenSize.height - player.height - cell.height;
            else player.y = cell.height;
            reversing = false;
        }
        else
        {
            if (player.y < ScreenSize.height / 2.0f) player.y = ScreenSize.height - player.height - cell.height;
            else player.y = cell.height;
            reverseIncrement = -reverseIncrement;
        }
    }

    protected void drawMap(Level level, Float offset) throws IOException {
        int block;
        for (int y = 0; y < level.GetHeight(); y++)
        {
            for (int x = 0; x < level.GetWidth(); x++)
            {
                block = level.GetCell(y*level.GetWidth() + x);
                if (block != 0)
                {
                    cell.x = x * ScreenSize.height / 12.0f + offset;
                    cell.y = ScreenSize.height - cell.height - (y * ScreenSize.height / 12.0f);

                    if (player.overlaps(cell))
                    {
                        if (block == 1)
                        {
                            if (score > highScore) highScore = score;

                            inGameMusic.stop();
                            Intent gameActivityIntent = new Intent(AndroidLauncher.getAppContext(), EndGameActivity.class);
                            gameActivityIntent.putExtra("Score", score);
                            gameActivityIntent.putExtra("HighScore", highScore);

                            ContextCompat.startActivity(AndroidLauncher.getAppContext(), gameActivityIntent, null);
                            dispose();
                        }
                        else if (block == 2)
                        {
                            if (!boostActivated)
                            {
                                if (mapMoveSpeed > 0.0f) mapMoveSpeed -= ScreenSize.height / 740.0f;
                                mapPosition += cell.x;
                            }
                        }
                        else if (block == 3) applyReverse();
                        else if (block == 4)
                        {
                            mapMoveSpeed += ScreenSize.height / 1480.0f;
                            mapPosition += cell.x;
                        }
                        else if (block == 5)
                        {
                            applyReverse();
                            voidActivated = !voidActivated;
                        }
                        else if (block == 6)
                        {
                            if (reversing) applyReverse();
                            applyReverse();
                            if (!boostActivated) mapMoveSpeed += (ScreenSize.height / 1480.0f);
                            else mapMoveSpeed -= ScreenSize.height / 740.0f;
                            boostActivated = !boostActivated;
                        }
                        else if (block == 7)
                        {
                            mapMoveSpeed = 0.0f;
                            mapPosition += cell.x;
                        }
                    }

                    if (voidActivated)
                    {
                        if (block == 5) game.batch.draw(textureMap.get("VoidDisable"), cell.x, cell.y, cell.width, cell.height);
                        else game.batch.draw(textureMap.get("Void"), cell.x, cell.y, cell.width, cell.height);
                    }
                    else
                    {
                        if (block == 1) game.batch.draw(textureMap.get("Wall"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 2) game.batch.draw(textureMap.get("Gold"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 3) game.batch.draw(textureMap.get("Reverse"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 4) game.batch.draw(textureMap.get("Speed"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 5) game.batch.draw(textureMap.get("Void"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 6) game.batch.draw(textureMap.get("Boost"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 7) game.batch.draw(textureMap.get("Benjamin"), cell.x, cell.y, cell.width, cell.height);
                        else if (block == 9) game.batch.draw(textureMap.get("Ground"), cell.x, cell.y, cell.width, cell.height);
                    }
                }
            }
        }
        if (mapMoveSpeed < 0.0f) mapMoveSpeed = 0.0f;
    }
}
