package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Activities.ABMultiplayer;
import my.game.achmed.Characters.Player;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//Class used to process communications to peer.
public class IncommingCommTask extends AsyncTask<Integer, Socket, Void> {


    private Socket mCliSocket;
    private ServerSocket mSrvSocket;
    private final String TAG = "===IncommingCommTask===";
    private ReceiveCommTask rct;
    private List<Socket> peersSockets;
    protected ABMultiplayer activity;

    @Override
    protected Void doInBackground(Integer... params) {

	peersSockets = new ArrayList<Socket>();
	//Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");
	

	
	try {
	    
	    //String port = getResource().getString(params[0]);
	    
	    mSrvSocket = new ServerSocket(9091);
	    
	    //Toast.makeText(activity, "Init send.", Toast.LENGTH_LONG).show();

	

	} catch (IOException e) {

	   

	}

	while (!Thread.currentThread().isInterrupted()) {
	    try {
		Socket clientSocket = mSrvSocket.accept();
		peersSockets.add(clientSocket);
		
		Player pl = ABEngine.createRandomPlayer();
		char id = pl.getID();
		Event e = Event.INIT;
		char[][] m = ABEngine.MAP;
		int x = (int) pl.getXPosition();
		int y = (int) pl.getYPosition();
		
		InitState initS = new InitState(id,e,m,x,y);


		for (Socket cSocket : peersSockets){
		    ObjectOutputStream os = new ObjectOutputStream(cSocket.getOutputStream());
		    //os.writeObject(n);
		    os.writeObject(initS);
		    //os.close();
		}

		
		publishProgress(clientSocket);
		
	    } catch (IOException e) {
		
		while(true){
		    Log.w("in", e.toString());
		}

		
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