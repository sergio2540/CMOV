package my.game.achmed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;

import my.game.achmed.R;
import my.game.achmed.Activities.ABGame;
import my.game.achmed.Characters.BOMB_ACTION;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;
import my.game.achmed.Events.LoadingEvent;
import my.game.achmed.Multiplayer.OutgoingCommTask;
import my.game.achmed.Multiplayer.ReceiveCommTask;
import android.view.View;
import android.widget.TextView;

public class ABEngine {

    public static LoadingEvent loadingEvent = new LoadingEvent();
    
    public static final int GAME_THREAD_DELAY = 2000;

    public static int displayWidth = 0;
    public static int displayHeight = 0;

    public static String PLAYER_NICK = "";
    public static boolean GAME_MUSIC_SOUND = true;
    public static final int SPLASH_SCREEN_MUSIC = R.raw.achmed;
    public static final int R_VOLUME = 100;
    public static final int L_VOLUME = 100;
    public static final boolean LOOP_BACKGROUND_MUSIC = true;

    public static int GAME_FPS = 60;
    public static int GAME_THREAD_FPS_SLEEP = (1000/GAME_FPS);

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

    public static boolean isOnMultiplayer = false;


    //public static int BOMB_TIME_TO_EXPLOSION = 4000;

    //public static int EXPLOSION_RADIUS = 1;

    //public static boolean BOMB_DROPPED = false;

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

    //Start do jogo
    public static float START_X;
    public static float START_Y;

    public static ABGame GAME;
    public static Level LEVEL;
    public static int NUM_LEVEL;

    public static char[][] MAP = {{'W','-','-','O','-','W'}, 
	{'W','-','-','O','-','W'},
	{'W','-','-','O','-','W'},
	{'W','-','-','O','-','W'},
	{'W','1','-','O','-','W'},
	{'W','-','-','O','R','W'},
	{'W','-','-','O','-','W'}};

    public static Player createRandomPlayer() {

	//char[][] matrix  = MAP;
	Random random = new Random();
	int x = 0, y = 0;
	boolean success = false;

	while(!success) {


	    x = random.nextInt(ABEngine.getMaxX());
	    y = random.nextInt(ABEngine.getMaxY());

	    if(getObject(x,y) == '-') {
		success = true;
	    }
	}

	Player player  = Player.create(x * 100, y * 100);
	
	return player;

    }

    public static synchronized void setObject(int x, int y, char object){
	if(getObject(x,y) != 'W'){
	    MAP[MAP.length - 1 - y][x] = object;
	}
    }

    public static synchronized boolean isNotInMatrixRange(int mtx_x, int mtx_y){
	return !isInMatrixRange(mtx_x, mtx_y);
    }

    private static synchronized boolean isInMatrixRange(int mtx_x, int mtx_y){
	return mtx_x > 0 && mtx_x < getMaxX() && mtx_y > 0  &&  mtx_y < getMaxY();
    }

    public static synchronized char getObject(int mtx_x, int mtx_y){
	return MAP[MAP.length - 1 - mtx_y][mtx_x];
    }

    public static int getMaxX(){
	return MAP[0].length;
    }

    public static int getMaxY(){
	return MAP.length;
    }


    public static synchronized int getXMatrixPosition(float x){
	return (int) (x/100.f);
    }

    public static synchronized int getYMatrixPosition(float y){
	return (int) (y/100.f);
    }

    public static synchronized int getXMatrixPosition(float x, CHARACTER_ACTION action){

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
	    matrix_x = (int) x;
	    break;

	}

	return matrix_x; 
    }	 

    public static synchronized int getYMatrixPosition(float y, CHARACTER_ACTION character){

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
	    matrix_y = (int) y;
	    break;
	}


	return matrix_y;
    }	 

    public static synchronized boolean detectColision(float x, float y, CHARACTER_ACTION action) {


	int mtx_x = getXMatrixPosition(x,action);
	int mtx_y = getYMatrixPosition(y,action);

	char pos = getObject(mtx_x,mtx_y);

	if(pos == '-') {
	    return false;
	} else {
	    return true;
	}

    }


    public static synchronized void updateScore(final float points){

	GAME.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {

		String pts = points + "Points";
		TextView t = (TextView) GAME.findViewById(R.id.score);    
		t.setText(pts);
	    }
	});
    }

    public static synchronized void endGame(final boolean won) {

	GAME.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		if(won) {
		    GAME.wonOrLost(true);
		} else {
		    GAME.wonOrLost(false);
		}
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

    public static void create_map(char[][] gameLevelMatrix) {

	char[][] tempMap  = new char[gameLevelMatrix.length][gameLevelMatrix[0].length];

	for(int i = 0; i < gameLevelMatrix.length; i++) {

	    for(int j = 0; j < gameLevelMatrix[i].length; j++) {

		tempMap[i][j] = gameLevelMatrix[i][j];
	    }
	}

	MAP = tempMap;
	FIRST_MAP_DRAW = true;
    }


    //return the player
    public static java.lang.Character newPlayer(char id, int x, int y, char[][] gameLevelMatrix){

	Player p;
	if(!ABEngine.PLAYERS.containsKey(id)){
	    p = Player.create(id, x, y);
	    ABEngine.PLAYERS.put(id, p);
	} else {
	    p = Player.create(x, y);
	    ABEngine.PLAYERS.put(id, p);
	}

	create_map(gameLevelMatrix);

	//Rever retorno!!!
	if(p == null)
	    return null;

	return p.getID();

    }

    public static void setPlayerAction(char id, CHARACTER_ACTION action){

	ABEngine.PLAYERS.get(id).setAction(action);

    }

    public static void setBombAction(char id, BOMB_ACTION action){

	ABEngine.PLAYERS.get(id).getBomb().setAction(action);

    }
    
    
    
    public static LoadingEvent getLoadingEvent() {
	return ABEngine.loadingEvent;
    }

    public static void sendPlayerAction(char playerId, CHARACTER_ACTION ca) {
	ReceiveCommTask.sendPlayerAction(ca, playerId);
    }
    
}
