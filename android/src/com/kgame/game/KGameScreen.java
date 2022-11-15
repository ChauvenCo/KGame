package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
    // Déclaration des variables de jeu

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

    Boolean boostActivated, voidActivated, showFPS, playMusics;

    // Constructeur de l'objet, initialisation des variables de jeu

    public KGameScreen(KGame kgame) throws FileNotFoundException {
        this.game = kgame;
        game.font.getData().setScale(2.5f);
        randomGenerator = new Random();

        // Gestion d ela caméra

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenSize.width, ScreenSize.height);

        // Récupération des options

        SharedPreferences sharedPref = ((Activity)AndroidLauncher.getAppContext()).getPreferences(Context.MODE_PRIVATE);
        showFPS = sharedPref.getBoolean("SHOWFPS", false);
        playMusics = sharedPref.getBoolean("PLAYMUSICS", true);

        // Récupération du contenu du fichier de configuration

        textureMap = new HashMap<>();
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("Configuration/configuration.json"));

        for (int index = 0; index < base.getInt("number"); index++)
        {
            textureMap.put(base.get("names").getString(index),
                    new Texture(Gdx.files.internal(base.get("paths").getString(index)))
            );
        }

        // Gestion des scores

        highScore = base.getInt("high-score");
        score = 0;
        scoreLastSecond = TimeUtils.millis();

        // Récupération du contenu des maps

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

        // Gestion des deux premières maps (fixes pour toutes les parties)

        firstMap = levelsMap.get("Premium-Empty");
        secondMap = levelsMap.get("Premium-BoostDefault");
        mapPosition = 0.0f;
        mapMoveSpeed = ScreenSize.height / 240.0f;

        // Initialisation d'un rectangle représentatif d'un bloc à l'écran

        cell = new Rectangle();
        cell.width = ScreenSize.height / 12.0f;
        cell.height = ScreenSize.height / 12.0f;

        // Lancement de la musique de jeu

        if (playMusics)
        {
            inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KIngame.mp3"));
            inGameMusic.setLooping(true);
            inGameMusic.play();
        }

        // Initialisation du personnage du joueur

        player = new Rectangle();
        player.width = ScreenSize.height / 12.0f;
        player.height = ScreenSize.height / 10.0f;
        player.x = ScreenSize.height / 12.0f;
        player.y = cell.height;

        // Initialisation des variables utiles en jeu

        reversing = false;
        reverseIncrement = -ScreenSize.height / 72.0f - mapMoveSpeed;
        noJoke = true;

        boostActivated = false;
        voidActivated = false;
    }

    @Override
    public void show() {
    }

    // Méthode de rendu appelée un certains nombre de fois par seconde

    @Override
    public void render(float delta) {
        // Effacement du fond en noir

        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);

        // Si l'écran est touché, le renversement du personnage (chute) s'opère

        if (Gdx.input.justTouched()) {
            reversing = true;
            if (reverseIncrement > 0) reverseIncrement = -(ScreenSize.height / 72.0f + mapMoveSpeed);
            else reverseIncrement = ScreenSize.height / 72.0f + mapMoveSpeed;
        }

        // Gestion du mouvement des maps

        mapPosition += mapMoveSpeed;
        if (mapPosition >= firstMap.GetWidth() * cell.width)
        {
            mapPosition = 0.0f;
            firstMap = secondMap;
            secondMap = (Level)levelsMap.values().toArray()[randomGenerator.nextInt(levelsMap.values().toArray().length)];
        }

        // Apllication de la gravité au personnage

        if (reversing) reversing = applyGravity();

        // Augmentation du score et de la vitesse de défilement chaque seconde

        if (TimeUtils.millis() - scoreLastSecond > 1000) increaseScoreAndSpeed();

        // Début de la gestion de l'affichage

        game.batch.begin();

        // Affichage des maps actuelles

        try {
            drawMap(firstMap, 0.0f - mapPosition);
            drawMap(secondMap, firstMap.GetWidth() * cell.width - mapPosition);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Affichage des textes du score et du nombre de FPS

        game.font.draw(game.batch, "Score : " + score, ScreenSize.width * 0.05f, ScreenSize.height * 0.98f);
        if (showFPS) game.font.draw(game.batch, "FPS : " + Gdx.graphics.getFramesPerSecond(), ScreenSize.width * 0.85f, ScreenSize.height * 0.98f);

        // Affichage du personnage

        if (reverseIncrement < 0.0f) game.batch.draw(textureMap.get("Player"), player.x, player.y, player.width, player.height);
        else game.batch.draw(textureMap.get("Player-reverse"), player.x, player.y, player.width, player.height);

        // Fin de la gestion de l'affichage

        game.batch.end();
    }

    // En cas de redimensionnement de l'écran, on adapte ses informations de largeur et hauteur

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

    // En fin de partie, on nettoie les données créées dynamiquement

    @Override
    public void dispose() {
        for (Map.Entry<String, Texture> texture: textureMap.entrySet())
        {
            if (texture.getKey() != null && texture.getValue() != null) texture.getValue().dispose();
        }

        if (playMusics)
        {
            inGameMusic.stop();
            inGameMusic.dispose();
        }

        //game.dispose();
    }

    // Méthode d'augmentation du score et de la vitesse de défilement

    protected void increaseScoreAndSpeed() {
        score++;
        if (boostActivated) score++; // Si le boost est activé, le score augment deux par deux
        mapMoveSpeed += ScreenSize.height / 1480.0f;
        scoreLastSecond = TimeUtils.millis();
    }

    // Méthode d'application de la gravité sur le personnage

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

    // Méthode de renversement instantané du personnage (similaire à applyGravity, mais sans incrémentation progressive)

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

    // Méthode d'affichage d'une map

    protected void drawMap(Level level, Float offset) throws IOException {
        int block;

        // Pour tout les blocs constituant cette map

        for (int y = 0; y < level.GetHeight(); y++)
        {
            for (int x = 0; x < level.GetWidth(); x++)
            {
                block = level.GetCell(y*level.GetWidth() + x); // Acquisition de la valeur du bloc
                if (block != 0)
                {
                    // Établissement des coordonnées du bloc à l'écran

                    cell.x = x * ScreenSize.height / 12.0f + offset;
                    cell.y = ScreenSize.height - cell.height - (y * ScreenSize.height / 12.0f);

                    // Selon le type de bloc, un traitement est effectué

                    if (player.overlaps(cell))
                    {
                        if (block == 1) // Cas du mur
                        {
                            if (score > highScore) highScore = score;

                            if (playMusics) inGameMusic.stop();
                            Intent gameActivityIntent = new Intent(AndroidLauncher.getAppContext(), EndGameActivity.class);
                            gameActivityIntent.putExtra("Score", score);
                            gameActivityIntent.putExtra("HighScore", highScore);

                            dispose();
                            ContextCompat.startActivity(AndroidLauncher.getAppContext(), gameActivityIntent, null);
                        }
                        else if (block == 2) // Cas de la pièce
                        {
                            if (!boostActivated)
                            {
                                if (mapMoveSpeed > 0.0f) mapMoveSpeed -= ScreenSize.height / 740.0f;
                                mapPosition += cell.x;
                            }
                        }
                        else if (block == 3) applyReverse(); // Cas du renversement instantané
                        else if (block == 4) // Cas de l'accélérateur
                        {
                            mapMoveSpeed += ScreenSize.height / 1480.0f;
                            mapPosition += cell.x;
                        }
                        else if (block == 5) // Cas du bloc de vide (changement de toutes les textures)
                        {
                            applyReverse();
                            voidActivated = !voidActivated;
                        }
                        else if (block == 6) // Cas du boost
                        {
                            if (reversing) applyReverse();
                            applyReverse();
                            if (!boostActivated) mapMoveSpeed += (ScreenSize.height / 1480.0f);
                            else mapMoveSpeed -= ScreenSize.height / 740.0f;
                            boostActivated = !boostActivated;
                        }
                        else if (block == 7) // Cas de Benjamin
                        {
                            mapMoveSpeed = 0.0f;
                            mapPosition += cell.x;
                        }
                    }

                    // Affichage de la texture du bloc selon son type

                    if (voidActivated) // Si le bloc de vide est activé, il n'y a que deux textures possibles
                    {
                        if (block == 5) game.batch.draw(textureMap.get("VoidDisable"), cell.x, cell.y, cell.width, cell.height);
                        else game.batch.draw(textureMap.get("Void"), cell.x, cell.y, cell.width, cell.height);
                    }
                    else // Sinon on applique la bonne texture
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
        if (mapMoveSpeed < 0.0f) mapMoveSpeed = 0.0f; // Si la vitesse de défilement devient négative, on l'annule
    }
}
