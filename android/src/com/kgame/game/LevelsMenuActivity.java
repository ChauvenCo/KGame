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


public class LevelsMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels_menu);

        Button level1Button = findViewById(R.id.level1);
        level1Button.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, GameLauncher.class);
            launcherActivityIntent.putExtra("PLAYMODE", 0);
            launcherActivityIntent.putExtra("LEVELPATH", "Levels/KLevel1.json");
            ContextCompat.startActivity(this, launcherActivityIntent, null); // Passage sur l'activité GameLauncher
        });

        Button level2Button = findViewById(R.id.level2);
        level2Button.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, GameLauncher.class);
            launcherActivityIntent.putExtra("PLAYMODE", 0);
            launcherActivityIntent.putExtra("LEVELPATH", "Levels/KLevel2.json");
            ContextCompat.startActivity(this, launcherActivityIntent, null); // Passage sur l'activité GameLauncher
        });

        Button retourButton = findViewById(R.id.retour);
        retourButton.setOnClickListener(v -> {
            finish(); // Arrêt de l'activité LevelsMenuActivity (retour sur MainMenuActivity)
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPref = ((Activity)MainMenuActivity.getAppContext()).getPreferences(Context.MODE_PRIVATE);
        int state = sharedPref.getInt("STATE", 0);
        if (state == 1) finish(); // Arrêt de l'activité MainMenuActivity (arrêt de l'application)
    }
}
