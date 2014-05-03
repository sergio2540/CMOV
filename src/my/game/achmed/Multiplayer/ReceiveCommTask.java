package my.game.achmed.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
	
//para receber mensagens.
	public class ReceiveCommTask extends AsyncTask<Void, String, Void> {
		
		private Socket groupOwner;
		private List<Socket> peers;

		public Socket getGroupOwner() {
			return groupOwner;
		}

		public void setGroupOwner(Socket groupOwner) {
			this.groupOwner = groupOwner;
		}

		public List<Socket> getPeers() {
			return peers;
		}

		public void setPeers(List<Socket> peers) {
			this.peers = peers;
		}

		
		private State readSocket(Socket s){
			
			ObjectInputStream objStream;
			State message = null;
			
			try {
				objStream = new ObjectInputStream(s.getInputStream());
				message = (State) objStream.readObject();

			} catch (IOException e) {
				Log.d("Error reading socket:", e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.d("Error reading socket2:", e.getMessage());

			}
			
			return message;
			
		}
		
		private void processStateMessage(State message){
			
			switch(message.getEvent()){
				case PLAYER:
					PlayerState pState = (PlayerState) message;
				case BOMB:
					BombState bState = (BombState) message;

				case INIT:
					InitState iState = (InitState) message;

			
				
			
			
			
			}
			
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			State message;
			Socket s;
			if (groupOwner != null)
				message = readSocket(groupOwner);
			else 
				for(Socket peerS : peers){
					message = readSocket(peerS);
					//publishProgress();

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
			if (!s.isClosed()) {
				try {
					s.close();
				}
				catch (Exception e) {
					Log.d("Error closing socket:", e.getMessage());
				}
			}
			s = null;
			
		}
	}