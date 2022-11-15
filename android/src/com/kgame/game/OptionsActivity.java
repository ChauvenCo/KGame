package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        SharedPreferences sharedPref = ((Activity)MainMenuActivity.getAppContext()).getPreferences(Context.MODE_PRIVATE);
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
            finish(); // Arrêt de l'activité OptionsActivity (retour sur MainMenuActivity ou EndGameActivity)
        });
    }
}
