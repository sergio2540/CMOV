package my.game.achmed.Activities;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.R.layout;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AchmedBomberActivity extends Activity {

	private Handler handler;
	private Runnable delayRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		ABEngine.context = getApplicationContext();
		
		handler = new Handler();
		delayRunnable = new Thread() {
			@Override
			public void run() {

				Intent createSP = new Intent(AchmedBomberActivity.this,
						ABCreateSinglePlayer.class);
				AchmedBomberActivity.this.startActivity(createSP);
				overridePendingTransition(R.layout.fade_in,R.layout.fade_out);
				AchmedBomberActivity.this.finish();

			}
		};

		handler.postDelayed(delayRunnable, 3000);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(delayRunnable);
	}



}
