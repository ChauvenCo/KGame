package com.kgame.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	private static Context context;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		boolean launchGame = getIntent().getBooleanExtra("launchGame", false);

		if (launchGame)
		{
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			config.useAccelerometer = false; // Désactivation de l'accéléromètre
			config.useCompass = false; // Désactivation du compas
			initialize(new KGame(), config);
		}
		else
		{
			Intent launcherActivityIntent = new Intent(this, MainMenuActivity.class);
			ContextCompat.startActivity(this, launcherActivityIntent, null);
			finish();
		}
	}

	public static Context getAppContext() {
		return context;
	}
}
