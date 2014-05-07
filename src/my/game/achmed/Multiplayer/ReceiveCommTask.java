package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.Activities.ABMultiplayer;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.Characters.Player;
import android.os.AsyncTask;
import android.util.Log;

//Processes input from clients/group owner
public class ReceiveCommTask extends AsyncTask<Void, String, Void> {

    private static Socket groupOwner = null;
    private static List<Socket> peers = null;

    public ReceiveCommTask(Socket s) {
	groupOwner = s;
    }

    public ReceiveCommTask(){
	peers = new ArrayList<Socket>();
    }

    public Socket getGroupOwner() {
	return groupOwner;
    }

    public void setGroupOwner(Socket groupOwner) {
	this.groupOwner = groupOwner;
    }

    public boolean isGroupOwner() {
	return groupOwner == null ? true : false;
    }

    public void addPeers(Socket s){
	peers.add(s);
    }


    private State readSocket(Socket s){
	Log.w("readsocket", "ok");
	ObjectInputStream objStream;
	State message = null;

	try {

	    InputStream i = s.getInputStream();
	    objStream = new ObjectInputStream(i);
	    message = (State) objStream.readObject();
	    //objStream.close();

	} catch (IOException e) {
	    Log.w("readsocket", e.getMessage());
	} catch (ClassNotFoundException e) {
	    Log.w("readsocket", e.getMessage());

	}

	return message;

    }

    private void processStateMessage(State message){

	switch(message.getEvent()){
	//Players action
	case PLAYER:
	    PlayerState pState = (PlayerState) message;
	    ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());

	    //if(isGroupOwner()) {
		//broadCastPlayerEvent(pState);
	    //}

	    break;
	    //Players bomb
	case BOMB:
	    BombState bState = (BombState) message;
	    ABEngine.setBombAction(bState.getPlayerId(), bState.getBombAction());
	    break;
	    //Received by the client
	case INIT:
	    InitState iState = (InitState) message;

	    ABEngine.LEVEL = iState.getLevel();

	    if (ABEngine.LEVEL != null){
		ABEngine.create_map(iState.getLevel().getGameLevelMatrix()); 
	    }

	    ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());

	    //ABEngine.PLAYER = iState.getPlayer();
	    //le.doEvent(true);
	    ABEngine.loadingEvent.doLoadingEvent(true);

	    break;
	}
    }

    @Override
    protected Void doInBackground(Void... params) {

	State message;
	while (!Thread.currentThread().isInterrupted()) {
	    if (groupOwner != null){
		message = readSocket(groupOwner);
		processStateMessage(message);

	    }
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
	    e.printStackTrace();
	}
    }

    public void broadCastPlayerEvent(State state) {

	for(Socket peer : peers) {

	    try {
		ObjectOutputStream out = new ObjectOutputStream(peer.getOutputStream());
		out.writeObject(state);
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}

    }
    public static void sendPlayerAction(CHARACTER_ACTION ca, char playerId) {

	PlayerState ps = new PlayerState(playerId, Event.PLAYER, ca);
	if (groupOwner == null)
	{
	    for (Socket client : peers)
	    {
		 ObjectOutputStream os;
		try {
		    os = new ObjectOutputStream(client.getOutputStream());
	            os.writeObject(ps);
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
	    }	
	    return;

	}
	try {

	    ObjectOutputStream os = new ObjectOutputStream(groupOwner.getOutputStream());
	    os.writeObject(ps);

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }


}
