package my.game.achmed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import my.game.achmed.Multiplayer.p2p.WiFiDirectBroadcastReceiver;
import my.game.achmed.State.BombState;
import my.game.achmed.State.Event;
import my.game.achmed.State.InitState;
import my.game.achmed.State.LeaveState;
import my.game.achmed.State.PlayerState;
import my.game.achmed.State.State;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
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

    public static int GAME_PAUSED = 99;

    public static final int GAME_MAP = R.raw.gametiles;
    public static final int GAME_PLAYER = R.raw.bomberman;
    public static final int GAME_BOMB = R.raw.bombs;
    public static final int GAME_FIRE = R.raw.fireball;
    public static final int GAME_ROBOTS = R.raw.robots;

    public static final int PLAYER_ACHMED_FRAMES = 0;
    public static final int PLAYER_FRAMES_BETWEEN_ANI = 9;

    //private static final int GO_PORT = 9091;

    public static boolean isOnMultiplayer = false;


    //public static int BOMB_TIME_TO_EXPLOSION = 4000;

    //public static int EXPLOSION_RADIUS = 1;

    //public static boolean BOMB_DROPPED = false;

    public static float start_x;
    public static float start_y;

    public static boolean FIRST_MAP_DRAW = true;

    public static int MAX_COUNTER = (int) (100f / 10f);

    public static float MILLIS_UNTIL_FINISHED = 0;

    
    //MULTIPLATER
    public static Socket GO_SOCKET;
    public static List<Socket> PEERS = new ArrayList<Socket>();
    
    public static List<ObjectOutputStream> PEERSSTREAMS = new ArrayList<ObjectOutputStream>();
    
    public static WifiP2pManager mManager;

	public static Channel mChannel;

    //pulic static boolean STOP = true;
    //public static boolean STOPPED = true;


    //State
    public static Player PLAYER;
    public static Map<Character,Player> ENEMIES = new HashMap<Character,Player>();

    public static Map<Character,Player> AVAILABLE_PLAYERS = new HashMap<Character,Player>();

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

    public synchronized static Player createRandomPlayer() {

	Player player = null;

	for(char id : AVAILABLE_PLAYERS.keySet()){
	    player = AVAILABLE_PLAYERS.get(id);

	    if(player != null)
		break;
	}

	if(player != null)
	    AVAILABLE_PLAYERS.remove(player.getID());

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

    public synchronized static void create_map(char[][] gameLevelMatrix) {

	char[][] tempMap  = new char[gameLevelMatrix.length][gameLevelMatrix[0].length];

	for(int i = 0; i < gameLevelMatrix.length; i++) {

	    for(int j = 0; j < gameLevelMatrix[i].length; j++) {

		tempMap[i][j] = gameLevelMatrix[i][j];
	    }
	}

	MAP = tempMap;
	FIRST_MAP_DRAW = true;
    }


    //	//return the player
    //	public static java.lang.Character newPlayer(char id, int x, int y, char[][] gameLevelMatrix){
    //
    //		Player p;
    //		if(!ABEngine.ENEMIES.containsKey(id)){
    //			p = Player.create(id, x, y);
    //			ABEngine.ENEMIES.put(id, p);
    //		} else {
    //			p = Player.create(x, y);
    //			ABEngine.ENEMIES.put(id, p);
    //		}
    //
    //		create_map(gameLevelMatrix);
    //
    //		//Rever retorno!!!
    //		if(p == null)
    //			return null;
    //
    //		return p.getID();
    //
    //	}

    public synchronized static void setPlayerAction(char id, CHARACTER_ACTION action){

	ABEngine.ENEMIES.get(id).setAction(action);

    }

    public synchronized static void setBombAction(char id, BOMB_ACTION action){

	ABEngine.ENEMIES.get(id).getBomb().setAction(action);

    }

    public static LoadingEvent getLoadingEvent() {
	return ABEngine.loadingEvent;
    }

    public synchronized static void sendPlayerAction(char playerId, CHARACTER_ACTION ca, boolean stop, boolean stopped, boolean hidden) {
	
	PlayerState ps = new PlayerState(playerId, Event.PLAYER, ca, stop, stopped, hidden);
	
	
	if(WiFiDirectBroadcastReceiver.client != null) {
	    ABEngine.sendStateMessage(ps);
	} else {
	    ABEngine.broadCastMessage(ps);
	} 
	

    }

    public synchronized static void sendDropBombAction(char playerId, BOMB_ACTION ba) {
	
	BombState bombState = new BombState(playerId, Event.BOMB, ba);

	if(WiFiDirectBroadcastReceiver.client != null) {
	    ABEngine.sendStateMessage(bombState);
	} else {
	    ABEngine.broadCastMessage(bombState);
	} 
	
	
    }

    public static void sendPausePlayer(char playerId, boolean pause) {
	//ReceiveCommTask.sendPausePlayer(pause, playerId);
    }  



    //Todas as threads actualizam o estado
    public static void processStateMessage(State message){

	switch(message.getEvent()){
	//Players action
	case PLAYER:
	    PlayerState pState = (PlayerState) message;
	    if(pState.getPlayerId() == ABEngine.PLAYER.getID() ){
		return;
	    }

	    ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());
	    Player player = ABEngine.ENEMIES.get(pState.getPlayerId());
	    player.STOP = pState.isStop();
	    player.STOPPED = pState.isStopped();
	    player.HIDDEN = pState.isHidden();
	    
//	    if(WiFiDirectBroadcastReceiver.client == null) {
//		ABEngine.broadCastMessage(pState);
//	    }

	    break;
	    //Players bomb
	case BOMB:
	    BombState bState = (BombState) message;
	    if(bState.getPlayerId() == ABEngine.PLAYER.getID() ){
		return;
	    }
	    ABEngine.setBombAction(bState.getPlayerId(), bState.getBombAction());
	    break;
	    //Received by the client
	case INIT://e aqui vao haver envios de volta?
	    InitState iState = (InitState) message;


	    if(ABEngine.LEVEL == null){
		ABEngine.LEVEL = iState.getLevel();
		ABEngine.create_map(iState.getLevel().getGameLevelMatrix()); 
		ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());
		//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
		ABEngine.loadingEvent.doLoadingEvent(true);

	    } else {

		//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
		ABEngine.ENEMIES.put(iState.getPlayerId(), Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY()));


	    }


	    break;

	case LEAVE:

	    LeaveState lState = (LeaveState) message;

	    ABEngine.ENEMIES.remove(lState.getPlayerId());

	    break;

	}
    }


    public static synchronized void sendStateMessage(State message){

	ObjectOutputStream os = null;

	try{


	    
	    os = new ObjectOutputStream(GO_SOCKET.getOutputStream());
	    os.reset();
	    os.writeObject(message);
	    os.flush();
	

	} catch (UnknownHostException e) {
	    Log.e("exception", "Don't know about host");
	} catch (IOException e) {
	    Log.e("exception", "Couldn't get I/O for the connection to host");
	} finally {

	   
		
		    //os.close();
		
	}

    }


    public static synchronized void broadCastMessage(State state) {


	for(ObjectOutputStream s : PEERSSTREAMS) {
	    try {
		s.writeObject(state);
	    } catch (IOException e) {
		e.printStackTrace();
	    }


	}

    }
    
    public static void drawMapDebug() {

	Log.w("map", "*******************************************");
	int max_x = ABEngine.getMaxX();
	int max_y = ABEngine.getMaxY();
	String map = "";
	for(int y = 0; y < max_y; y++) {

	    for(int x = 0; x < max_x; x++) {
		map += "" + ABEngine.getObject(x, y);
	    }

	    map += "\n";
	}
	Log.w("map", "*******************************************");
	Log.w("map", map);
    }

}
