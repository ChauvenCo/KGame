package com.kgame.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

        Button jouerLevelsButton = findViewById(R.id.jouerLevels);
        jouerLevelsButton.setOnClickListener(v -> {
            Intent levelsActivityIntent = new Intent(this, LevelsMenuActivity.class);
            levelsActivityIntent.putExtra("PLAYMODE", 0);
            ContextCompat.startActivity(this, levelsActivityIntent, null); // Passage sur l'activité LevelsMenuActivity
        });

        /*Button jouerEndlessButton = findViewById(R.id.jouerEndless);
        jouerEndlessButton.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, GameLauncher.class);
            launcherActivityIntent.putExtra("PLAYMODE", 1);
            ContextCompat.startActivity(this, launcherActivityIntent, null); // Passage sur l'activité GameLauncher
        });*/

        Button jouerOriginalButton = findViewById(R.id.jouerOriginal);
        jouerOriginalButton.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, GameLauncher.class);
            launcherActivityIntent.putExtra("PLAYMODE", 2);
            ContextCompat.startActivity(this, launcherActivityIntent, null); // Passage sur l'activité GameLauncher
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
        else
        {
            SharedPreferences.Editor editor = this.getPreferences(Context.MODE_PRIVATE).edit();
            editor.putInt("STATE", 0);
            editor.apply();
        }
    }


    public static Context getAppContext() {
        return context;
    }
}
