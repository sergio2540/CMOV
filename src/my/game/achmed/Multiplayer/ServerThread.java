package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.Level;
import my.game.achmed.Characters.Player;
import my.game.achmed.State.Event;
import my.game.achmed.State.InitState;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

public class ServerThread implements Runnable {
    
	List<ServerInThread> inThread = new ArrayList<ServerInThread>();
	//ServerOutThread outThread;
	
	//List<Socket> peersSockets = new ArrayList<Socket>();
	
	ServerSocket goSocket;
	

	public ServerThread() {
		
		//outThread = new ServerOutThread();
		//new Thread(outThread).start();
		
		try {
			
			goSocket = new ServerSocket(9253);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public void accept() {


		while(true) { 
			
			Socket clientSocket = null;

			try {
				
			clientSocket = goSocket.accept();
			//peersSockets.add(clientSocket);
			ABEngine.PEERS.add(clientSocket);
				
			
				
				
				ServerInThread serverInThread = new ServerInThread(clientSocket);
				inThread.add(serverInThread);
				
				//outThread.addPeerSocket(clientSocket);
				
				
				
				new Thread(serverInThread).start();
				
				Player pl = ABEngine.createRandomPlayer();
				char id = pl.getID();
				
				ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
    		    
				ABEngine.PEERSSTREAMS.put(id, objOut);
				
				Event e = Event.INIT;
				int x = (int) pl.getXPosition();
				int y = (int) pl.getYPosition();
				ABEngine.ENEMIES.put(id, pl);
				ABEngine.setObject(x / 100, y/100, id);
				ABEngine.FIRST_MAP_DRAW = true;
				Level level = ABEngine.LEVEL;
				//Map<Character, Player> opponentsPlayers = ABEngine.ENEMIES;
				//List<Robot> robots  = ABEngine.ROBOTS;
				InitState initS = new InitState(id,e,x,y,level, clientSocket.getInetAddress());
				PeerData pd = new PeerData();
				pd.setIpAddress(clientSocket.getInetAddress().getHostAddress());
				ServerCom.playingPeersnew.put(id,pd);
				
				ABEngine.broadCastMessage(initS);
				
//				ReceiveCommTask.characters.put(clientSocket.getInetAddress().getHostAddress(), id);
//				ObjectOutputStream os = null;
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
	
//	public void sendStateToSendingThread(State state) {
//
//
//		if(this.outThread.servOutHandler != null) {
//
//			Message msg = new Message();
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("State", state);
//			msg.setData(bundle);
//			
//			this.outThread.servOutHandler.sendMessage(msg);
//			
//		}
//
//	}
	
	@Override
	public void run() {

	    this.accept();
	
	}

	
	
}
