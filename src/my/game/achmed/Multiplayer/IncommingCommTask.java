package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Characters.Player;
import android.os.AsyncTask;
import android.util.Log;

//Class used to process communications to peer.
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
				Socket clientSocket = mSrvSocket.accept();
				peersSockets.add(clientSocket);
				Player pl = ABEngine.createRandomPlayer();
				InitState initS = new InitState(pl.getID(),Event.INIT,ABEngine.MAP, (int) pl.getXPosition(), (int) pl.getYPosition());


				for (Socket cSocket : peersSockets){
					ObjectOutputStream os = new ObjectOutputStream(cSocket.getOutputStream());
					os.writeObject(initS);
					os.close();
				}
				//ABEngine.PLAYER = Player.create(c,iState.getCoordX(),iState.getCoordY());
				//if (mCliSocket != null && mCliSocket.isClosed()) {
				//	mCliSocket = null;
				//}
				//if (mCliSocket != null) {
				//	Log.d(TAG, "Closing accepted socket because mCliSocket still active.");
				//	sock.close();
				//} else {
				publishProgress(clientSocket);
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


	@Override
	protected void onProgressUpdate(Socket... values) {
		if(rct == null){
			rct = new ReceiveCommTask();
			rct.execute();
		}
		rct.addPeers(values[0]);
	}


}