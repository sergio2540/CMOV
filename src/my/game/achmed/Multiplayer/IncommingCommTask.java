package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import android.os.AsyncTask;
import android.util.Log;

//recebe ligacoes.
public class IncommingCommTask extends AsyncTask<Void, Socket, Void> {
	
	
		private Socket mCliSocket;
		private ServerSocket mSrvSocket;
		private String TAG = "===IncommingCommTask===";
		private ReceiveCommTask rct;
		private List<Socket> peersSockets;
		
		@Override
		protected Void doInBackground(Void... params) {

			peersSockets = new ArrayList<Socket>();
			//Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

			try {
				mSrvSocket = new ServerSocket(Integer.parseInt(ABEngine.context.getResources().getString(R.string.port)));
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			while (!Thread.currentThread().isInterrupted()) {
				try {
					peersSockets.add(mSrvSocket.accept());
					//if (mCliSocket != null && mCliSocket.isClosed()) {
					//	mCliSocket = null;
					//}
					//if (mCliSocket != null) {
					//	Log.d(TAG, "Closing accepted socket because mCliSocket still active.");
					//	sock.close();
					//} else {
					//	publishProgress(sock);
					//}
				} catch (IOException ioe) {
					Log.d("Error accepting socket:", ioe.getMessage());
					System.err.println(ioe.getMessage());
					break;
					//e.printStackTrace();
				}
			}
			
			return null;
		}

		public List<Socket> getPeersSockets() {
			return peersSockets;
		}



		@Override
		protected void onProgressUpdate(Socket... values) {
			mCliSocket = values[0];
			rct = new ReceiveCommTask();
			rct.execute(mCliSocket);
		}
		
		
	}