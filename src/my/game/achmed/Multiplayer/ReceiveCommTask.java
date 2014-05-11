package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import my.game.achmed.ABEngine;
import my.game.achmed.Activities.ABMultiplayer;
import my.game.achmed.Characters.BOMB_ACTION;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.Characters.Player;
import android.os.AsyncTask;
import android.util.Log;

//Processes input from clients/group owner
public class ReceiveCommTask extends AsyncTask<Void, String, Void> {

	private static Socket groupOwner = null;
	private static List<Socket> peers = new ArrayList<Socket>();
	public static HashMap<String,java.lang.Character> characters = new HashMap<String,java.lang.Character>();

	public ReceiveCommTask(Socket s) {
		groupOwner = s;
		
	}

	public ReceiveCommTask(){
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

		} catch (Exception e) {
			Log.w("readsocket", e.getMessage());
			cancel(true);
		}

		return message;

	}

	private void processStateMessage(State message){

		switch(message.getEvent()){
		//Players action
		case PLAYER:
			PlayerState pState = (PlayerState) message;
			if(pState.getPlayerId() == ABEngine.PLAYER.getID() ){
				return;
			}

			ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());
			Player player = ABEngine.PLAYERS.get(pState.getPlayerId());
			player.STOP = pState.isStop();
			player.STOPPED = pState.isStopped();
			player.HIDDEN = pState.isHidden();
			//if(isGroupOwner()) {
			//broadCastPlayerEvent(pState);
			//}

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

			ABEngine.LEVEL = iState.getLevel();

			if (ABEngine.LEVEL != null){
				ABEngine.create_map(iState.getLevel().getGameLevelMatrix()); 
			}

			ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());

		
			
			characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
			ABEngine.loadingEvent.doLoadingEvent(true);
			
			break;

		case LEAVE:

			LeaveState lState = (LeaveState) message;

			ABEngine.PLAYERS.remove(lState.getPlayerId());

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
				for(Socket peer : peers){

					message = readSocket(peer);
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
			cancel(true);
		}
	}

	public void broadCastPlayerEvent(State state) {

	    HashMap<String,Character> whoLeft = new HashMap<String,Character>();
	    List<Socket> newPeers = new ArrayList<Socket>();
		for(Socket peer : peers) {

			try {
				ObjectOutputStream out = new ObjectOutputStream(peer.getOutputStream());
				out.writeObject(state);
				newPeers.add(peer);
			} catch (IOException e) {
			    whoLeft.put(peer.getInetAddress().getHostAddress(),characters.get(peer.getInetAddress().getHostAddress()));
			    
				e.printStackTrace();
				//cancel(true);
			}
			
			
		}
		
		peers = newPeers;
		
		if(!whoLeft.isEmpty())
		    announceLeft(whoLeft);
	}
	
	public void announceLeft(HashMap<String, Character> toLeave){
	    
	    ObjectOutputStream os;
	    for(Entry<String,Character> ent : toLeave.entrySet())
	    {
		for(Socket s : peers){
		    
		    try {
			os = new ObjectOutputStream(s.getOutputStream());
			os.writeObject(new LeaveState(ent.getValue(), Event.LEAVE));
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		  
		    
		}
		
	    }
	    //removemos do mapa de caracteres.Temos de remover os sockets
	    for(Entry<String,Character> leaving : toLeave.entrySet()){
		    
		characters.remove(leaving.getKey());
  
		}
	    
	    
	}
	public static void sendPlayerAction(CHARACTER_ACTION ca, char playerId, boolean stop, boolean stopped, boolean hidden) {

		PlayerState ps = new PlayerState(playerId, Event.PLAYER, ca, stop, stopped, hidden);
		if (groupOwner == null)
		{


			for (Socket client : peers)
			{
				ObjectOutputStream os;
				try {
					os = new ObjectOutputStream(client.getOutputStream());
					os.writeObject(ps);
				} catch (IOException e) {
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

	public static void sendDropBombAction(BOMB_ACTION ba, char playerId) {

		BombState ps = new BombState(playerId, Event.BOMB, ba);
		if (groupOwner == null)
		{
			for (Socket client : peers)
			{
				ObjectOutputStream os;
				try {
					os = new ObjectOutputStream(client.getOutputStream());
					os.writeObject(ps);
				} catch (IOException e) {
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

	@Override
	protected void onCancelled(){

		if((peers != null) && (!peers.isEmpty()))
			for(Socket sock : peers)
			{
				try {
					sock.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

			}

		if(groupOwner != null)
			try {
				groupOwner.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
	}

}
