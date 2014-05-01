package my.game.achmed.Activities;

import my.game.achmed.ABEngine;
import my.game.achmed.ABMusic;
import my.game.achmed.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ABMainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.ab_main_menu);

	initGameMusic();

	TextView textView_single;
	TextView textView_multiplayer;
	TextView textView_settings;
	TextView textView_about;
	final TextView textView_exit;

	Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

	textView_single =(TextView)findViewById(R.id.single);
	textView_multiplayer =(TextView)findViewById(R.id.multiplayer);
	textView_settings = (TextView)findViewById(R.id.highscore);
	textView_about = (TextView)findViewById(R.id.about);
	textView_exit =(TextView)findViewById(R.id.exit);


	textView_single.setTypeface(font);
	textView_multiplayer.setTypeface(font);
	textView_settings.setTypeface(font);
	textView_about.setTypeface(font);
	textView_exit.setTypeface(font);

	textView_single.setTextColor(Color.WHITE);
	textView_multiplayer.setTextColor(Color.WHITE);
	textView_settings.setTextColor(Color.WHITE);
	textView_about.setTextColor(Color.WHITE);
	textView_exit.setTextColor(Color.RED);

	textView_single.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {

		Intent levelMenu = new Intent(getApplicationContext(),
			ABLevelMenu.class);

		ABEngine.isOnMultiplayer = false;

		ABMainMenu.this.startActivity(levelMenu);
		ABMainMenu.this.finish();
		//overridePendingTransition(R.layout.fade_in,R.layout.fade_out);
		//setText() sets the string value of the TextView
		//textView_exit.setText("Clicked");

	    }
	});

	textView_multiplayer.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {

		Intent levelMenu = new Intent(getApplicationContext(),
			ABLevelMenu.class);

		ABEngine.isOnMultiplayer = true;

		ABMainMenu.this.startActivity(levelMenu);
		ABMainMenu.this.finish();
		//overridePendingTransition(R.layout.fade_in,R.layout.fade_out);
		//setText() sets the string value of the TextView
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

	ABEngine.context = getApplicationContext();

	final ImageButton sound = (ImageButton) findViewById(R.id.soundon);

	sound.setOnTouchListener(new View.OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

		if(event.getAction() == (MotionEvent.ACTION_UP)){
		    if(ABEngine.GAME_MUSIC_SOUND) {
			stopGameMusic();
			sound.setImageResource(R.raw.soundoff);
			ABEngine.GAME_MUSIC_SOUND = false;
		    } else {
			startGameMusic();
			sound.setImageResource(R.raw.soundon);
			ABEngine.GAME_MUSIC_SOUND = true;
		    }
		}
		return true;
	    }
	});

	textView_exit.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		stopGameMusic();
		ABMainMenu.this.finish();
	    }

	});

    }


    public void initGameMusic(){
	
	final ImageButton sound = (ImageButton) findViewById(R.id.soundon);
	if(ABEngine.GAME_MUSIC_SOUND){
	    startGameMusic();
	    sound.setImageResource(R.raw.soundon);
	}else {
	    sound.setImageResource(R.raw.soundoff);
	}
    }

    public void startGameMusic() {

	Intent backMusic = new Intent(getApplicationContext(), ABMusic.class);
	startService(backMusic);

    }

    public void stopGameMusic() {
	Intent backMusic = new Intent(getApplicationContext(), ABMusic.class);
	stopService(backMusic);
    }

}
