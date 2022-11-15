package com.kgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	KGame game = new KGame();

	@Override
	protected void onStart() {
		super.onStart();

		int state = ((Activity)MainMenuActivity.getAppContext()).getPreferences(Context.MODE_PRIVATE).getInt("STATE", 0);

		if (state == 0)
		{
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			config.useAccelerometer = false; // Désactivation de l'accéléromètre
			config.useCompass = false; // Désactivation du compas

			initialize(game, config); // Passage sur l'écran de jeu
		}
		else if (state == 1) finish(); // Arrêt de l'activité AndroidLauncher (retour sur MainMenuActivity)
	}

	@Override
	protected void onPause() {
		super.onPause();
		game.dispose();
	}
}
