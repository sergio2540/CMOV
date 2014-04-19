package my.game.achmed.Activities;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.R.layout;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class AchmedBomberActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.layout.fade_in,R.layout.fade_out);

		setContentView(R.layout.splash_screen);

		new Handler().postDelayed(new Thread() {
			@Override
			public void run() {

				Intent mainMenu= new Intent(AchmedBomberActivity.this,
						ABMainMenu.class);
				AchmedBomberActivity.this.startActivity(mainMenu);
				AchmedBomberActivity.this.finish();
				overridePendingTransition(R.layout.fade_in,R.layout.fade_out);

			}
		}, ABEngine.GAME_THREAD_DELAY);

	}

}
