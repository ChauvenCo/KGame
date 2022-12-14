package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class EndGameActivity extends AppCompatActivity {
    Integer score, highScore, gameMode;
    Music menuMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game);

        SharedPreferences sharedPref = ((Activity)MainMenuActivity.getAppContext()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KMenu.mp3"));
        if (sharedPref.getBoolean("PLAYMUSICS", true))
        {
            menuMusic.setLooping(true);
            menuMusic.play();
        }

        gameMode = sharedPref.getInt("PLAYMODE", 2);
        if (gameMode == 0)
        {
            TextView scoreLabel = findViewById(R.id.scoreLabel);
            scoreLabel.setText(R.string.LevelEndLabel);

            findViewById(R.id.score).setVisibility(View.INVISIBLE);
            findViewById(R.id.highscoreLabel).setVisibility(View.INVISIBLE);
            findViewById(R.id.highscore).setVisibility(View.INVISIBLE);
        }
        /*else if (gameMode == 1)
        {

        }*/
        else if (gameMode == 2)
        {
            score = getIntent().getIntExtra("Score", 0);
            highScore = getIntent().getIntExtra("HighScore", 0);

            int savedHighScore = sharedPref.getInt("HIGHSCORE", 0);

            if (savedHighScore < highScore)
            {
                editor.putInt("HIGHSCORE", highScore);
                editor.apply();
            }
            else highScore = savedHighScore;

            TextView scoreLabel = findViewById(R.id.score);
            scoreLabel.setText(String.valueOf(score));
            TextView highscoreLabel = findViewById(R.id.highscore);
            highscoreLabel.setText(String.valueOf(highScore));
        }

        Button rejouerButton = findViewById(R.id.rejouer);
        rejouerButton.setOnClickListener(v -> {
            editor.putInt("STATE", 0);
            editor.apply();

            finish(); // Arr??t de l'activit?? EndGameActivit?? (retour sur GameLauncher)
        });

        Button quitterButton = findViewById(R.id.quitter);
        quitterButton.setOnClickListener(v -> {
            editor.putInt("STATE", 1);
            editor.apply();

            finish(); // Arr??t de l'activit?? EndGameActivit?? (retour sur GameLauncher)
        });

        Button retourButton = findViewById(R.id.retour);
        retourButton.setOnClickListener(v -> {
            editor.putInt("STATE", 2);
            editor.apply();

            finish(); // Arr??t de l'activit?? EndGameActivit?? (retour sur GameLauncher)
        });

        Button optionsButton = findViewById(R.id.options);
        optionsButton.setOnClickListener(v -> {
            Intent optionsActivityIntent = new Intent(this, OptionsActivity.class);
            ContextCompat.startActivity(this, optionsActivityIntent, null);
        });
    }

    @Override
    protected void onStop() {
        menuMusic.stop();
        menuMusic.dispose();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        menuMusic.stop();
        menuMusic.dispose();
        super.onDestroy();
    }
}
