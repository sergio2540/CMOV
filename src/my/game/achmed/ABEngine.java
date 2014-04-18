package my.game.achmed;

import android.content.Context;
import android.content.Intent;

import my.game.achmed.R;
import android.util.Log;
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
	public static final int GAME_FIRE = R.raw.fireball;
	public static final int GAME_ROBOTS = R.raw.robots;

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
	public static final int BOMB_EXPLOSION = 2;

	public static int PLAYER_ACTION = PLAYER_RIGHT_RELEASE;
	public static int BOMB_ACTION = NO_BOMB;
	public static int BOMB_TIME_TO_EXPLOSION = 2000;

	public static int EXPLOSION_RADIUS = 2;

	public static boolean BOMB_DROPPED = false;

	public static float start_x;
	public static float start_y;

	public static boolean FIRST_MAP_DRAW = true;

	public static int MAX_COUNTER = (int) (1f / ABEngine.ACHMED_SPEED);

	public static int ACHMED_COUNTER = 0;

	public static boolean STOP = true;
	public static boolean STOPPED = true;


	//GREEN ROBOT

	public static int GREEN_ROBOT_ACTION = PLAYER_RIGHT_RELEASE;
	public static float GREEN_ROBOT_X = 0f;
	public static float GREEN_ROBOT_Y = 0f;
	public static final float ROBOT_SPEED = .1f;

	//BLUE ROBOT ETC...ETC



	public static char[][] game_map = {

		{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','O','O','O','O','O','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','O','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','O','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','O','O','O','O','O','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','R','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','W','W','W','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','1','-','-','-','W','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','W','W','W','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
		{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},


	};

	public static boolean detectColision(float x, float y) {



		int matrix_x = 0, matrix_y = 0;

		float startX = (ABEngine.start_x/0.05f) - ((float)game_map[0].length/2f);
		float startY = (ABEngine.start_y/0.05f) - ((float)game_map.length/2f);
		
		
		float X = x - startX;
	    float Y = y - startY;
	    
	    Log.w("INDEXES", "xx: " + ((float)game_map.length - 1f - Y) + " yy: " + X);

		switch(ABEngine.PLAYER_ACTION) {

		case ABEngine.PLAYER_UP: 

			matrix_x = Math.round(x - startX);
			
			matrix_y = (int) Math.floor(y - startY);

			break;

		case ABEngine.PLAYER_DOWN: 

			matrix_x = Math.round(x - startX);
			matrix_y = (int) Math.ceil(y - startY);

			break;

		case ABEngine.PLAYER_LEFT:

			matrix_x = Math.round(x - startX);
			matrix_y = (int) Math.floor(y - startY);

			break;

		case ABEngine.PLAYER_RIGHT:

			matrix_x = Math.round(x - startX);
			matrix_y = (int) Math.ceil(y - startY);

			break;

		}

		Log.w("INDEXES", "x: " + (game_map.length - 1 - matrix_y) + " y: " + matrix_x);

		char pos = game_map[game_map.length - 1 - matrix_y][ matrix_x];

		if(pos == '-' || pos == '1') {
			return false;
		} else {
			return true;
		}

	}



	public boolean onExit(View v) {
		try
		{
			Intent bgmusic = new Intent(context, ABMusic.class);
			context.stopService(bgmusic);

			musicThread.interrupt();
			return true;			

		} catch(Exception e) {
			return false;
		}

	}

}
