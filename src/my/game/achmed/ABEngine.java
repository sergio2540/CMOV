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
	public static final float ACHMED_SPEED = 10f;
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

	public static int MAX_COUNTER = (int) (100f / ABEngine.ACHMED_SPEED);

	public static int ACHMED_COUNTER = 0;
	
	public static int ROBOT_COUNTER = 0;

	public static boolean STOP = true;
	public static boolean STOPPED = true;


	//GREEN ROBOT

	public static int GREEN_ROBOT_ACTION = PLAYER_RIGHT;
	public static float GREEN_ROBOT_X = 0f;
	public static float GREEN_ROBOT_Y = 0f;
	public static final float ROBOT_SPEED = 10f;

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
	
	//Start do jogo
	public static float START_X;
	public static float START_Y;
		 

	public static boolean detectColision(float x, float y, int character) {

		int matrix_x = 0, matrix_y = 0;
		
		x = x/100;
		y = y/100;
		
		Log.w("xy", "x" + x + "y" + y);
		
		

		switch(character) {

		case ABEngine.PLAYER_UP: 

		    matrix_x = Math.round(x);
		    matrix_y = (int) Math.ceil(y);

		    break;

		case ABEngine.PLAYER_DOWN: 

		    matrix_x = Math.round(x);
		    matrix_y = (int) Math.floor(y);

		    break;

		case ABEngine.PLAYER_LEFT:

		    matrix_x = (int) Math.floor(x);
		    matrix_y = Math.round(y);

		    break;

		case ABEngine.PLAYER_RIGHT:

		    matrix_x = (int) Math.ceil(x);
		    matrix_y = Math.round(y);

		    break;

		}

		char pos = game_map[game_map.length - 1 - matrix_y][matrix_x];
		
		if(pos == '-' || pos == '1' || pos == 'R') {
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
