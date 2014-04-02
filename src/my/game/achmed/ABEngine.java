package my.game.achmed;

import android.content.Context;
import android.content.Intent;

import my.game.achmed.R;
import android.view.View;

public class ABEngine {


	public static final int GAME_THREAD_DELAY = 2000;

	public static final int SPLASH_SCREEN_MUSIC = R.raw.achmed;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;

	public static final int BACKGROUND_LAYER_ONE = R.raw.abbackground;
	public static float SCROLL_BACKGROUND_ONE = .001f;

	public static float SCROLL_BACKGROUND_TWO = .002f;
	public static final int BACKGROUND_LAYER_TWO = R.raw.abbackground2;

	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);

	public static Context context;
	public static Thread musicThread;

	public static Thread timeThread;

	public static int playerFlightAction = 0;
	
	public static final int PLAYER_SHIP = R.raw.spaceship;
	
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
	
	public static int PLAYER_ACTION = PLAYER_RIGHT_RELEASE;
	
	public static final int PLAYER_RELEASE = 3;
	
	public static final int PLAYER_BANK_LEFT_1 = 1;
	public static final int PLAYER_BANK_RIGHT_1 = 4;
	public static final int PLAYER_FRAMES_BETWEEN_ANI = 9;
	
	
	public static float playerBankPosX = 1.75f;

	public static final float PLAYER_BANK_SPEED = .1f;
	
	
	public static final float ACHMED_SPEED = .1f;
	
	public static float X_POSITION = 0f;
	public static float Y_POSITION = 0f;
	
	public static final int PLAYER_ACHMED_FRAMES = 0;
	
	public static int BOMB_ACTION = 0;
	public static final int NO_BOMB = 0;
	public static final int DROP_BOMB = 1;
	

	public static int displayHeight;
	public static int displayWidth;

	//game map
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

	// TODO nao sei se e o melhor sitio para inicializar o display



	public static void initDisplaySize() {


	}

	public static void initABEngine() {

		//		initDisplaySize();

	}

	public static final int GAME_DURATION = 300000; //5 minutes
	public static final int UPDATE_INTERVAL = 1000; //every second

	

	
	
	
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
