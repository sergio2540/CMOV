package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.Level;
import my.game.achmed.Characters.Player;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

public class ServerThread implements Runnable {

	
	List<InHandler> peersInHandlers;
	OutHandler peersOutHandlers;

	List<ServerInThread> inThread = new ArrayList<ServerInThread>();
	ServerOutThread outThread;
	
	List<Socket> peersSockets = new ArrayList<Socket>();
	
	ServerSocket goSocket;
	

	public ServerThread() {
		
		outThread = new ServerOutThread();
		new Thread(outThread).start();
		
		try {
			
			goSocket = new ServerSocket(9091);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		this.accept();
	}


	public void accept() {


		while(true) { 
			
			Socket clientSocket = null;

			try {
				
				clientSocket = goSocket.accept();

				peersSockets.add(clientSocket);
				outThread.addPeerSocket(clientSocket);
				ServerInThread serverInThread = new ServerInThread(clientSocket, outThread);
				inThread.add(serverInThread);
				new Thread(serverInThread).start();
				
				Player pl = ABEngine.createRandomPlayer();
				char id = pl.getID();
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
				
				this.sendStateToSendingThread(initS);
//				ReceiveCommTask.characters.put(clientSocket.getInetAddress().getHostAddress(), id);
//				ObjectOutputStream os = null;
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
	
	public void sendStateToSendingThread(State state) {


		if(this.outThread.servOutHandler != null) {

			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putSerializable("State", state);
			msg.setData(bundle);
			
			this.outThread.servOutHandler.sendMessage(msg);
			
		}

	}
	
	@Override
	public void run() {

			this.start();
	
	}

	
	
}
