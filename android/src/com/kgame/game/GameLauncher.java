package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameLauncher extends AndroidApplication {
	KGame game = new KGame();

	@Override
	protected void onStart() {
		super.onStart();

		SharedPreferences sharedPref = ((Activity)MainMenuActivity.getAppContext()).getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		int state = sharedPref.getInt("STATE", 0);

		editor.putInt("PLAYMODE", getIntent().getIntExtra("PLAYMODE", 0));
		editor.putString("LEVELPATH", getIntent().getStringExtra("LEVELPATH"));
		editor.apply();

		if (state == 0)
		{
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			config.useAccelerometer = false; // Désactivation de l'accéléromètre
			config.useCompass = false; // Désactivation du compas

			initialize(game, config); // Passage sur l'écran de jeu
		}
		else if (state == 1 || state == 2) finish(); // Arrêt de l'activité GameLauncher (retour sur MainMenuActivity ou LevelsMenuActivity)
	}

	@Override
	protected void onPause() {
		super.onPause();
		game.dispose();
	}
}
