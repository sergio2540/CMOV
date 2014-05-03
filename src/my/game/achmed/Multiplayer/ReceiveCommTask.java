package my.game.achmed.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.Player;
import android.os.AsyncTask;
import android.util.Log;

//Processes input from clients/group owner
public class ReceiveCommTask extends AsyncTask<Void, String, Void> {

	private Socket groupOwner;
	private List<Socket> peers;

	public Socket getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(Socket groupOwner) {
		this.groupOwner = groupOwner;
	}

	public void addPeers(Socket s){
		peers.add(s);
	}


	private State readSocket(Socket s){

		ObjectInputStream objStream;
		State message = null;

		try {
			objStream = new ObjectInputStream(s.getInputStream());
			message = (State) objStream.readObject();
			objStream.close();


		} catch (IOException e) {
			Log.d("Error reading socket:", e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.d("Error reading socket2:", e.getMessage());

		}
		return message;

	}

	private void processStateMessage(State message){

		switch(message.getEvent()){
		//Players action
		case PLAYER:
			PlayerState pState = (PlayerState) message;
			ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());
			break;
			//Players bomb
		case BOMB:
			BombState bState = (BombState) message;
			ABEngine.setBombAction(bState.getPlayerId(), bState.getBombAction());
			break;
			//Received by the client
		case INIT:
			InitState iState = (InitState) message;
			ABEngine.create_map(iState.getGameMap()); 
			ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());
			break;
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		State message;
		while (!Thread.currentThread().isInterrupted()) {
			if (groupOwner != null)
				message = readSocket(groupOwner);
			else 
				for(Socket peerS : peers){
					message = readSocket(peerS);
					processStateMessage(message);
					//publishProgress();
				}
		}
		return null;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(String... values) {

	}

	@Override
	protected void onPostExecute(Void result) {

		try{
			if(!groupOwner.isClosed())
				groupOwner.close();
			for (Socket cSocket : peers){
				if(!cSocket.isClosed())
					cSocket.close();

			}

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
