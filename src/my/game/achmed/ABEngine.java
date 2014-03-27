package my.game.achmed;


import my.game.achmed.R;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ABEngine {


	public static final int GAME_THREAD_DELAY = 4000;

	public static final int SPLASH_SCREEN_MUSIC = R.raw.achmed;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;

	public static Context context;
	public static Thread musicThread;

	public static Thread timeThread;
	public static final int GAME_DURATION = 300000; //5 minutes
	public static final int UPDATE_INTERVAL = 1000; //every second
	

	

	public boolean onExit(View v) {
		try
		{
			Intent bgmusic = new Intent(context, ABMusic.class);
			context.stopService(bgmusic);
			
			//mandar abaixo a thread
			
			return true;
		} catch(Exception e){
			return false;
		}
	}

}
