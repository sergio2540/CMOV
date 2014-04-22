package my.game.achmed.Activities;

import my.game.achmed.ABEngine;
import my.game.achmed.ABMusic;
import my.game.achmed.R;
import my.game.achmed.R.id;
import my.game.achmed.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ABMainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ab_main_menu);

		TextView textView_single;
		TextView textView_multiplayer;
		TextView textView_settings;
		TextView textView_about;
		final TextView textView_exit;

		Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

		textView_single =(TextView)findViewById(R.id.single);
		textView_multiplayer =(TextView)findViewById(R.id.multiplayer);
		textView_settings = (TextView)findViewById(R.id.settings);
		textView_about = (TextView)findViewById(R.id.about);
		textView_exit =(TextView)findViewById(R.id.exit);


		textView_single.setTypeface(font);
		textView_multiplayer.setTypeface(font);
		textView_settings.setTypeface(font);
		textView_about.setTypeface(font);
		textView_exit.setTypeface(font);

		textView_single.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent levelMenu = new Intent(getApplicationContext(),
						ABLevelMenu.class);
				ABMainMenu.this.startActivity(levelMenu);
				ABMainMenu.this.finish();
				//overridePendingTransition(R.layout.fade_in,R.layout.fade_out);
				// setText() sets the string value of the TextView
				//textView_exit.setText("Clicked");

			}
		});
		
		textView_settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent highscore = new Intent(getApplicationContext(), ABSingleRank.class);
				ABMainMenu.this.startActivity(highscore);
				ABMainMenu.this.finish();
			}
			
		});
		
		textView_about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createSinlePlayer = new Intent(getApplicationContext(), ABCreateSinglePlayer.class);
				ABMainMenu.this.startActivity(createSinlePlayer);
				ABMainMenu.this.finish();
			}
			
		});

		//	WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		//	Display display = wm.getDefaultDisplay();
		//
		//	Point point = new Point();
		//	display.getRealSize(point);
		//
		//	ABEngine.displayWidth = point.x;
		//	ABEngine.displayHeight = point.y;

		ABEngine.musicThread = new Thread() {
			@Override
			public void run(){

				Intent backGroundMusic = new
						Intent(getApplicationContext(), ABMusic.class);
				startService(backGroundMusic);
				ABEngine.context = getApplicationContext();

			}
		};

		ABEngine.musicThread.start();

	}

}
