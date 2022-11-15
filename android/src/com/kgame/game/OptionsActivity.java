package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class OptionsActivity extends AppCompatActivity {
    Integer caller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        caller = getIntent().getIntExtra("Caller", 0);

        SharedPreferences sharedPref = ((Activity)AndroidLauncher.getAppContext()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        CheckBox playMusicsCheckBox = findViewById(R.id.playMusics);
        playMusicsCheckBox.setChecked(sharedPref.getBoolean("PLAYMUSICS", true));
        playMusicsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("PLAYMUSICS", isChecked);
            editor.apply();
        });

        CheckBox showFPSCheckBox = findViewById(R.id.showFPS);
        showFPSCheckBox.setChecked(sharedPref.getBoolean("SHOWFPS", false));
        showFPSCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("SHOWFPS", isChecked);
            editor.apply();
        });

        Button retourButton = findViewById(R.id.retour);
        retourButton.setOnClickListener(v -> {
            Intent mainMenuActivityIntent = new Intent(this, MainMenuActivity.class);
            if (caller == 1) mainMenuActivityIntent = new Intent(this, EndGameActivity.class);
            finish();
            ContextCompat.startActivity(this, mainMenuActivityIntent, null);
        });
    }
}
