package my.game.achmed;

import android.content.Context;
import android.content.Intent;

import my.game.achmed.R;
import android.view.View;

public class ABEngine {

	public static final int GAME_THREAD_DELAY = 2000;
	public static final int GAME_DURATION = 300000; //5 minutes
	public static final int UPDATE_INTERVAL = 1000; //every second
	
	public static int displayWidth = 0;
	public static int displayHeight = 0;
	
	public static final int SPLASH_SCREEN_MUSIC = R.raw.achmed;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;

	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);

	public static Context context;
	
	public static Thread musicThread;
	public static Thread timeThread;
	
	public static final int GAME_MAP = R.raw.gametiles;
	public static final int GAME_PLAYER = R.raw.bomberman;
	public static final int GAME_BOMB = R.raw.bombs;
	
	public static final int PLAYER_LEFT = 1;
	public static final int PLAYER_UP = 4;
	public static final int PLAYER_RIGHT = 2;
	public static final int PLAYER_DOWN = 5;
	
	public static final int PLAYER_LEFT_RELEASE = 11;
	public static final int PLAYER_UP_RELEASE = 44;
	public static final int PLAYER_RIGHT_RELEASE = 22;
	public static final int PLAYER_DOWN_RELEASE = 55;

	public static float X_POSITION = 0f;
	public static float Y_POSITION = 0f;
	public static final float ACHMED_SPEED = .1f;
	public static final int PLAYER_ACHMED_FRAMES = 0;
	public static final int PLAYER_FRAMES_BETWEEN_ANI = 9;
	
	public static final int NO_BOMB = 0;
	public static final int DROP_BOMB = 1;
	
	public static int PLAYER_ACTION = PLAYER_RIGHT_RELEASE;
	public static int BOMB_ACTION = NO_BOMB;
	
	
	
	public static char[][] game_map = {
		
		{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},
		
		
	};


	
	public boolean onExit(View v) {
		try
		{
			Intent bgmusic = new Intent(context, ABMusic.class);
			context.stopService(bgmusic);

			musicThread.interrupt();
			return true;			

		} catch(Exception e){
			return false;
		}
	
	}

}
