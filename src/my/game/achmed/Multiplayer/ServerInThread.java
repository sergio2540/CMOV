package my.game.achmed.Multiplayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.Player;
import android.os.Bundle;
import android.os.Message;

public class ServerInThread implements Runnable {

	Socket client;
	ServerOutThread servOutThread;

	public ServerInThread(Socket s, ServerOutThread servOutThread) {
		this.client = s;
		this.servOutThread = servOutThread;
	}


	@Override
	public void run() {

		ObjectInputStream inStream = null;
		State state;

		while(true) {

			try {

				InputStream i = client.getInputStream();
				inStream = new ObjectInputStream(i);


				while((state = (State) inStream.readObject()) != null){
					processStateMessage(state);
				}



			} catch (EOFException e){
				//Log.w("readsocket", e.getMessage());
			}
			catch (ClassNotFoundException e){

				//Log.w("readsocket", e.getMessage());

			} catch (IOException e) {
				//Log.w("readsocket", e.getMessage());
			}

			finally{

				try {
					if(inStream != null)
						inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}

	public void processStateMessage(State state) {


		//broadcast para todos os clientes
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putSerializable("State", state);
		msg.setData(bundle);

		this.updateGameState(state);


		//update

		servOutThread.servOutHandler.sendMessage(msg);


	}

	public synchronized void updateGameState(State message) {

		switch(message.getEvent()){
		//Players action
		case PLAYER:
			PlayerState pState = (PlayerState) message;
			if(pState.getPlayerId() == ABEngine.PLAYER.getID() ){
				return;
			}

			ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());
			Player player = ABEngine.ENEMIES.get(pState.getPlayerId());
			player.STOP = pState.isStop();
			player.STOPPED = pState.isStopped();
			player.HIDDEN = pState.isHidden();
			//			if(isGroupOwner()) {
			//				broadCastPlayerEvent(pState);
			//			}

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


			if(ABEngine.LEVEL == null){
				ABEngine.LEVEL = iState.getLevel();
				ABEngine.create_map(iState.getLevel().getGameLevelMatrix()); 
				ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());
				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
				ABEngine.loadingEvent.doLoadingEvent(true);

			} else {

				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
				ABEngine.ENEMIES.put(iState.getPlayerId(), Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY()));


			}


			break;

		case LEAVE:

			LeaveState lState = (LeaveState) message;

			ABEngine.ENEMIES.remove(lState.getPlayerId());

			break;

		}
}


}




