package com.kgame.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button jouerButton = findViewById(R.id.jouer);
        jouerButton.setOnClickListener(v -> {
            Intent launcherActivityIntent = new Intent(this, AndroidLauncher.class);
            launcherActivityIntent.putExtra("launchGame", true);
            ContextCompat.startActivity(this, launcherActivityIntent, null);
        });

        Button quitterButton = findViewById(R.id.quitter);
        quitterButton.setOnClickListener(v -> {
            finish();
            System.exit(0);
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
