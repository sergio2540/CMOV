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
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import my.game.achmed.R;
import my.game.achmed.Activities.ABGame;
import my.game.achmed.Characters.BOMB_ACTION;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.ROBOT_ACTION;
import my.game.achmed.Characters.Robot;
import my.game.achmed.Characters.SPLIT_ACTION;
import my.game.achmed.Events.LoadingEvent;
import my.game.achmed.Multiplayer.PeerData;
import my.game.achmed.Multiplayer.ServerCom;
import my.game.achmed.Multiplayer.p2p.WiFiDirectBroadcastReceiver;
import my.game.achmed.State.BombState;
import my.game.achmed.State.Event;
import my.game.achmed.State.InitState;
import my.game.achmed.State.LeaveState;
import my.game.achmed.State.PlayerState;
import my.game.achmed.State.ResponseState;
import my.game.achmed.State.RobotState;
import my.game.achmed.State.SplitState;
import my.game.achmed.State.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


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

	public static Map<Character, ObjectOutputStream> PEERSSTREAMS = new HashMap<Character, ObjectOutputStream>();

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

	public synchronized static void sendRobotAction(char robotId, CHARACTER_ACTION ra) {

		RobotState ps = new RobotState(robotId, Event.ROBOT, ra);


		if(WiFiDirectBroadcastReceiver.client != null) {
			ABEngine.sendStateMessage(ps);
		} else {
			ABEngine.broadCastMessage(ps);
		} 


	}

	public synchronized static void sendSplitAction(char playerId, SPLIT_ACTION sa, String newOwner, List<Character> selectedPlayers) {

		SplitState ss = new SplitState(playerId, Event.SPLIT, sa, newOwner, selectedPlayers);


		if(WiFiDirectBroadcastReceiver.client != null) {
			ABEngine.sendStateMessage(ss);
		} else {
			ABEngine.broadCastMessage(ss);
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
	static boolean connectSucess = false;

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

				final WifiManager wifiManager = (WifiManager) ABEngine.context.getSystemService(Context.WIFI_SERVICE);
				final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

				ABEngine.sendStateMessage(new ResponseState(ABEngine.PLAYER.getID(), Event.INIT_RESPONSE, connectionInfo.getMacAddress()));

			} else {

				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
				ABEngine.ENEMIES.put(iState.getPlayerId(), Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY()));


			}




			break;

		case INIT_RESPONSE:

			ResponseState rState = (ResponseState) message;
			if(rState.getPlayerId() == ABEngine.PLAYER.getID() ){
				return;
			}
			PeerData pd = ServerCom.playingPeersnew.get(rState.getPlayerId());
			pd.setMacAddress(rState.getMac());
			break;
		case LEAVE:

			LeaveState lState = (LeaveState) message;

			ABEngine.ENEMIES.remove(lState.getPlayerId());

			break;


		case SPLIT:

			SplitState sState = (SplitState) message;
			final WifiManager wifiManager = (WifiManager) ABEngine.context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			String myMac = connectionInfo.getMacAddress();


			if(sState.getPlayerId() == ABEngine.PLAYER.getID()){
				removeLeavers(sState);

				ABEngine.mManager.cancelConnect(ABEngine.mChannel, new ActionListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFailure(int reason) {
						// TODO Auto-generated method stub
						
					}
				});

				mManager.removeGroup(mChannel, new ActionListener() {

					@Override
					public void onSuccess() {

					}

					@Override
					public void onFailure(int reason) {

					}
				});

				ABEngine.mChannel = ABEngine.mManager.initialize(ABEngine.context, ABEngine.context.getMainLooper(), null);

				//se este peer tem o mesmo mac que o que vem na msg cria grupo
				if(myMac.equals(sState.getNewOwner())) {
					//create


					//ABEngine.mChannel = ABEngine.mManager.initialize(ABEngine.context, ABEngine.context.getMainLooper(), null);

					ABEngine.mManager.createGroup(ABEngine.mChannel, new ActionListener() {

						@Override
						public void onSuccess() {
							if(true) {
								int a = 2;
							}
						}

						@Override
						public void onFailure(int reason) {
							if(true) {
								int a = 2;
							}
						}

					});
				}
				return;
			}

			boolean foundNewOwner = false;
			SplitState newSplitState = null;

			//se e group owner
			if(WiFiDirectBroadcastReceiver.client == null) {

				removeLeavers(sState);
				//se o group owner pertence ao novo grupo
				if(sState.getPeers().contains(ABEngine.PLAYER.getID())) {

					//eliminar jogadores que nao estao na lista

					for(Entry<Character, ObjectOutputStream> c : ABEngine.PEERSSTREAMS.entrySet()) {
						//se o peer vai pertencer a um novo grupo
						if(sState.getPeers().contains(c.getKey())) {
							try {
								c.getValue().reset();
								c.getValue().writeObject(sState);
								c.getValue().flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {

							//se ja escolhi um novo (primeiro que vem aqui ao else)
							if(!foundNewOwner) {
								//manda a mensagem anterior ja mudada
								//envia

								//reescreves a msg e envias
								foundNewOwner = true;
								newSplitState = new SplitState(sState.getPlayerId(), sState.getEvent(), 
										sState.getSplitAction(), ServerCom.playingPeersnew.get(c.getKey()).getMacAddress(), sState.getPeers());

							}

							try {
								c.getValue().reset();
								c.getValue().writeObject(newSplitState);
								c.getValue().flush();
							} catch (IOException e) {
								e.printStackTrace();
							}

							//reescrever o mac address na mensagem e enviar

						}

					}

					//					mManager.removeGroup(mChannel, new ActionListener() {
					//
					//						@Override
					//						public void onSuccess() {
					//
					//						}
					//
					//						@Override
					//						public void onFailure(int reason) {
					//
					//						}
					//					});

					WifiP2pConfig config = new WifiP2pConfig();
					config.deviceAddress = sState.getNewOwner();
					config.groupOwnerIntent = 0;
					
					mManager.removeGroup(mChannel, new ActionListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int reason) {

						}
					});

					connectSucess = false;
					ABEngine.mManager.cancelConnect(ABEngine.mChannel, new ActionListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onFailure(int reason) {
							// TODO Auto-generated method stub
							
						}
					});
					
					
					
					//ABEngine.mChannel = ABEngine.mManager.initialize(ABEngine.context, ABEngine.context.getMainLooper(), null);
					while(!connectSucess) {
					
						ABEngine.mChannel = ABEngine.mManager.initialize(ABEngine.context, ABEngine.context.getMainLooper(), null);

						mManager.connect(mChannel, config, new ActionListener() {

							@Override
							public void onSuccess() {
								connectSucess = true;
							}

							@Override
							public void onFailure(int reason) {
								connectSucess = false;
							}
						} );

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					connectSucess = false;

					return;
					//se go nao pertence
				} else {

					newSplitState = new SplitState(sState.getPlayerId(), sState.getEvent(), 
							sState.getSplitAction(), myMac, sState.getPeers());

					for(Entry<Character, ObjectOutputStream> c : ABEngine.PEERSSTREAMS.entrySet()) {
						//se o peer vai pertencer a um novo grupo
						if(sState.getPeers().contains(c.getKey())) {
							try {
								c.getValue().reset();
								c.getValue().writeObject(sState);
								c.getValue().flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {

							//se ja escolhi um novo (primeiro que vem aqui ao else)
							//manda a mensagem anterior ja mudada
							//envia


							//reescreves a msg e envias

							try {
								c.getValue().reset();
								c.getValue().writeObject(newSplitState);
								c.getValue().flush();
							} catch (IOException e) {
								e.printStackTrace();
							}



							//reescrever o mac address na mensagem e enviar

						}
					}


				}	

				//				mManager.removeGroup(mChannel, new ActionListener() {
				//
				//					@Override
				//					public void onSuccess() {
				//
				//					}
				//
				//					@Override
				//					public void onFailure(int reason) {
				//
				//					}
				//				});


				//go -> destruir grupo

				//se e cliente
			} else {

				//eliminar jogadores que nao constam do novo grupo
				removeLeavers(sState);

				//se este peer tem o mesmo mac que o que vem na msg cria grupo
				if(myMac.equals(sState.getNewOwner())) {
					//create
					ABEngine.mManager.createGroup(ABEngine.mChannel, new ActionListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							if(true) {
								int a = 2;
							}

						}


						@Override
						public void onFailure(int reason) {
							// TODO Auto-generated method stub
							if(true) {
								int a = 2;
							}
						}

					});


				} else {
					//connect
					//ligar ao novo go
					WifiP2pConfig config = new WifiP2pConfig();
					config.deviceAddress = sState.getNewOwner();
					config.groupOwnerIntent = 0;
					
					ABEngine.mManager.cancelConnect(ABEngine.mChannel, new ActionListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onFailure(int reason) {
							// TODO Auto-generated method stub
							
						}
					});

					mManager.removeGroup(mChannel, new ActionListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int reason) {

						}
					});

					connectSucess = false;
					while(!connectSucess) {
						ABEngine.mChannel = ABEngine.mManager.initialize(ABEngine.context, ABEngine.context.getMainLooper(), null);

						mManager.connect(mChannel, config, new ActionListener() {

							@Override
							public void onSuccess() {
								connectSucess = true;
							}

							@Override
							public void onFailure(int reason) {
								connectSucess = false;
							}
						} );

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					connectSucess = false;

				}

			}



			//			if(WiFiDirectBroadcastReceiver.client == null) {
			//
			//				//Toast.makeText(WiFiDirectBroadcastReceiver, "SPLIT NO SERVER :", Toast.LENGTH_LONG).show();
			//
			//				broadCastMessage(sState);////deveria ser multicast e nao broadcast
			//
			//
			//			}
			//
			//			//Toast.makeText(ABEngine.context, "SPLIT NO CLIENT :", Toast.LENGTH_SHORT).show();
			//
			//			final WifiManager wifiMan = (WifiManager) ABEngine.context.getSystemService(Context.WIFI_SERVICE);
			//			final WifiInfo connInfo = wifiMan.getConnectionInfo();
			//			String macAddress = connInfo.getMacAddress();
			//
			//			boolean isForMe = sState.getPeers().contains(ABEngine.PLAYER.getID());
			//
			//			//if message is form me, i remove all peers that are not on this new group
			//			if(isForMe){
			//				for(Character enemy : ABEngine.ENEMIES.keySet())
			//					if(!sState.getPeers().contains(enemy)){
			//						ABEngine.ENEMIES.remove(enemy);
			//						//ABEngine.AVAILABLE_PLAYERS.put(enemy, )
			//					}
			//			}	
			//
			//			//remove this new group
			//			else
			//
			//				for(Character enemy : ABEngine.ENEMIES.keySet()){
			//					if(sState.getPeers().contains(enemy))
			//						ABEngine.ENEMIES.remove(enemy);
			//				}
			//
			//
			//
			//			mManager.removeGroup(mChannel, new ActionListener() {
			//
			//				@Override
			//				public void onSuccess() {
			//					// TODO Auto-generated method stub
			//
			//				}
			//
			//				@Override
			//				public void onFailure(int reason) {
			//					// TODO Auto-generated method stub
			//
			//				}
			//			});
			//			
			//		
			//
			//			//now we connect to the new group owner.
			//			WifiP2pConfig config = new WifiP2pConfig();
			//			config.deviceAddress = sState.getNewOwner();
			//			config.groupOwnerIntent = 0;
			//
			//			mManager.connect(mChannel, config, new ActionListener() {
			//
			//				@Override
			//				public void onSuccess() {
			//					// TODO Auto-generated method stub
			//
			//				}
			//
			//				@Override
			//				public void onFailure(int reason) {
			//					// TODO Auto-generated method stub
			//
			//				}
			//			} );

			break;

		case ROBOT: 

			RobotState robState = (RobotState) message;

			Robot rob = ABEngine.ROBOTS.get((int) robState.getPlayerId());
			rob.robotAction = robState.getRobotAction();

			break;

		}
	}

	public static void removeLeavers(SplitState sState) {

		boolean isForMe = ((sState.getPlayerId() == ABEngine.PLAYER.getID()) || sState.getPeers().contains(ABEngine.PLAYER.getID()));

		//if message is form me, i remove all peers that are not on this new group
		if(isForMe){
			for(Character enemy : ABEngine.ENEMIES.keySet())
				if((!sState.getPeers().contains(enemy)) && (sState.getPlayerId() != enemy)){
					ABEngine.ENEMIES.remove(enemy);
					//TODO: FAZER ISTO!!!!
					//ABEngine.AVAILABLE_PLAYERS.put(enemy, )
				}
		}	

		//remove this new group
		else

			for(Character enemy : ABEngine.ENEMIES.keySet()){
				if(sState.getPeers().contains(enemy) || (enemy == sState.getPlayerId()))
					ABEngine.ENEMIES.remove(enemy);
			}
	}

	public static synchronized void sendStateMessage(State message){

		ObjectOutputStream os = null;

		try {



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


		for(ObjectOutputStream s : PEERSSTREAMS.values()) {
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
