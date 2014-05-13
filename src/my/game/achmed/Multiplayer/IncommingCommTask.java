package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import my.game.achmed.ABEngine;
import my.game.achmed.Level;
import my.game.achmed.Activities.ABMultiplayer;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;
import android.os.AsyncTask;
import android.util.Log;

//Class used to process communications to peer.
public class IncommingCommTask extends AsyncTask<Integer, Socket, Void> {

	private ServerSocket mSrvSocket;
	private ReceiveCommTask rct;
	private List<Socket> peersSockets;
	protected ABMultiplayer activity;

	@Override
	protected Void doInBackground(Integer... params) {

		peersSockets = new ArrayList<Socket>();
		//Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");



		try {

			//String port = getResource().getString(params[0]);

			//porta ta mal -> 9091
			mSrvSocket = new ServerSocket(9080);

			//Toast.makeText(activity, "Init send.", Toast.LENGTH_LONG).show();



		} catch (IOException e) {



		}

		while (!Thread.currentThread().isInterrupted()) {

			Socket clientSocket = null;

			try {
				clientSocket = mSrvSocket.accept();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
			    clientSocket.setSoTimeout(5);
			} catch (SocketException e2) {
			    // TODO Auto-generated catch block
			    e2.printStackTrace();
			}
			peersSockets.add(clientSocket);



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

			ReceiveCommTask.characters.put(clientSocket.getInetAddress().getHostAddress(), id);
			ObjectOutputStream os = null;

			for (Socket cSocket : peersSockets) {
				try {
					os = new ObjectOutputStream(cSocket.getOutputStream());
					os.writeObject(initS);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{

					//if(os != null)
					//	try {
							//os.close();
					//	} catch (IOException e1) {
							// TODO Auto-generated catch block
						//	e1.printStackTrace();
					//	}

				}
				//os.close();
			}



			publishProgress(clientSocket);


		}

		return null;
	}


	@Override
	protected void onProgressUpdate(Socket... values) {

		if(rct == null){
			rct = new ReceiveCommTask();
			rct.execute();
		}

		rct.addPeers(values[0]);
	}

	@Override
	protected void onCancelled(){

		if((peersSockets != null) && (!peersSockets.isEmpty()))
			for(Socket sock : peersSockets)
			{
				try {
					sock.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

			}

		if(mSrvSocket != null)
			try {
				mSrvSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		if(rct != null)
			rct.cancel(true);
	}
}