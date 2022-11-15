package com.kgame.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button jouerButton = findViewById(R.id.jouer);
        jouerButton.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, AndroidLauncher.class);
            launcherActivityIntent.putExtra("launchGame", true);
            finish();
            ContextCompat.startActivity(this, launcherActivityIntent, null);
        });

        Button optionsButton = findViewById(R.id.options);
        optionsButton.setOnClickListener(v -> {
            Intent optionsActivityIntent = new Intent(this, OptionsActivity.class);
            optionsActivityIntent.putExtra("Caller", 0);
            finish();
            ContextCompat.startActivity(this, optionsActivityIntent, null);
        });

        Button quitterButton = findViewById(R.id.quitter);
        quitterButton.setOnClickListener(v -> {
            ((Activity)AndroidLauncher.getAppContext()).finish();
            finish();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
