package com.kgame.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class EndGameActivity extends AppCompatActivity {
    Integer score, highScore;
    Music menuMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game);


        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/KMenu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.play();

        score = getIntent().getIntExtra("Score", 0);
        highScore = getIntent().getIntExtra("HighScore", 0);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

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

        Button rejouerButton = findViewById(R.id.rejouer);
        rejouerButton.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, AndroidLauncher.class);
            launcherActivityIntent.putExtra("launchGame", true);
            ContextCompat.startActivity(this, launcherActivityIntent, null);
        });

        Button quitterButton = findViewById(R.id.quitter);
        quitterButton.setOnClickListener(v -> {
            menuMusic.stop();
            menuMusic.dispose();
            finish();
            System.exit(0);
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
