package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;

public class MainMenuActivity extends AppCompatActivity {
    private static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        context = this;

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("STATE", 0);
        editor.apply();

        Button jouerButton = findViewById(R.id.jouer);
        jouerButton.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, AndroidLauncher.class);
            launcherActivityIntent.putExtra("launchGame", true);
            ContextCompat.startActivity(this, launcherActivityIntent, null); // Passage sur l'activité AndroidLauncher
        });

        Button optionsButton = findViewById(R.id.options);
        optionsButton.setOnClickListener(v -> {
            Intent optionsActivityIntent = new Intent(this, OptionsActivity.class);
            ContextCompat.startActivity(this, optionsActivityIntent, null); // Passage sur l'activité OptionsActivity
        });

        Button quitterButton = findViewById(R.id.quitter);
        quitterButton.setOnClickListener(v -> {
            finish(); // Arrêt de l'activité MainMenuActivity (arrêt de l'application)
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        int state = this.getPreferences(Context.MODE_PRIVATE).getInt("STATE", 0);
        if (state == 1) finish(); // Arrêt de l'activité MainMenuActivity (arrêt de l'application)
    }

    public static Context getAppContext() {
        return context;
    }
}
