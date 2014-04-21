package my.game.achmed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;

import my.game.achmed.R;
import my.game.achmed.Activities.ABGame;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;
import android.view.View;
import android.widget.TextView;

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

    public static final int PLAYER_ACHMED_FRAMES = 0;
    public static final int PLAYER_FRAMES_BETWEEN_ANI = 9;

    public static final int NO_BOMB = 0;
    public static final int DROP_BOMB = 1;
    public static final int BOMB_EXPLOSION = 2;

    public static int BOMB_ACTION = NO_BOMB;
    public static int BOMB_TIME_TO_EXPLOSION = 4000;

    public static int EXPLOSION_RADIUS = 1;

    public static boolean BOMB_DROPPED = false;

    public static float start_x;
    public static float start_y;

    public static boolean FIRST_MAP_DRAW = true;

    public static int MAX_COUNTER = (int) (100f / 10f);

    public static boolean STOP = true;
    public static boolean STOPPED = true;


    //State
    public static Player PLAYER;
    public static Map<Character,Player> PLAYERS = new TreeMap<Character,Player>();
    public static List<Robot> ROBOTS = new ArrayList<Robot>();


    public static char[][] game_map = {

	{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},
	{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
	{'W','-','-','O','-','-','-','-','-','-','O','-','R','-','-','-','-','R','-','W'},
	{'W','2','O','O','O','-','O','O','-','-','-','-','-','-','-','-','-','-','-','W'},
	{'W','-','-','-','-','-','-','-','-','-','-','-','-','O','-','-','-','-','-','W'},
	{'W','-','-','O','-','R','-','-','O','-','-','-','-','-','-','-','-','-','-','W'},
	{'W','-','-','-','O','-','-','-','-','-','-','-','-','-','-','-','O','-','-','W'},
	{'W','-','-','-','-','-','-','-','R','-','-','O','-','-','O','O','O','O','-','W'},
	{'W','O','O','O','-','O','O','O','O','O','-','-','-','-','-','-','O','-','-','W'},
	{'W','-','O','-','-','R','-','-','-','O','-','-','O','O','-','-','-','-','-','W'},
	{'W','-','-','-','-','-','-','-','-','O','-','-','3','-','-','O','O','-','-','W'},
	{'W','-','-','-','-','O','O','O','O','O','-','R','-','-','O','-','-','-','-','W'},
	{'W','-','-','-','-','-','-','-','-','-','-','-','O','-','O','-','R','-','-','W'},
	{'W','-','-','O','-','-','-','-','-','-','O','-','-','-','O','-','-','-','-','W'},
	{'W','-','-','-','-','-','O','O','O','-','-','-','-','-','O','-','O','-','-','W'},
	{'W','O','O','-','1','-','-','-','O','R','-','-','O','-','O','-','-','-','-','W'},
	{'W','-','-','-','-','-','O','O','O','-','O','-','O','O','O','O','O','-','-','W'},
	{'W','R','-','O','-','-','-','-','-','-','-','-','-','-','-','-','O','-','-','W'},
	{'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
	{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},


    };

    //Start do jogo
    public static float START_X;
    public static float START_Y;
    
    public static ABGame GAME;

    public static void setObject(int x, int y, char object){
	game_map[game_map.length - 1 - y][x] = object;
    }

    public static boolean isNotInMatrixRange(int mtx_x, int mtx_y){
	return !isInMatrixRange(mtx_x, mtx_y);
    }

    private static boolean isInMatrixRange(int mtx_x, int mtx_y){
	return mtx_x > 0 && mtx_x < getMaxX() && mtx_y > 0  &&  mtx_y < getMaxY();
    }

    public static char getObject(int mtx_x, int mtx_y){
	return game_map[game_map.length - 1 - mtx_y][mtx_x];
    }

    private static int getMaxX(){
	return game_map[0].length;
    }

    private static int getMaxY(){
	return game_map.length;
    }


    public static int getXMatrixPosition(float x){
	return (int) (x/100.f);
    }

    public static int getYMatrixPosition(float y){
	return (int) (y/100.f);
    }

    public static int getXMatrixPosition(float x, CHARACTER_ACTION action){

	int matrix_x = 0;

	x = x/100;

	switch(action) {

	case UP: 

	    matrix_x = Math.round(x);

	    break;

	case DOWN: 

	    matrix_x = Math.round(x);

	    break;

	case LEFT:

	    matrix_x = (int) Math.floor(x);

	    break;

	case RIGHT:

	    matrix_x = (int) Math.ceil(x);

	    break;

	default:
	    break;

	}

	return matrix_x; 
    }	 

    public static int getYMatrixPosition(float y, CHARACTER_ACTION character){

	int matrix_y = 0;

	y = y/100;

	switch(character) {

	case UP: 

	    matrix_y = (int) Math.ceil(y);

	    break;

	case DOWN: 

	    matrix_y = (int) Math.floor(y);

	    break;

	case LEFT:

	    matrix_y = Math.round(y);

	    break;

	case RIGHT:


	    matrix_y = Math.round(y);

	    break;

	default:
	    break;
	}


	return matrix_y;
    }	 

    public static boolean detectColision(float x, float y, CHARACTER_ACTION action) {


	int mtx_x = getXMatrixPosition(x,action);
	int mtx_y = getYMatrixPosition(y,action);

	char pos = getObject(mtx_x,mtx_y);

	if(pos == '-') {
	    return false;
	} else {
	    return true;
	}

    }
    
    
    public static void updateScore(final float points){
	
	GAME.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		
		String pts = points + "Points";
		TextView t = (TextView) GAME.findViewById(R.id.score);    
		t.setText(pts);
	    }
	});
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
